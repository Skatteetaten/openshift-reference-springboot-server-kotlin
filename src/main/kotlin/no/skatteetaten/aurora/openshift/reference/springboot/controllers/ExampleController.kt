package no.skatteetaten.aurora.openshift.reference.springboot.controllers

import no.skatteetaten.aurora.AuroraMetrics.StatusValue.CRITICAL
import no.skatteetaten.aurora.AuroraMetrics.StatusValue.OK

import java.util.HashMap

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

import com.fasterxml.jackson.databind.JsonNode

import no.skatteetaten.aurora.AuroraMetrics

/*
 * An example controller that shows how to do a REST call and how to do an operation with a operations metrics
 * There should be a metric called http_client_requests http_server_requests and operations
 */
@RestController
class ExampleController(private val restTemplate: RestTemplate, private val metrics: AuroraMetrics) {

    @GetMapping("/api/example/ip")
    fun ip(): Map<String, Any> {
        val response = restTemplate.getForObject("http://httpbin.org/ip", JsonNode::class.java)
        val ip = response?.get("origin")?.textValue() ?: "Ip missing from response"
        return mapOf("ip" to ip)
    }

    @GetMapping("/api/example/sometimes")
    fun example(): Map<String, Any> {
        return metrics.withMetrics(SOMETIMES) {
            val wasSuccessful = performOperationThatMayFail()
            if (wasSuccessful) {
                metrics.status(SOMETIMES, OK)
                mapOf("result" to "Sometimes I succeed")
            } else {
                metrics.status(SOMETIMES, CRITICAL)
                throw RuntimeException("Sometimes I fail")
            }
        }
    }

    protected fun performOperationThatMayFail(): Boolean {

        val sleepTime = (Math.random() * SECOND).toLong()
        Thread.sleep(sleepTime)
        return sleepTime % 2 == 0L
    }

    companion object {

        private val SOMETIMES = "sometimes"
        private val SECOND = 1000
    }
}

