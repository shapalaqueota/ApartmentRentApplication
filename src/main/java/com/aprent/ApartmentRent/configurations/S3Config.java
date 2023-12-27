package com.aprent.ApartmentRent.configurations;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Bean
    public AmazonS3 s3client() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("hb.vkcs.cloud", "kz-ast"))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("jCHihDpfjGEQvm7sCzvwoR", "4Mk5pL7w4NkCBgRwFi6gfTmExKLo3idY64PYfmBgSZap")))
                .build();
    }
}