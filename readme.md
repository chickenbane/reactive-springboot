Steps to recreate

1. Do IntelliJ -> New Project -> Spring Initializer
    * Add Kotlin
    * Use Gradle Wrapper
    * Use SpringBoot2 WebFlux 
    * Add DevTools
2. Add Application
    * JUnit
    * Add endpoints via @Bean, @RestController, router DSL
3. Integrate with Google Cloud Container Builder
    * Added Dockerfile from https://spring.io/guides/gs/spring-boot-docker/
    * Example cloudbuild.yaml from https://github.com/GoogleCloudPlatform/cloud-builders
    * Add cloudbuild.yaml to enable the below command (which is the same as the command above)
    * `gcloud container builds submit --config cloudbuild.yaml .`
    * These operations appear to tarball current directory, so `./gradlew clean` before might be a good idea