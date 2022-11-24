package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.CaffData;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

public interface ICaffDataService {

    CaffData store(UUID callerUserId, String filename, String description, MultipartFile file);

    Stream<Path> loadAll();

    Resource load(String filename);

    Resource loadAsResource(UUID caffDataId);

    void delete(UUID caffDataId);
}
