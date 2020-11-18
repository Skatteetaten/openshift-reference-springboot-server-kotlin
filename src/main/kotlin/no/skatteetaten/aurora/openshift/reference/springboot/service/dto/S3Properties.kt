package no.skatteetaten.aurora.openshift.reference.springboot.service.dto

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "s3")
@ConstructorBinding
data class S3Properties(
    val buckets: Map<String, S3Bucket>
) {
    data class S3Bucket(
        val serviceEndpoint: String,
        val accessKey: String,
        val secretKey: String,
        val objectPrefix: String,
        val bucketRegion: String,
        val bucketName: String
    )

    fun getBucketOrThrow(objectAreaName: String) = buckets[objectAreaName]
        ?: throw S3ConfigurationException("Could not find configured objectarea=$objectAreaName")
}

class S3ConfigurationException(message: String) : RuntimeException(message)
