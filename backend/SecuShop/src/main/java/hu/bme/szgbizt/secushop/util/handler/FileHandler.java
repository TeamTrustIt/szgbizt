package hu.bme.szgbizt.secushop.util.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static hu.bme.szgbizt.secushop.service.AdminService.PATH_CAFF_DATA_JPG;
import static hu.bme.szgbizt.secushop.service.AdminService.PATH_CAFF_DATA_RAW;
import static hu.bme.szgbizt.secushop.util.Constant.FILE_EXTENSION_CAFF;

public class FileHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileHandler.class);

    public static final String DIRECTORY_USER = System.getProperty("user.dir");
    public static final String DIRECTORY_CAFF_DATA = DIRECTORY_USER + "\\caffdata";

    public static void createSubdirectoryForCiffData(String filename) {
        var fileDirectoryToSave = new File(DIRECTORY_CAFF_DATA + "\\jpg\\" + filename); // ../caffdata/jpg/<filename>/<ciffData>
        if (fileDirectoryToSave.mkdirs()) {
            LOGGER.debug("Successful created subdirectory for caff data [{}]", filename);
        }
    }

    public static void deleteCaffDataRaw(String filename) {

        var filenameWithExtension = filename + FILE_EXTENSION_CAFF;
        var pathCaffDataRaw = PATH_CAFF_DATA_RAW.resolve(filenameWithExtension);

        try {
            Files.delete(pathCaffDataRaw);
            LOGGER.info("Successful deleted caff data raw [{}]", filename);
        } catch (IOException e) {
            LOGGER.info("Failed deleted caff data raw [{}]", filename);
        }
    }

    public static void deleteCaffDataJpg(String filename) {

        var pathCaffDataJpg = PATH_CAFF_DATA_JPG.resolve(filename);

        try {
            Files.walk(pathCaffDataJpg)
                    .map(Path::toFile)
                    .forEach(File::delete);

            Files.delete(pathCaffDataJpg);

            LOGGER.info("Successful deleted caff data jpg [{}]", filename);
        } catch (IOException e) {
            LOGGER.info("Failed deleted caff data jpg [{}]", filename);
        }
    }

    private FileHandler() {
        // Empty constructor.
    }
}
