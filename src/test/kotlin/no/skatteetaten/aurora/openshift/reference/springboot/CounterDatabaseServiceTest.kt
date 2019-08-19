package no.skatteetaten.aurora.openshift.reference.springboot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate

import no.skatteetaten.aurora.openshift.reference.springboot.service.CounterDatabaseService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@JdbcTest
class CounterDatabaseServiceTest {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun `Verify maintains counter`() {

        val service = CounterDatabaseService(jdbcTemplate)

        var counter = service.getAndIncrementCounter()

        println(counter == 0L)

        counter = service.getAndIncrementCounter()

        println(counter == 1L)
        println(service.counter == 2L)
    }
}
