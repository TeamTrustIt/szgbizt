#include <iostream>
#include <string>

#include "../inc/caff.h"

int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        std::cout << "CAFF parser only accepts 2 parameters!" << std::endl;
        return 1;
    }

    std::string input_path = argv[1];
    std::string output_path = argv[2];

    std::cout << "Input file: " << input_path << std::endl;
    std::cout << "Output file: " << output_path << std::endl;

    CAFF caff(input_path);
    bool caff_parse_success = caff.ParseCAFF();

    if (caff_parse_success)
    {
        std::cout << "CAFF file successfully parsed!" << std::endl;
    }
    else
    {
        std::cout << "Error - CAFF file couldn't be parsed!" << std::endl;
    }

    return 0;
}