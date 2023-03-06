plugins {
    id("io.micronaut.build.license-report")
}

licenseReports {
    listOf(
//            "micronaut-core@3.7.x",
//            "micronaut-rxjava3",
//            "micronaut-docs",
//            "micronaut-reactor",
//            "micronaut-sql",
//            "micronaut-guides",
//            "micronaut-data",
//            "micronaut-build",
//            "micronaut-servlet",
//            "micronaut-gradle-plugin",
//            "micronaut-coherence",
//            "micronaut-rxjava2",
//            "micronaut-kotlin",
//            "micronaut-oracle-cloud",
//            "micronaut-gcp",
//            "micronaut-starter",
//            "micronaut-jms",
//            "micronaut-kubernetes",
//            "micronaut-aws",
//            "micronaut-cache",
//            "micronaut-grpc",
//            "micronaut-kafka",
//            "micronaut-micrometer",
//            "micronaut-security",
//            "micronaut-discovery-client",
//            "micronaut-pulsar",
//            "micronaut-azure",
//            "micronaut-aot",
//            "micronaut-serialization",
//            "micronaut-spring",
//            "micronaut-test",
//            "micronaut-mqtt",
//            "micronaut-views",
//            "micronaut-rabbitmq",
//            "micronaut-neo4j",
//            "micronaut-elasticsearch",
//            "micronaut-hibernate-validator",
//            "micronaut-rss",
//            "micronaut-jmx",
//            "micronaut-groovy",
//            "micronaut-liquibase",
//            "micronaut-mongodb",
//            "micronaut-redis",
//            "micronaut-problem-json",
//            "micronaut-picocli",
            "micronaut-openapi@v4.5.2",
//            "micronaut-nats",
//            "micronaut-multitenancy",
//            "micronaut-jackson-xml",
//            "micronaut-ignite",
//            "micronaut-graphql",
//            "micronaut-cassandra",
//            "micronaut-couchbase",
//            "micronaut-acme",
//            "micronaut-project-template",
//            "micronaut-maven-plugin",
//            "micronaut-camel",
//            "micronaut-build-plugins",
    ).filter {
      it.contains("micronaut")
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
