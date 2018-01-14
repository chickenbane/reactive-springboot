package com.googlejobapp.reactivespringboot

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("c")
class CassandraController(private val repo: UserRepo) {
    private val log = LoggerFactory.getLogger(CassandraController::class.java)

    @ApiOperation("Get all users", response = User::class, responseContainer = "List", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(ApiResponse(code = 200, message = "All users"))
    @GetMapping("all")
    fun all(): Flux<User> = repo.findAll()

    @ApiOperation("Add a user", response = Unit::class, consumes = APPLICATION_JSON_VALUE)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Updated", response = Unit::class),
        ApiResponse(code = 201, message = "Created", response = Unit::class)
    ])
    @PostMapping("/")
    fun post(@RequestBody body: User): Mono<Void> {
        log.info("post: got body=$body")
        return repo.save(body).then()
    }

    @ApiOperation("Get a user", response = User::class, produces = APPLICATION_JSON_VALUE)
    @ApiResponses(ApiResponse(code = 200, message = "User"))
    @GetMapping("{username}")
    fun getUsername(@PathVariable username: String): Mono<ResponseEntity<User>> = repo.findById(username)
            .map { ok(it) }
            .switchIfEmpty(Mono.just(notFound().build()))
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
