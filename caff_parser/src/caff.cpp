#include "../inc/caff.h"
#include <string>
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "../inc/stb_image_write.h"
#include "../inc/json.hpp"

#include <fstream>
#include <iostream>
#include <stdint.h>

bool CAFF::ParseCAFF()
{
    std::ifstream input_stream;

    input_stream.open(m_path, std::ios::binary);
    if (input_stream.fail())
    {
        std::cout << "CAFF::Error - Couldn't open file!" << std::endl; 
        return false;
    }

    const Block* header_block = ReadBlock(input_stream);
    if (header_block->ID != HEADER_ID)
    {
        std::cout << "CAFF::Error - File doesn't start with Header! File couldn't be parsed!" << std::endl;
        delete header_block;
        input_stream.close();
        return false;
    }

    const Header* header = ReadHeader(input_stream, header_block);
    if (header == nullptr)
    {
        std::cout << "CAFF::Error - Error during Header parse!" << std::endl;
        delete header;
        delete header_block;
        input_stream.close();
        return false;
    }

    uint64_t remaining_anim_blocks = header->num_anim;
    uint64_t remaining_credit_blocks = 1;
    while(remaining_anim_blocks > 0 || remaining_credit_blocks > 0)
    {
        const Block* block = ReadBlock(input_stream);
        
        switch (block->ID)
        {
            case CREDITS_ID:
            {
                if (remaining_credit_blocks <=0 )
                {
                    std::cout << "CAFF::Error - More credit blocks than one! File couldn't be parsed!" << std::endl;
                    delete header;
                    delete header_block;
                    input_stream.close();
                    return false;
                }
                --remaining_credit_blocks;

                const Credits* credits = ReadCredits(input_stream, block);
                if (credits == nullptr)
                {
                    std::cout << "CAFF::Error - Error during Credits parse!" << std::endl;
                    delete header;
                    delete header_block;
                    input_stream.close();
                    return false;
                }
                delete credits;
                break;
            }
            case ANIMATION_ID:
            {
                if (remaining_anim_blocks <=0 )
                {
                    std::cout << "CAFF::Error - More animation blocks than specified in header! File couldn't be parsed!" << std::endl;
                    delete block;
                    delete header;
                    delete header_block;
                    input_stream.close();
                    return false;
                }
                --remaining_anim_blocks;
                
                const Animation* animation = ReadAnimation(input_stream, block);
                if (animation == nullptr)
                {
                    std::cout << "CAFF::Error - Error during Animation parse!" << std::endl;
                    delete block;
                    delete header;
                    delete header_block;
                    input_stream.close();
                    return false;
                }

                ParsedCIFFData ciff_data = animation->ciff->GetParsedData();
                ciff_data.duration = animation->duration;
                m_parsed_data.ciff_data.push_back(ciff_data);
                delete animation;
                break;
            }
        }

        delete block;
    }

    if (input_stream.rdbuf()->in_avail() > 0)
    {
        std::cout << "CAFF::Error - File contains more bytes than specified!" << std::endl;
        delete header;
        delete header_block;
        input_stream.close();
        return false;
    }

    delete header;
    delete header_block;
    input_stream.close();
    return true;
}

const CAFF::Block* CAFF::ReadBlock(std::ifstream& input_stream)
{
    Block* block = new Block();
    input_stream.read(reinterpret_cast<char*>(&block->ID), sizeof(block->ID));
    std::cout<< "Block ID: " << int(block->ID)<<std::endl;
    
    input_stream.read(reinterpret_cast<char*>(&block->length), sizeof(block->length));
    std::cout<< "Block length: " << int(block->length)<<std::endl;

    return block;
}

const CAFF::Header* CAFF::ReadHeader(std::ifstream& input_stream, const Block* block)
{
    std::cout << "Reading block as Header." << std::endl;

    Header* header = new Header();

    std::string magic_string = "";
    for (unsigned int i = 0; i < 4; ++i)
    {
        input_stream.read(reinterpret_cast<char*>(&header->magic[i]), sizeof(uint8_t));
        magic_string += header->magic[i];
    }
    std::cout << "Header magic: " << magic_string <<std::endl;
    if (magic_string != "CAFF")
    {
        std::cout << "CAFF::Error - Magic string doesn't equal 'CAFF'! File couldn't be parsed!" << std::endl;
        return nullptr;
    }

    input_stream.read(reinterpret_cast<char*>(&header->header_size), sizeof(header->header_size));
    std::cout << "Header size: " << header->header_size << std::endl;
    if (header->header_size != block->length)
    {
        std::cout << "CAFF::Error - Header size doesn't equal block data length! File couldn't be parsed!" << std::endl;
        return nullptr;
    }

    input_stream.read(reinterpret_cast<char*>(&header->num_anim), sizeof(header->num_anim));
    std::cout << "Header number of animated CIFFs: " << header->num_anim << std::endl;

    return header;
}

const CAFF::Credits* CAFF::ReadCredits(std::ifstream& input_stream, const Block* block)
{
    std::cout << "Reading block as Credits." << std::endl;

    Credits* credits = new Credits();

    input_stream.read(reinterpret_cast<char*>(&credits->year), sizeof(credits->year));
    std::cout << "Credits year: " << int(credits->year) <<std::endl;

    input_stream.read(reinterpret_cast<char*>(&credits->month), sizeof(credits->month));
    std::cout << "Credits month: " << int(credits->month) <<std::endl;

    input_stream.read(reinterpret_cast<char*>(&credits->day), sizeof(credits->day));
    std::cout << "Credits day: " << int(credits->day) <<std::endl;

    input_stream.read(reinterpret_cast<char*>(&credits->hour), sizeof(credits->hour));
    std::cout << "Credits hour: " << int(credits->hour) <<std::endl;

    input_stream.read(reinterpret_cast<char*>(&credits->minute), sizeof(credits->minute));
    std::cout << "Credits minute: " << int(credits->minute) <<std::endl;

    input_stream.read(reinterpret_cast<char*>(&credits->creator_len), sizeof(credits->creator_len));
    std::cout << "Credits creator length: " << credits->creator_len <<std::endl;

    if (credits->creator_len + 6 + 8 != block->length)
    {
        std::cout << "CAFF::Error - Credits size doesn't equal block data length! File couldn't be parsed!" << std::endl;
        return nullptr;
    }

    credits->creator.clear();
    m_parsed_data.credits_data.creator = "";
    for (unsigned int i = 0; i < credits->creator_len; ++i)
    {
        uint8_t creator_data;
        input_stream.read(reinterpret_cast<char*>(&creator_data), sizeof(creator_data));
        credits->creator.push_back(creator_data);
        m_parsed_data.credits_data.creator += creator_data;
    }
    std::cout << "Credits creator: " << m_parsed_data.credits_data.creator  <<std::endl;

    m_parsed_data.credits_data.year = credits->year;
    m_parsed_data.credits_data.month = credits->month;
    m_parsed_data.credits_data.day = credits->day;
    m_parsed_data.credits_data.hour = credits->minute;

    return credits;
}

const CAFF::Animation* CAFF::ReadAnimation(std::ifstream& input_stream, const Block* block)
{
    std::cout << "Reading block as Animation." << std::endl;

    Animation* animation = new Animation();

    input_stream.read(reinterpret_cast<char*>(&animation->duration), sizeof(animation->duration));
    std::cout<< "Animation duration: " << int(animation->duration)<<std::endl;

    animation->ciff = new CIFF(input_stream);

    if (!animation->ciff->ParseCIFF())
    {
        std::cout << "CAFF::Error - Error during CIFF parse!" << std::endl;
        return nullptr;
    }

    if (animation->ciff->GetHeaderSize() + animation->ciff->GetContentSize() + 8 != block->length)
    {
        std::cout << "CAFF::Error - Animation size doesn't equal block data length! File couldn't be parsed!" << std::endl;
        return nullptr;
    }
    
    return animation;
}

void CAFF::GenerateImage(std::string output_path, ParsedCIFFData ciff_data)
{
    uint8_t* pixels = new uint8_t[ciff_data.content_size];

    for (unsigned int j = 0; j < ciff_data.content_size; j += 3)
    {
        pixels[j + 0] = ciff_data.pixels[j / 3].R;
        pixels[j + 1] = ciff_data.pixels[j / 3].G;
        pixels[j + 2] = ciff_data.pixels[j / 3].B;
    }

    stbi_write_jpg(output_path.c_str(), ciff_data.width, ciff_data.height, 3, pixels, 100);
    std::cout<<"Image " << output_path << " generated!"<<std::endl;

    delete[] pixels;
}

void to_json(nlohmann::ordered_json& j, const ParsedCreditsData& data) 
{
    j = nlohmann::ordered_json{ 
            {"date", {
                {"year", data.year},
                {"month", data.month},
                {"day", data.day},
                {"hour", data.hour},
                {"minute", data.minute}
            }},
        {"creator", data.creator}
        };
}

void to_json(nlohmann::ordered_json& j, const ParsedCIFFData& data) 
{
    j = nlohmann::ordered_json{
        {"caption", data.caption},
        {"tags", data.tags},
        {"width", data.width},
        {"height", data.height},
        {"duration", data.duration},
        {"image", data.image_path}
        };
}

bool CAFF::GenerateOutput(std::string output_path)
{
    std::ofstream output_stream;
    output_stream.open(output_path);
    if (output_stream.fail())
    {
        std::cout << "Output::Error - Couldn't open file!" << std::endl; 
        output_stream.close();
        return false;
    }

    size_t extension_index = output_path.find_last_of("."); 
    for (unsigned int i = 0; i < m_parsed_data.ciff_data.size(); ++i) 
    {
        std::string image_path = output_path.substr(0, extension_index) + "_ciff" + std::to_string(i) + ".jpg";
        GenerateImage(image_path, m_parsed_data.ciff_data[i]);
        m_parsed_data.ciff_data[i].image_path = image_path;
    }
    
    nlohmann::ordered_json json_file;
    json_file["credits"] = m_parsed_data.credits_data;
    json_file["CIFFs"] = m_parsed_data.ciff_data;

    output_stream << json_file.dump(4) << std::endl;
    output_stream.close();
    return true;
}