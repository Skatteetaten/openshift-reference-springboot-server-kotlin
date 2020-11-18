package no.skatteetaten.aurora.openshift.reference.springboot.service

import no.skatteetaten.aurora.openshift.reference.springboot.service.dto.S3Configuration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class S3Service(
    val defaultS3Configuration: S3Configuration,
    @Qualifier("otherArea")
    val otherS3Configuration: S3Configuration
) {
    fun putFileContent(keyName: String, fileContent: String, useDefaultObjectArea: Boolean) {
        val (s3Client, s3Bucket) = if (useDefaultObjectArea) defaultS3Configuration else otherS3Configuration
        val fullKeyName = "${s3Bucket.objectPrefix}/$keyName"
        s3Client.putObject(
            PutObjectRequest.builder().bucket(s3Bucket.bucketName).key(fullKeyName).build(),
            RequestBody.fromString(fileContent)
        )
    }

    fun getFileContent(keyName: String, useDefaultObjectArea: Boolean): String {
        val (s3Client, s3Bucket) = if (useDefaultObjectArea) defaultS3Configuration else otherS3Configuration
        val fullKeyName = "${s3Bucket.objectPrefix}/$keyName"
        return s3Client.getObjectAsBytes(
            GetObjectRequest.builder().bucket(s3Bucket.bucketName).key(fullKeyName).build()
        ).asUtf8String()
    }
}
