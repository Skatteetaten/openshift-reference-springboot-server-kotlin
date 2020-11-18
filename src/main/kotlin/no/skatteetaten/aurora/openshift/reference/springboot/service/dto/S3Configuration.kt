package no.skatteetaten.aurora.openshift.reference.springboot.service.dto

import software.amazon.awssdk.services.s3.S3Client

data class S3Configuration(
    val s3Client: S3Client,
    val bucket: S3Properties.S3Bucket
)
