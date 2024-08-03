package com.TheMFG.HotelManagement.service;

import com.TheMFG.HotelManagement.dto.BookingDTO;
import com.TheMFG.HotelManagement.dto.RoomDTO;
import com.TheMFG.HotelManagement.dto.UserDTO;
import com.TheMFG.HotelManagement.exception.OurException;
import com.TheMFG.HotelManagement.model.Booking;
import com.TheMFG.HotelManagement.model.Room;
import com.TheMFG.HotelManagement.model.User;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwsS3Service {
    private final String bucketName = "themfg-hotel-images1";

    @Value("${aws.s3.access.key}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secret.key}")
    private String awsS3SecretKey;

    /* kullanıcı tarafından yüklenen bir fotoğrafı Amazon S3'e yükler ve bu resim S3 üzerindeki URL'sini döndürür. */
    public String saveImageToS3(MultipartFile photo){
        String s3LocationImage = null;

        try {
            String s3Filename = photo.getOriginalFilename();

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey,awsS3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_2)
                    .build();
            InputStream inputStream = photo.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,s3Filename,inputStream,metadata);
            s3Client.putObject(putObjectRequest);
            return "https://" + bucketName + ".s3.amazon.com/"+ s3Filename;
        }catch (Exception e){
            e.printStackTrace();
            throw new OurException("s3 Bucket'e resim yüklenemedi" + e.getMessage() );
        }
    }
}
