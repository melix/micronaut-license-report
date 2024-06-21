plugins {
    id("io.micronaut.build.license-report")
}

val checkoutRepo : String by project

licenseReports {
    val checkoutRepositories : List<String> = if (project.hasProperty("checkoutRepo")) checkoutRepo.split(",") else listOf();
    var repositoryNames : List<String> = checkoutRepositories.map {
        it.split('@').first();
    }

    listOf(
            "micronaut-openapi@v6.11.0"
    ).filter {
        !repositoryNames.contains(it.split('@').first())
    }.plus(
        checkoutRepositories
    ).filter {
      it.contains("micronaut")
    }.map {
        val (name, branch) = if (it.contains('@')) {
            it.split('@')
        } else {
            listOf(it, "master")
        }
        Pair("https://github.com/micronaut-projects/${name}.git", branch.trim())
    }.forEach {
        checkout(it.first, it.second)
    }
}
