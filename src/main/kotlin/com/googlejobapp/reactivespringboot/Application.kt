package com.googlejobapp.reactivespringboot

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

@SpringBootApplication
class ReactiveApplication {
    private val log = LoggerFactory.getLogger(ReactiveApplication::class.java)
    @Bean
    fun wha() = ApplicationRunner {
        log.info("oh hai")
    }

    /*
    @Bean
    fun router() = RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.GET("/hai") {
            req -> ok().body
        }) }
    }
    */
}

@Service
class HaiService {
    fun generic(): Mono<String> = Mono.just("ugh")
}


fun main(args: Array<String>) {
    runApplication<ReactiveApplication>(*args)
}

