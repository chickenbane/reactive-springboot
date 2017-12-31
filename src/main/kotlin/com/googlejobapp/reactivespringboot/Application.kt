package com.googlejobapp.reactivespringboot

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.support.beans
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@SpringBootApplication
class ReactiveApplication {
    private val log = LoggerFactory.getLogger(ReactiveApplication::class.java)

    @Bean
    fun wha() = ApplicationRunner {
        log.info("oh hai")
    }

    @Bean
    fun router(service: HaiService): RouterFunction<ServerResponse> {
        return RouterFunctions.route(
                GET("/hai"), HandlerFunction { ok().body(service.generic(), Hai::class.java) }
        )
    }
}

@RestController
@RequestMapping("rc")
class HaiController(private val service: HaiService) {
    @GetMapping("/hai")
    fun generic(): Mono<Hai> = service.generic()

    @GetMapping("/hola/{name}")
    fun simple(@PathVariable name: String) = service.simple(name)
}

@Service
class HaiService {
    fun generic(): Mono<Hai> = Mono.just(Hai("ugh"))
    fun simple(name: String): Mono<Hai> = Mono.just(Hai("hola $name"))
}

data class Hai(val message: String)

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(ReactiveApplication::class.java)
            .initializers(beans {
                bean {
                    router {
                        val service = ref<HaiService>()
                        GET("/main") {
                            ok().body(service.generic(), Hai::class.java)
                        }
                    }
                }
            })
            .run(*args)
}

