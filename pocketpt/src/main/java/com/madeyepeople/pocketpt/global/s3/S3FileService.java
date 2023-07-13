package com.madeyepeople.pocketpt.global.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@EnableAutoConfiguration
@Slf4j
public class S3FileService {
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public S3FileService(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String uploadFile(String classification, MultipartFile multipartFile) {
        try {
            // [1] 파일의 사이즈를 알려줌
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            // [2] 버킷 이름과 파일 경로 지정
            String fileUUID = UUID.randomUUID().toString(); // 파일 이름 생성
            String filePath = classification + fileUUID + "/" + multipartFile.getOriginalFilename();

            // [3] S3에 파일 업로드
            try (InputStream inputStream = multipartFile.getInputStream()) {
                PutObjectRequest request = new PutObjectRequest(bucketName, filePath, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3Client.putObject(request);
            } catch (IOException e) {
                log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
                throw new IllegalStateException("S3 파일 업로드에 실패했습니다.");
            }

            // [4] 업로드한 파일의 URL 반환
            return amazonS3Client.getUrl(bucketName, filePath).toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
