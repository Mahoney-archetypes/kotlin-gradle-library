import org.gradle.api.JavaVersion.VERSION_14
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

val mainSrc = setOf("main/code")
val mainResources = setOf("main/resources")
val testSrc = setOf("test/code")
val testResources = setOf("test/resources")

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
}

idea {
  setPackagePrefix("$group.${name.toLegalPackageName()}")
}

fun String.remove(regex: Regex) = replace(regex, "")
fun String.removeLeadingNonAlphabetic() = remove("^[^a-z]*".toRegex())
fun String.removeNonAlphanumeric() = remove("[^a-z0-9]".toRegex())
fun String.toLegalPackageName() = toLowerCase().removeLeadingNonAlphabetic().removeNonAlphanumeric()
