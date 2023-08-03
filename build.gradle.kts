plugins {
    id("io.micronaut.build.license-report")
}

licenseReports {
    listOf(
            "micronaut-core@v3.8.5",
            "micronaut-rxjava3",
            "micronaut-docs",
            "micronaut-reactor@v2.5.0",
            "micronaut-sql@v5.0.1",
            "micronaut-guides",
            "micronaut-data@v3.9.6",
            "micronaut-build",
            "micronaut-servlet",
            "micronaut-gradle-plugin",
            "micronaut-coherence",
            "micronaut-rxjava2@v1.3.0",
            "micronaut-kotlin",
            "micronaut-oracle-cloud@v2.3.4",
            "micronaut-gcp",
            "micronaut-starter",
            "micronaut-jms",
            "micronaut-kubernetes@v4.0.0",
            "micronaut-aws",
            "micronaut-cache@v3.5.0",
            "micronaut-grpc",
            "micronaut-kafka@v4.5.1",
            "micronaut-micrometer@v4.7.2",
            "micronaut-security@v3.9.2",
            "micronaut-discovery-client",
            "micronaut-pulsar",
            "micronaut-azure",
            "micronaut-aot",
            "micronaut-serialization@v1.5.0",
            "micronaut-spring",
            "micronaut-test@v3.8.2",
            "micronaut-mqtt",
            "micronaut-views",
            "micronaut-rabbitmq",
            "micronaut-neo4j",
            "micronaut-elasticsearch",
            "micronaut-hibernate-validator",
            "micronaut-rss",
            "micronaut-jmx",
            "micronaut-groovy",
            "micronaut-liquibase@v5.6.0",
            "micronaut-mongodb@v4.6.0",
            "micronaut-redis",
            "micronaut-problem-json",
            "micronaut-picocli@v4.3.0",
            "micronaut-openapi@v4.8.4",
            "micronaut-nats",
            "micronaut-multitenancy",
            "micronaut-jackson-xml@v3.2.0",
            "micronaut-ignite",
            "micronaut-graphql",
            "micronaut-cassandra",
            "micronaut-couchbase",
            "micronaut-acme",
            "micronaut-project-template",
            "micronaut-maven-plugin",
            "micronaut-camel",
            "micronaut-build-plugins",
    ).filter {
      it.contains("micronaut-sql")
    }.map {
        val (name, branch) = if (it.contains('@')) {
            it.split('@')
        } else {
            listOf(it, "master")
        }
        Pair("https://github.com/micronaut-projects/${name}.git", branch)
    }.forEach {
        checkout(it.first, it.second)
    }
}
