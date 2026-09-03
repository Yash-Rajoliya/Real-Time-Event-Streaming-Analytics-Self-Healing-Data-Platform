// services/storage-connector/src/main/java/com/platform/storage/sink/S3SinkConnector.java
package com.platform.storage.sink;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3SinkConnector {

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;

    @Value("${storage.s3.bucket-name:platform-data-lake}")
    private String bucketName;

    private static final DateTimeFormatter PATH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH")
            .withZone(ZoneOffset.UTC);

    public String writeBatch(String dataset, List<Map<String, Object>> records) {
        if (records == null || records.isEmpty()) {
            return null;
        }

        Instant now = Instant.now();
        String datePath = PATH_FORMATTER.format(now);
        String fileKey = String.format("raw/%s/%s/batch-%d-%s.json.gz", 
                dataset, datePath, now.toEpochMilli(), UUID.randomUUID().toString().substring(0, 8));

        try {
            byte[] compressedData = compressJsonLines(records);

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .contentType("application/gzip")
                    .contentEncoding("gzip")
                    .build();

            log.info("Uploading batch of {} records ({} bytes) to S3 path [s3://{}/{}]", 
                    records.size(), compressedData.length, bucketName, fileKey);

            s3Client.putObject(putRequest, RequestBody.fromBytes(compressedData));
            return fileKey;

        } catch (Exception e) {
            log.error("Failed to upload batch to S3 path [{}/{}]: {}", bucketName, fileKey, e.getMessage(), e);
            throw new RuntimeException("S3 batch upload failed", e);
        }
    }

    private byte[] compressJsonLines(List<Map<String, Object>> records) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            for (Map<String, Object> record : records) {
                gzipStream.write(objectMapper.writeValueAsBytes(record));
                gzipStream.write('\n');
            }
            gzipStream.finish();
        }
        return byteStream.toByteArray();
    }
}