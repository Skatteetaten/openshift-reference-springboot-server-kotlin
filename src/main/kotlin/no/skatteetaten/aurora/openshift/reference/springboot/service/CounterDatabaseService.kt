package no.skatteetaten.aurora.openshift.reference.springboot.service

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException

/**
 * An example service that demonstrates basic database operations.
 * For some reason if this is called CounterService it will not load?
 */
@Service
class CounterDatabaseService(private val jdbcTemplate: JdbcTemplate) {

    @Transactional
    fun getAndIncrementCounter(): Long {
        val counter = jdbcTemplate.queryForObject("SELECT value FROM counter FOR UPDATE OF value", Long::class.java)
            ?: throw IllegalStateException("counter table not initialized")
        jdbcTemplate.update("UPDATE counter SET value=value+1")
        return counter
    }

    val counter: Long get() = jdbcTemplate.queryForObject("SELECT value FROM counter", Long::class.java) ?: 0L
}
