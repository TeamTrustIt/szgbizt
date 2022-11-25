package hu.bme.szgbizt.secushop.service;

import hu.bme.szgbizt.secushop.dto.CaffData;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ICaffDataService {

    CaffData store(UUID callerUserId, String filename, String description, MultipartFile file);

    List<CaffData> loadAll();

    CaffData load(UUID caffDataId);

    Resource loadAsResource(UUID caffDataId);

    void delete(UUID caffDataId);
}
