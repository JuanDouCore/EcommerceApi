package ar.com.juanferrara.ecommerceapi.business.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Builder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Region;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AmazonS3ClientService {
    private AmazonS3   amazonS3;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String  bucketName;
    @Value("${amazonProperties.accessKey}")
    private String  accessKey;
    @Value("${amazonProperties.secretKey}")
    private String  secretKey;

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials  credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("sa-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public void uploadFileToS3Bucket(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public void deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
    }

    public String getCompleteUrl() {
        return endpointUrl + "/" + bucketName + "/";
    }
}
