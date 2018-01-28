package org.joeytsai.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

// Setup a Spring Boot environment with Kotlin, like a gentleman
class KotlinBootPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("oh hai")
    }
}