# Micronaut projects license report

This project will checkout the various Micronaut modules and perform a transitive dependency license verification to generate a report. It will also generate a dependency tree for all modules and submodules that are being checked out.

# Excluding dependencies

If you want to add dependencies to be excluded from the report, either by full name or by a part of the name (e.g. if you add test, it will remove all dependencies that have test in their name), make a file in the src/resources folder named excluded_deps.txt and put all the dependencies you want excluded separated by a new line (one line, one dependency)

## How to use

Run `./gradlew licenseReportZip`
