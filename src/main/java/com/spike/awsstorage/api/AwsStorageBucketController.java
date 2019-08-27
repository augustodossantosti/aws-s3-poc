package com.spike.awsstorage.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.Bucket;
import com.spike.awsstorage.service.AWSService;

@RestController
@RequestMapping("/buckets")
public class AwsStorageBucketController {

   private final AWSService awsService;

   public AwsStorageBucketController(AWSService awsService) {
      this.awsService = awsService;
   }

   @PostMapping
   public Bucket createBucket(@RequestParam String bucketName) {
      return awsService.createBucket(bucketName);
   }

   @GetMapping
   public List<Bucket> listBuckets() {
      return awsService.listBuckets();
   }

   @DeleteMapping
   public void deleteBucket(@RequestParam String bucketName) {
      awsService.deleteBucket(bucketName);
   }
}
