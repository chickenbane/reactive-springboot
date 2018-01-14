package com.googlejobapp.reactivespringboot

import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class HaiServiceTest {
    @Autowired lateinit var service: HaiService

    @Test
    @Ignore // TODO this breaks because we can't connect to Cassandra =/
    fun testGeneric() {
        val generic = service.generic().block()
        assertEquals("response", Hai("ugh"), generic)
    }
}