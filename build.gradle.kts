import com.github.zafarkhaja.semver.Version
import org.gradle.api.JavaVersion.VERSION_14
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
  base
  kotlin("jvm") version kotlinVersion
  id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
  id("org.jetbrains.gradle.plugin.idea-ext")
  id("com.dorongold.task-tree") version "1.5"
}

@Suppress("UnstableApiUsage")
val javaVersion by extra(VERSION_14)

repositories {
  jcenter()
  mavenCentral()
}

val mainSrc = setOf("src")
val mainResources = setOf("srcResources")
val testSrc = setOf("tests")
val testResources = setOf("testsResources")

configure<JavaPluginExtension> {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

configure<SourceSetContainer> {
  named("main") {
    java.setSrcDirs(mainSrc)
    resources.setSrcDirs(mainResources)
  }
  named("test") {
    java.setSrcDirs(testSrc)
    resources.setSrcDirs(testResources)
  }
}

configure<KotlinJvmProjectExtension> {
  sourceSets {
    named("main") {
      kotlin.setSrcDirs(mainSrc)
      resources.setSrcDirs(mainResources)
    }
    named("test") {
      kotlin.setSrcDirs(testSrc)
      resources.setSrcDirs(testResources)
    }
  }
}

dependencies {

  api(kotlin("stdlib"))

  testImplementation(kotest("core"))
  testImplementation(kotest("runner-junit5"))
  testImplementation(mockk)
}

tasks {

  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "13" // hardcoded until kotlin can cope with 14
  }

  named<Test>("test") {
    environment("BUILD_SYSTEM", "GRADLE")
    useJUnitPlatform()
  }

  register<DownloadDependenciesTask>("downloadDependencies")
  register<DependencyReportTask>("allDeps")

  register("version") {
    doLast {
      println(project.property("version"))
    }
  }

  register<WriteProperties>("prepareRelease") {
    val newVersion = Version.valueOf(version.toString()).normalVersion
    outputFile = file("gradle.properties")
    properties(outputFile.readProperties())
    property("version", newVersion)
  }

  register<WriteProperties>("prepareNextPatchVersion") {
    val newVersion = Version.valueOf(version.toString())
      .incrementPatchVersion()
      .setPreReleaseVersion("SNAPSHOT")
    outputFile = file("gradle.properties")
    properties(outputFile.readProperties())
    property("version", newVersion)
  }

  register<WriteProperties>("prepareNextMinorVersion") {
    val newVersion = Version.valueOf(version.toString())
      .incrementMinorVersion()
      .setPreReleaseVersion("SNAPSHOT")
    outputFile = file("gradle.properties")
    properties(outputFile.readProperties())
    property("version", newVersion)
  }

  register<WriteProperties>("prepareNextMajorVersion") {
    val newVersion = Version.valueOf(version.toString())
      .incrementMajorVersion()
      .setPreReleaseVersion("SNAPSHOT")
    outputFile = file("gradle.properties")
    properties(outputFile.readProperties())
    property("version", newVersion)
  }
}

idea {
  setPackagePrefix("$group.${name.toLegalPackageName()}")
}

fun String.remove(regex: Regex) = replace(regex, "")
fun String.removeLeadingNonAlphabetic() = remove("^[^a-z]*".toRegex())
fun String.removeNonAlphanumeric() = remove("[^a-z0-9]".toRegex())
fun String.toLegalPackageName() = toLowerCase().removeLeadingNonAlphabetic().removeNonAlphanumeric()

fun File.readProperties(): Map<String, Any> = inputStream().use {
  val properties = Properties()
  properties.load(it)
  properties
}.mapKeys { (key, _) -> key.toString() }
