package com.aprent.ApartmentRent.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 s3Client;

    private String bucketName = "chicken-bucket";

    public void uploadFile(String keyName, MultipartFile file) throws IOException {
        File fileObj = convertMultiPartFileToFile(file);
        s3Client.putObject(new PutObjectRequest(bucketName, keyName, fileObj));
        fileObj.delete();
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }


    public String generateFileKey(Long listingId, String originalFilename) {
        String fileExtension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
            fileExtension = originalFilename.substring(i);
        }
        String filenameWithoutExtension = originalFilename.substring(0, i);
        return "listings/" + listingId + "/" + filenameWithoutExtension + "-" + System.currentTimeMillis() + fileExtension;
    }
}
