package GDSCKNU.CitySavior.service.storage;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    public String saveFile(MultipartFile imgFiles);
}
