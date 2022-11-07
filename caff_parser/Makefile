SRC = ./src/*.cpp

caff_parser:
	g++ -fPIE -fstack-protector-strong -D_FORTIFY_SOURCE=2 -Wformat -Wformat-security $(SRC) -o caff_parser.exe