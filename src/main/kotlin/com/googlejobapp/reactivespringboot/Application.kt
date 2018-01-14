package com.googlejobapp.reactivespringboot

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class ReactiveApplication {
    @Bean
    fun sqsSwagger(): Docket = Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .paths(paths())
            .build()

    private fun paths(): Predicate<String> = Predicates.or(
            PathSelectors.regex("/hola.*"),
            PathSelectors.regex("/c/.*")
    )

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("Reactive SpringBoot")
                .description("Kotlin + SpringFox + WebFlux + Cassandra")
                .contact(Contact("Joey T", "https://github.com/chickenbane/reactive-springboot", "thechickenbane@gmail.com"))
                .license("Apache License Version 2.0")
                .licenseUrl("https://choosealicense.com/licenses/apache-2.0")
                .version("1.0")  // TODO get version
                .build()
    }
}


fun main(args: Array<String>) {
    SpringApplication.run(ReactiveApplication::class.java, *args)
}

@Controller
class SwaggerRedirectController {
    @GetMapping("/")
    fun swagger() = "redirect:swagger-ui.html"
}

@RestController
class HaiController(private val service: HaiService) {
    private val log = LoggerFactory.getLogger(HaiController::class.java)

    @ApiOperation("Basic greeting", response = Hai::class, produces = APPLICATION_JSON_VALUE)
    @ApiResponses(ApiResponse(code = 200, message = "Hi"))
    @GetMapping("/hola")
    fun generic(): Mono<Hai> = service.generic()

    @ApiOperation("Special greeting", response = Hai::class, produces = APPLICATION_JSON_VALUE)
    @ApiResponses(ApiResponse(code = 200, message = "Hi"))
    @GetMapping("/hola/{name}")
    fun simple(@PathVariable name: String): Mono<Hai> {
        log.debug("simple: got name=$name")
        return service.simple(name)
    }
}

@Service
class HaiService {
    fun generic(): Mono<Hai> = Mono.just(Hai("Hola human"))
    fun simple(name: String): Mono<Hai> = Mono.just(Hai("Hola $name"))
}

data class Hai(val message: String)
