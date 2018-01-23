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
    * Submit build `gcloud container builds submit --config cloudbuild.yaml .`
    * These operations appear to tarball current directory, so `./gradlew clean` before might be a good idea
    * Setup Build trigger in GCP Container Registry and cloudbuild.yaml
4. Deploy to GKE
    * Create cluster `gcloud container clusters create [CLUSTER-NAME]`
    * Create deployment `kubectl run rsb-deployment --image=gcr.io/talknerdy-one/reactive-springboot:latest --port 8080`
    * Create service `kubectl expose deployment rsb-deployment --type=LoadBalancer --port 80 --target-port 8080`
5. Infra as code
    * Recreate deployment.yaml and service.yaml to match kubectl commands
    * Setup Cloud DNS
6. Cleanup
    * Delete service `kubectl delete svc rsb-service`
    * Delete cluster `gcloud container clusters delete [CLUSTER-NAME]`


Attach to Cassandra
1. Create keyspace and table in Cassandra cluster
    * Use cqlsh locally `kubectl port-forward cassandra-0 9042`
    * `create keyspace rsb with replication = {'class':'SimpleStrategy', 'replication_factor':1};`
    * `use rsb;`
    * `create table users ( username text primary key, first text, last text, email text);`
    * Do operations
    * `insert into users (username, first, last, email) values ('bob', 'bob', 'smith', 'bobsmith@foo.com');`
    * `select * from users;`
2. Forward port to cluster `kubectl port-forward cassandra-0 9042`
3. Edit the configuration via environment variable `SPRING_DATA_CASSANDRA_CONTACT_POINTS=localhost`
```
curl -X POST \
     http://localhost:8080/cass/ \
     -H 'cache-control: no-cache' \
     -H 'content-type: application/json' \
     -H 'postman-token: b1c68ff0-ab83-a51c-7611-e555beac725a' \
     -d '{"username":"joey", "first": "joey", "last": "doe", "email": "hi@email.com"}'
```
