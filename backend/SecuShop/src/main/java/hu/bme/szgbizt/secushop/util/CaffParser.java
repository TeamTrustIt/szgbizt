package hu.bme.szgbizt.secushop.util;

import hu.bme.szgbizt.secushop.exception.CaffDataParsingException;

import java.io.File;
import java.io.IOException;

import static hu.bme.szgbizt.secushop.util.Constant.FILE_EXTENSION_CAFF;
import static hu.bme.szgbizt.secushop.util.Constant.FILE_EXTENSION_JSON;
import static hu.bme.szgbizt.secushop.util.handler.FileHandler.DIRECTORY_CAFF_DATA;

public class CaffParser {

    public static void runParseCommand(String filename) throws IOException, InterruptedException, CaffDataParsingException {

        var fileExe = DIRECTORY_CAFF_DATA + "\\caff_parser.exe";
        var inputFile = ".\\raw\\" + filename + FILE_EXTENSION_CAFF;
        var outputFile = ".\\jpg\\" + filename + "\\" + filename + FILE_EXTENSION_JSON;
        var command = fileExe + " " + inputFile + " " + outputFile; // caff_parser.exe <input_file.caff> <output_file.json>

        var process = Runtime.getRuntime().exec(
                command,
                null,
                new File(DIRECTORY_CAFF_DATA)
        );

        var exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new CaffDataParsingException();
        }
    }

    private CaffParser() {
        // Empty constructor.
    }
}
