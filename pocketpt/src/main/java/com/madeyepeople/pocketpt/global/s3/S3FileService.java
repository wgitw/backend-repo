package com.madeyepeople.pocketpt.global.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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

    public ResponseEntity<byte[]> downloadFile(String fileUrl) throws IOException {
        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucketName, fileUrl));
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(s3ObjectInputStream);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(contentType(fileUrl));
        httpHeaders.setContentLength(bytes.length);
        String[] arr = fileUrl.split("/");
        String type = arr[arr.length - 1];
        String fileName = URLEncoder.encode(type, "UTF-8").replaceAll("\\+", "%20");
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    private MediaType contentType(String keyName) {
        String[] arr = keyName.split("\\.");
        String type = arr[arr.length - 1];
        switch (type) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return  MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
