package com.example

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry

@Configuration
@EnableRetry
class Configuration {

    @Bean
    fun service(): Service {
        return Service()
    }
}