Steps to recreate

1. Do IntelliJ -> New Project -> Spring Initializer
    * Add Kotlin
    * Use Gradle Wrapper
    * Use SpringBoot2 WebFlux 
    * Add DevTools
2. Add Application
    * JUnit
    * Add endpoints via @Bean, @RestController, router DSL
3. Docker container
    * Added Dockerfile from https://spring.io/guides/gs/spring-boot-docker/
    * Build locally `docker build -t reactive-springboot .`
    * Run locally `docker run --rm -p 8080:8080 reactive-springboot:latest`
4. Integrate with Google Cloud Container Builder
    * Example cloudbuild.yaml from https://github.com/GoogleCloudPlatform/cloud-builders
    * Add cloudbuild.yaml to enable the below command (which is the same as the command above)
    * `gcloud container builds submit --config cloudbuild.yaml .`
    * These operations appear to tarball current directory, so `./gradlew clean` before might be a good idea
    * Setup Build trigger in GCP Container Registry
4. Deploy to GKE
    * Create a cluster `gcloud container clusters create [CLUSTER-NAME]`
    * Create deployment rsb-deployment `kubectl run rsb-deployment --image=gcr.io/talknerdy-one/reactive-springboot:latest --port 8080`
    * Create service `kubectl expose deployment rsb-deployment --type=LoadBalancer --port 80 --target-port 8080`