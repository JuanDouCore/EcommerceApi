package ar.com.juanferrara.ecommerceapi.business.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
public class ImageService {

    @Autowired
    private AmazonS3ClientService amazonS3Client;

    public String  uploadImage(MultipartFile multipartFile, String productName) {
        String newFileName = new Date().getTime() + "-" + productName.replaceAll(" ", "_");
        String fileUrl = "";

        try {
            File file = convertMultiPartToFile(multipartFile, newFileName);
            String fileName = file.getName();
            fileUrl = amazonS3Client.getCompleteUrl() + fileName;
            amazonS3Client.uploadFileToS3Bucket(fileName, file);
            file.delete();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public void deleteImage(String fileUrl) {
        amazonS3Client.deleteFileFromS3Bucket(fileUrl);
    }

    private File convertMultiPartToFile(MultipartFile multipartFile, String newFileName) throws IOException {
        String originalFileName = Objects.requireNonNull(multipartFile.getOriginalFilename());
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String newFileNameWithExtension = newFileName + fileExtension;

        File convFile = new File(newFileNameWithExtension);
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }
}
