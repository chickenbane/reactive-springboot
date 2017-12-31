package com.googlejobapp.reactivespringboot

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ReactiveApplication {
    private val log = LoggerFactory.getLogger(ReactiveApplication::class.java)
    @Bean
    fun wha() = ApplicationRunner {
        log.info("oh hai")
    }
}

fun main(args: Array<String>) {
    runApplication<ReactiveApplication>(*args)
}

