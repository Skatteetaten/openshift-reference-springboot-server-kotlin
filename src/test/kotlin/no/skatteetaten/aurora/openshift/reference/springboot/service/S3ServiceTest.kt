package no.skatteetaten.aurora.openshift.reference.springboot.service

import io.findify.s3mock.S3Mock
import no.skatteetaten.aurora.openshift.reference.springboot.ApplicationConfig
import no.skatteetaten.aurora.openshift.reference.springboot.service.dto.S3Configuration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import software.amazon.awssdk.services.s3.model.CreateBucketRequest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RestClientTest(components = [ApplicationConfig::class, S3Service::class])
class S3ServiceTest {

    @Autowired
    private lateinit var s3Service: S3Service

    @Autowired
    private lateinit var s3Configuration: S3Configuration

    private val s3Mock = S3Mock.Builder().withInMemoryBackend().withPort(9000).build()

    @BeforeAll
    fun setup() {
        s3Mock.start()
        s3Configuration.s3Client.createBucket(CreateBucketRequest.builder().bucket("default").build())
    }

    @Test
    fun `verify is able to put and get file from s3 with default objectArea`() {
        val expectedFileContent = "my awesome test file"

        assertDoesNotThrow {
            s3Service.putFileContent("myFile.txt", expectedFileContent, true)
        }

        val fileContent = assertDoesNotThrow {
            s3Service.getFileContent("myFile.txt", true)
        }

        assertThat(fileContent).isEqualTo(expectedFileContent)
    }

    @Test
    fun `verify is able to put and get file from s3 with other objectArea`() {
        val expectedFileContent = "my awesome test file"

        assertDoesNotThrow {
            s3Service.putFileContent("myFile.txt", expectedFileContent, false)
        }

        val fileContent = assertDoesNotThrow {
            s3Service.getFileContent("myFile.txt", false)
        }

        assertThat(fileContent).isEqualTo(expectedFileContent)
    }
}
