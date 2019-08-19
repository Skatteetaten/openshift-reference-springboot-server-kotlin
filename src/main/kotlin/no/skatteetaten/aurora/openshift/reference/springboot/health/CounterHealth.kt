package no.skatteetaten.aurora.openshift.reference.springboot.health

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

import no.skatteetaten.aurora.openshift.reference.springboot.service.CounterDatabaseService

/**
 * A sample custom health check. You can add your own health checks that verifies the proper operational status of your
 * application.
 */
@Component
class CounterHealth(private val counterDatabaseService: CounterDatabaseService) : HealthIndicator {

    override fun health(): Health {
        val currentValue = counterDatabaseService.counter

        return if (currentValue % 2 == 0L) {
            Health.status("OBSERVE")
                .withDetail("message", "Even number in nominator")
                .withDetail("Count", currentValue)
                .build()
        } else {
            Health.up()
                .withDetail("Count", currentValue)
                .build()
        }
    }
}
