package com.spike.awsstorage.api;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.spike.awsstorage.service.AWSService;

@RestController
@RequestMapping("/objects")
public class AwsStorageObjectController {

   private final ResourceLoader resourceLoader;
   private final AWSService awsService;

   public AwsStorageObjectController(ResourceLoader resourceLoader, AWSService awsService) {
      this.resourceLoader = resourceLoader;
      this.awsService = awsService;
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public PutObjectResult uploadFile(@RequestParam String bucketName) throws IOException {
      final Resource resource = resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX + "upload-file.txt");
      return awsService.putObject(bucketName, resource.getFile());
   }

   @GetMapping
   public List<S3ObjectSummary> listFiles(@RequestParam String bucketName) {
      return awsService.listObjects(bucketName).getObjectSummaries();
   }
}
