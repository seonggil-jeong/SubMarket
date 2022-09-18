package com.submarket.itemservice.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3ServiceImpl implements com.submarket.itemservice.service.S3Service {

    private final AmazonS3Client amazonS3Client;
    private final Environment env;

    @Value("${s3.bucket}")
    public String url;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    @Override
    public String uploadImageInS3(MultipartFile multipartFile, String dirName) throws IOException {
        String rPath = "";
        // 사진 저장 후 사진 Path 를 return 함

        try {
            File uploadFile = uploadInLocal(multipartFile) // 파일 변환할 수 없으면 에러
                    .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

            rPath = uploadInS3(uploadFile, dirName);
            // 파일 저장 후 파일 Path return

        } catch (HttpStatusCodeException statusCodeException) {
            int code = statusCodeException.getRawStatusCode();
            log.info("statusCodeException : " + code + "(" + statusCodeException.getMessage() + ")");

            rPath = "/";

        } catch (IllegalArgumentException illegalArgumentException) {
            log.info("IllegalArgumentException : " + illegalArgumentException.getMessage());
            log.info("사진 저장 실패");
            rPath = "/";

        } catch (Exception exception) {
            log.info("Exception : " + exception);
            rPath = "/";

        } finally {

            return rPath;

        }
    }

    private String uploadInS3(File uploadFile, String dirName) {

        String fileName = dirName + "/" +  UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeFileInLocal(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }



    private Optional<File> uploadInLocal(MultipartFile file) throws IOException {

        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File 이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }


    private void removeFileInLocal(File targetFile) {

        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }
}
