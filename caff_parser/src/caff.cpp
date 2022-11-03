#include "../inc/caff.h"
#include <exception>
#include <fstream>
#include <iostream>
#include <stdint.h>

bool CAFF::ParseCAFF()
{
    std::ifstream input_stream;

    try
    {
        input_stream.open(m_path, std::ios::binary);
    }
    catch (std::exception e)
    {
        std::cout << "CAFF::Error - Couldn't open file:" << e.what() << std::endl; 
        return false;
    }

    const Block* header_block = ReadBlock(input_stream);
    if (header_block->ID != HEADER_ID)
    {
        std::cout << "CAFF::Error - File doesn't start with Header! File couldn't be parsed!" << std::endl;
        input_stream.close();
        return false;
    }

    const Header* header = ReadHeader(input_stream, header_block);
    if (header == nullptr)
    {
        std::cout << "CAFF::Error - Error during Header parse!" << std::endl;
        input_stream.close();
        return false;
    }

    const Block* block = ReadBlock(input_stream);
    if (block->ID != CREDITS_ID)
    {
        input_stream.close();
        return false;
    }

    const Credits* credits = ReadCredits(input_stream, block);

    for (int i = 0; i < header->num_anim; ++i)
    {
        const Block* anim_block = ReadBlock(input_stream);
        if (anim_block->ID != ANIMATION_ID)
        {
            input_stream.close();
            return false;
        }

        Animation* animation = new Animation();
        input_stream.read(reinterpret_cast<char*>(&animation->duration), sizeof(animation->duration));
        std::cout<< "Animation duration: " << int(animation->duration)<<std::endl;

        CIFF ciff(input_stream);
        bool ciff_parse_success = ciff.ParseCIFF();

        if (!ciff_parse_success)
        {
            std::cout << "CAFF::Error - Error during CIFF parse!" << std::endl;
            input_stream.close();
            return false;
        }
        else
        {
            std::cout << "CIFF file sucessfully parsed!" << std::endl;
        }
    }

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

    for (unsigned int i = 0; i < 4; ++i)
    {
        input_stream.read(reinterpret_cast<char*>(&header->magic[i]), sizeof(uint8_t));
    }

    std::string magic_string = "";
    for (unsigned int i = 0; i < 4; ++i)
    {
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

    credits->creator.clear();
    std::string creator_string = "";
    for (unsigned int i = 0; i < credits->creator_len; ++i)
    {
        uint8_t creator_data;
        input_stream.read(reinterpret_cast<char*>(&creator_data), sizeof(creator_data));
        credits->creator.push_back(creator_data);
        creator_string += creator_data;
    }
    std::cout << "Credits creator: " << creator_string <<std::endl;

    return credits;
}