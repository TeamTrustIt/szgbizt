#include <iostream>
#include <string>

#include "../inc/caff.h"

int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        std::cout << "Error - CAFF parser only accepts 2 parameters!" << std::endl;
        return 1;
    }

    std::string input_path = argv[1];
    std::string output_path = argv[2];

    std::cout << "Input file: " << input_path << std::endl;
    std::cout << "Output file: " << output_path << std::endl;

    if(input_path.substr(input_path.find_last_of(".") + 1) != "caff") 
    {
        std::cout << "Error - Input file doesn't have .caff extension!" << std::endl;
        return 1;
    }

    if(output_path.substr(output_path.find_last_of(".") + 1) != "json") 
    {
        std::cout << "Error - Output file doesn't have .json extension!" << std::endl;
        return 1;
    }

    CAFF caff(input_path);
    if (!caff.ParseCAFF())
    {
        std::cout << "Error - CAFF file couldn't be parsed!" << std::endl;
        return 1;
    }

    std::cout << "CAFF file successfully parsed!" << std::endl;
    if(!caff.GenerateOutput(output_path))
    {
        std::cout << "Error - Coulnd't create output files!" << std::endl;
        return 1;
    }

    std::cout << "Output files successfully generated!" << std::endl;
    return 0;
}