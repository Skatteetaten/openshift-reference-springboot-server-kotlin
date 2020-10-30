package no.skatteetaten.aurora.openshift.reference.springboot

import no.skatteetaten.aurora.openshift.reference.springboot.service.S3Properties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
@EnableConfigurationProperties(S3Properties::class)
class ApplicationConfig {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate = builder.build()

    @Bean
    fun defaultS3Client(s3Properties: S3Properties): S3Client {
        val s3Config =
            s3Properties.buckets["default"]
                ?: throw RuntimeException("Could not find s3 configuration with name=default")

        return S3Client.builder()
            .region(Region.of(s3Config.bucketRegion))
            .credentialsProvider {
                AwsBasicCredentials.create(s3Config.accessKey, s3Config.secretKey)
            }
            .endpointOverride(URI(s3Config.serviceEndpoint))
            .build()
    }
}
