package kr.co.vlink.Vlink.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;

@Service
@NoArgsConstructor
public class S3Service {
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    @Async
    public String upload(MultipartFile file, String folder) {
        String fileName = folder + "/" + new Date().toString() + file.getOriginalFilename();
        try {
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(bytes.length);
            s3Client.putObject(new PutObjectRequest(bucket, fileName, byteArrayInputStream, objMeta)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            return null;
        }
        return s3Client.getUrl(bucket, fileName).toString();
    }

    @Async
    public void deleteFile(String fileURL) {
        try {
            fileURL = URLDecoder.decode(fileURL, "UTF-8");
            String fileName = fileURL.substring(fileURL.lastIndexOf("https://vlink-data.s3.ap-northeast-2.amazonaws.com/") + 1);
            DeleteObjectsRequest deleteObjectRequest = new DeleteObjectsRequest(bucket).withKeys(fileName);
            s3Client.deleteObjects(deleteObjectRequest);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}