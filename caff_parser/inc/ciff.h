#pragma once

#include <cstdint>
#include <stdint.h>
#include <vector>
#include <fstream>

class CIFF
{
public:
    CIFF(std::ifstream& input_stream) : m_input_stream(input_stream) {};

    bool ParseCIFF();

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

    struct Pixel
    {
        uint8_t R;
        uint8_t G;
        uint8_t B;
    };
    const Pixel* ReadPixel(std::ifstream& input_stream);

private:
    std::ifstream& m_input_stream;
};