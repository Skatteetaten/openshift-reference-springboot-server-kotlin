package no.skatteetaten.aurora.openshift.reference.springboot

import no.skatteetaten.aurora.openshift.reference.springboot.service.dto.S3Configuration
import no.skatteetaten.aurora.openshift.reference.springboot.service.dto.S3Properties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.client.RestTemplate
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
@EnableConfigurationProperties(S3Properties::class)
class ApplicationConfig {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate = builder.build()

    @Primary
    @Bean
    fun defaultS3Client(
        s3Properties: S3Properties
    ): S3Configuration {
        return baseS3Configuration(s3Properties, "referanse-kotlin")
    }

    @Qualifier("otherArea")
    @Bean
    fun otherS3Client(
        s3Properties: S3Properties
    ): S3Configuration {
        return baseS3Configuration(s3Properties, "referanse-kotlin2")
    }

    private fun baseS3Configuration(s3Properties: S3Properties, objectAreaName: String): S3Configuration {
        val objectArea = s3Properties.getBucketOrThrow(objectAreaName)
        val s3Client = S3Client.builder()
            .region(Region.of(objectArea.bucketRegion))
            .credentialsProvider(
                StaticCredentialsProvider
                    .create(AwsBasicCredentials.create(objectArea.accessKey, objectArea.secretKey))
            ).endpointOverride(URI.create(objectArea.serviceEndpoint))
            .build()

        return S3Configuration(s3Client, objectArea)
    }
}
