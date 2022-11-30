#pragma once

#include <cstdint>
#include <stdint.h>
#include <vector>
#include <string>
#include <fstream>

#include "ciff.h"

struct ParsedCreditsData
{
    uint16_t year = 0;
    uint8_t month = 0;
    uint8_t day = 0;
    uint8_t hour = 0;
    uint8_t minute = 0;

    std::string creator;
};

struct ParsedCAFFData
{
    ParsedCreditsData credits_data;
    std::vector<ParsedCIFFData> ciff_data;
};

class CAFF
{
public:
    CAFF(std::string& path) : m_path(path) {};
    
    bool ParseCAFF();

    bool GenerateOutput(std::string output_path);

private:
    static constexpr uint8_t HEADER_ID = 0x1;
    static constexpr uint8_t CREDITS_ID = 0x2;
    static constexpr uint8_t ANIMATION_ID = 0x3;

    struct Block
    {
        uint8_t ID = 0;
        uint64_t length = 0;
    };
    const Block* ReadBlock(std::ifstream& input_stream);

    struct Header
    {
        uint8_t magic[4];
        uint64_t header_size = 0;
        uint64_t num_anim = 0;
    };
    const Header* ReadHeader(std::ifstream& input_stream, const Block* block);

    struct Credits
    {
        uint16_t year = 0;
        uint8_t month = 0;
        uint8_t day = 0;
        uint8_t hour = 0;
        uint8_t minute = 0;

        uint64_t creator_len = 0;
        std::vector<uint8_t> creator;
    };
    const CAFF::Credits* ReadCredits(std::ifstream& input_stream, const Block* block);

    struct Animation
    {
        uint64_t duration = 0;
        CIFF* ciff;
    };
    const Animation* ReadAnimation(std::ifstream& input_stream, const Block* block);

    void GenerateImage(std::string output_path, ParsedCIFFData ciff_data);

private:
    std::string& m_path;

    ParsedCAFFData m_parsed_data;
};