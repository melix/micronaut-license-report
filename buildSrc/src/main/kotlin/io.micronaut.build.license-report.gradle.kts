import io.micronaut.build.LicenseExtension
import io.micronaut.build.GenerateReport

val licenseReport = extensions.create<LicenseExtension>("licenseReports", layout.projectDirectory.file("src/init.gradle"))

val allReports by tasks.registering {
    dependsOn(tasks.withType<GenerateReport>())
}

val licenseReportZip = tasks.register<Zip>("licenseReportZip") {
    dependsOn(allReports)
    destinationDirectory.set(layout.buildDirectory)
    archiveFileName.set("license-report.zip")
    from(layout.buildDirectory.dir("reports"))
}
