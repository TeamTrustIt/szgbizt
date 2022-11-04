#include "../inc/ciff.h"
#include <fstream>
#include <iostream>
#include <stdint.h>

bool CIFF::ParseCIFF()
{
    const Header* header = ReadHeader(m_input_stream);
    if (header == nullptr)
    {
        std::cout << "CIFF::Error - Error during Header parse!" << std::endl;
        return false;
    }
    m_header_size = header->header_size;
    m_content_size = header->content_size;

    for (unsigned int i = 0; i < header->content_size / 3; ++i)
    {
        const Pixel* pixel = ReadPixel(m_input_stream);
        m_parsed_data.pixels.push_back({pixel->R, pixel->G, pixel->B});
        delete pixel;
    }

    delete header;
    return true;
}

const CIFF::Header* CIFF::ReadHeader(std::ifstream& input_stream)
{
    Header* header = new Header();

    std::string magic_string = "";
    for (unsigned int i = 0; i < 4; ++i)
    {
        input_stream.read(reinterpret_cast<char*>(&header->magic[i]), sizeof(uint8_t));
        magic_string += header->magic[i];
    }
    std::cout << "Header magic: " << magic_string <<std::endl;
    if (magic_string != "CIFF")
    {
        std::cout << "CIFF::Error - Magic string doesn't equal 'CIFF'! File couldn't be parsed!" << std::endl;
        return nullptr;
    }

    input_stream.read(reinterpret_cast<char*>(&header->header_size), sizeof(header->header_size));
    std::cout << "Header size: " << header->header_size << std::endl;

    input_stream.read(reinterpret_cast<char*>(&header->content_size), sizeof(header->content_size));
    std::cout << "Header Content size: " << header->content_size << std::endl;
    m_parsed_data.content_size = header->content_size;

    input_stream.read(reinterpret_cast<char*>(&header->width), sizeof(header->width));
    std::cout << "Header Width: " << header->width << std::endl;
    m_parsed_data.width = header->width;

    input_stream.read(reinterpret_cast<char*>(&header->height), sizeof(header->height));
    std::cout << "Header Height: " << header->height << std::endl;
    m_parsed_data.height = header->height;

    if (header->content_size != header->width * header->height * 3)
    {
        std::cout << "CIFF::Error - Content Size doesn't equal Width * Height * 3! File couldn't be parsed!" << std::endl;
        return nullptr;
    }

    header->caption.clear();
    uint8_t caption_char = ' ';
    m_parsed_data.caption = "";
    do
    {
        input_stream.read(reinterpret_cast<char*>(&caption_char), sizeof(caption_char));
        header->caption.push_back(caption_char);
        m_parsed_data.caption += caption_char;
    }
    while (caption_char != '\n');
    m_parsed_data.caption.pop_back();
    std::cout << "Caption: " << m_parsed_data.caption << std::endl;

    uint64_t tags_size = header->header_size - 4 - 8 - 8 - 8 - 8 - header->caption.size();
    std::cout << "Header Tags size: " << tags_size << std::endl;

    header->tags.clear();
    uint8_t tags_char = ' ';
    std::cout << "Header Tags:" << std::endl;
    while (tags_size > 0)
    {
        std::string tag = "";
        do
        {
            if (tags_size <= 0)
            {
                std::cout << "CIFF::Error - Last tag doesn't end with '\\0'! File couldn't be parsed!" << std::endl;
                return nullptr;
            }

            input_stream.read(reinterpret_cast<char*>(&tags_char), sizeof(tags_char));

            if (tags_char == '\n')
            {
                std::cout << "CIFF::Error - Tag contains '\\n' character! File couldn't be parsed!" << std::endl;
                return nullptr;
            }

            header->tags.push_back(tags_char);
            tag += tags_char;
            --tags_size;
        }
        while (tags_char != '\0');
        tag.pop_back();
        std::cout << tag << std::endl;
        m_parsed_data.tags.push_back(tag);
    }

    return header;
}

const Pixel* CIFF::ReadPixel(std::ifstream& input_stream)
{
    Pixel* pixel = new Pixel();

    input_stream.read(reinterpret_cast<char*>(&pixel->R), sizeof(pixel->R));
    input_stream.read(reinterpret_cast<char*>(&pixel->G), sizeof(pixel->G));
    input_stream.read(reinterpret_cast<char*>(&pixel->B), sizeof(pixel->B));

    return pixel;
}