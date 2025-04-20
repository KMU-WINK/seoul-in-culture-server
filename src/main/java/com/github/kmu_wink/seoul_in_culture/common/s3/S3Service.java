package com.github.kmu_wink.seoul_in_culture.common.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.github.kmu_wink.seoul_in_culture.common.property.AwsProperty;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class S3Service {

    private final AwsProperty awsProperty;
    private final AmazonS3Client amazonS3Client;

    public List<S3ObjectSummary> files(String prefix) {

        return amazonS3Client.listObjects(awsProperty.getS3().getBucket(), prefix).getObjectSummaries();
    }

    @SneakyThrows(IOException.class)
    public String upload(String path, MultipartFile file) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        amazonS3Client.putObject(new PutObjectRequest(
                awsProperty.getS3().getBucket(),
                path,
                file.getInputStream(),
                objectMetadata
        ).withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(awsProperty.getS3().getBucket(), path).toString();
    }

    public String upload(String path, File file) {

        amazonS3Client.putObject(new PutObjectRequest(awsProperty.getS3().getBucket(), path, file).withCannedAcl(
                CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(awsProperty.getS3().getBucket(), path).toString();
    }

    public void deleteFile(String path) {

        amazonS3Client.deleteObject(awsProperty.getS3().getBucket(), path);
    }

    public Optional<String> urlToKey(String url) {

        if (!url.contains("amazonaws.com/")) {
            return Optional.empty();
        }

        return Optional.of(url.split("amazonaws.com/")[1]);
    }
}