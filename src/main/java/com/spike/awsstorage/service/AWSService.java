package com.spike.awsstorage.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class AWSService {
   private static final Logger LOG = LoggerFactory.getLogger(AWSService.class);

   private static final String BUCKET_INVALID_NAME = "This bucket already exists";

   private final AmazonS3 s3client;
   private final String uploadFolder;

   public AWSService(@Value("${amazon.s3.accessKey}") String accessKey, @Value("${amazon.s3.secretKey}") String secretKey, @Value("${amazon.s3.uploadFolder}") String uploadFolder) {
      this.uploadFolder = uploadFolder;
      this.s3client = AmazonS3ClientBuilder
              .standard()
              .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
              .withRegion(Regions.US_EAST_2)
              .build();
   }

   public Bucket createBucket(String bucketName) {
      if(s3client.doesBucketExistV2(bucketName)) {
         LOG.error(BUCKET_INVALID_NAME);
         throw new IllegalArgumentException(BUCKET_INVALID_NAME);
      }
      return s3client.createBucket(bucketName);
   }

   public List<Bucket> listBuckets() {
      return s3client.listBuckets();
   }

   public void deleteBucket(String bucketName) {
      s3client.deleteBucket(bucketName);
   }

   public PutObjectResult putObject(String bucketName, File file) {
      return s3client.putObject(bucketName, uploadFolder + file.getName(), file);
   }

   public ObjectListing listObjects(String bucketName) {
      return s3client.listObjects(bucketName);
   }

   public S3Object getObject(String bucketName, String objectKey) {
      return s3client.getObject(bucketName, objectKey);
   }

   public CopyObjectResult copyObject(
           String sourceBucketName,
           String sourceKey,
           String destinationBucketName,
           String destinationKey
   ) {
      return s3client.copyObject(
              sourceBucketName,
              sourceKey,
              destinationBucketName,
              destinationKey
      );
   }

   public void deleteObject(String bucketName, String objectKey) {
      s3client.deleteObject(bucketName, objectKey);
   }

   public DeleteObjectsResult deleteObjects(DeleteObjectsRequest delObjReq) {
      return s3client.deleteObjects(delObjReq);
   }

}
