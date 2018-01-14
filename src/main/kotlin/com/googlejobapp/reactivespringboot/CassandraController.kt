package com.googlejobapp.reactivespringboot

import org.slf4j.LoggerFactory
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("c")
class CassandraController(private val repo: UserRepo) {
    private val log = LoggerFactory.getLogger(CassandraController::class.java)

    @GetMapping("all")
    fun all(): Flux<User> = repo.findAll()

    @PostMapping
    fun post(@RequestBody body: User): Mono<Void> {
        log.info("post: got body=$body")
        return repo.save(body).then()
    }

    @GetMapping("{username}")
    fun getUsername2(@PathVariable username: String): Mono<User> = repo.findById(username)

    // TODO this endpoint doesn't really work =/
    @GetMapping("cass/{username}")
    fun getUsername(@PathVariable username: String): Mono<ServerResponse> {
        return repo.findById(username)
                .flatMap { ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(it), User::class.java) }
                .switchIfEmpty(notFound().build())
    }
}

// create table users ( username text primary key, first text, last text, email text)

@Table("users")
data class User(
        @PrimaryKey
        var username: String,
        var first: String,
        var last: String,
        var email: String
)

@Repository
interface UserRepo : ReactiveCassandraRepository<User, String>
