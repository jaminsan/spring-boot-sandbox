package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Bootstrap {

    fun main(args: Array<String>) {
        SpringApplication.run(Bootstrap::class.java, *args)
    }

}