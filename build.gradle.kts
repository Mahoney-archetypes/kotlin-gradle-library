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

  testImplementation(kotest("runner-junit5-jvm"))
  testImplementation(kotest("runner-console-jvm"))
  testImplementation(kotest("assertions-core-jvm"))
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

  register<DependencyReportTask>("allDeps")

  register("version") {
    doLast {
      println(project.property("version"))
    }
  }

  register<WriteProperties>("prepareRelease") {
    writeVersion(version.toVersion().normal())
  }

  register<WriteProperties>("prepareNextPatchVersion") {
    writeVersion(
      version.toVersion()
        .incrementPatchVersion()
        .snapshot()
    )
  }

  register<WriteProperties>("prepareNextMinorVersion") {
    writeVersion(
      version.toVersion()
        .incrementMinorVersion()
        .snapshot()
    )
  }

  register<WriteProperties>("prepareNextMajorVersion") {
    writeVersion(
      version.toVersion()
        .incrementMajorVersion()
        .snapshot()
    )
  }
}

idea {
  setPackagePrefix("$group.${name.toLegalPackageName()}")
}
