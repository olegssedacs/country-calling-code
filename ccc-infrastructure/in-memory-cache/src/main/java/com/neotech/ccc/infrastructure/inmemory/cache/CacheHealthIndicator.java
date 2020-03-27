package com.neotech.ccc.infrastructure.inmemory.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CacheHealthIndicator implements HealthIndicator {

    private static final String ACTUATOR_HEALTH_KEY = "caching_country_calling_codes_map_repository";
    private final CachingCountryCallingCodesMapRepository repository;

    @Autowired
    public CacheHealthIndicator(CachingCountryCallingCodesMapRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {
        return repository.getCachedValue() != null
               ? up()
               : down();
    }

    private Health up() {
        return Health.up()
                     .withDetail(ACTUATOR_HEALTH_KEY, "Available")
                     .build();
    }

    private Health down() {
        return Health.down()
                     .withDetail(ACTUATOR_HEALTH_KEY, "Unavailable")
                     .build();
    }
}
