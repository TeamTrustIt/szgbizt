#pragma once

#include <cstdint>
#include <stdint.h>
#include <vector>
#include <fstream>

struct Pixel
{
    uint8_t R;
    uint8_t G;
    uint8_t B;
};

struct ParsedCIFFData
{
    std::string caption;
    std::vector<std::string> tags;

    uint64_t width;
    uint64_t height;
    uint8_t duration;

    std::string image_path = "";

    uint64_t content_size;
    std::vector<Pixel> pixels;
};

class CIFF
{
public:
    CIFF(std::ifstream& input_stream) : m_input_stream(input_stream) {};

    bool ParseCIFF();

    ParsedCIFFData GetParsedData()
    {
        return m_parsed_data;
    }

    uint64_t GetHeaderSize()
    {
        return m_header_size;
    }

    uint64_t GetContentSize()
    {
        return m_content_size;
    }

private:
    struct Header
    {
        uint8_t magic[4];
        uint64_t header_size;
        uint64_t content_size;

        uint64_t width;
        uint64_t height;

        std::vector<uint8_t> caption;
        std::vector<uint8_t> tags;
    };
    const Header* ReadHeader(std::ifstream& input_stream);

    const Pixel* ReadPixel(std::ifstream& input_stream);

private:
    std::ifstream& m_input_stream;
    
    ParsedCIFFData m_parsed_data;

    uint64_t m_header_size;
    uint64_t m_content_size;
};