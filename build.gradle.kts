import com.palantir.gradle.revapi.RevapiAnalyzeTask
import org.gradle.api.JavaVersion.VERSION_14
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  base
  kotlin("jvm") version kotlinVersion
  id("org.jmailen.kotlinter") version "3.2.0"
  id("org.jetbrains.gradle.plugin.idea-ext")
  id("com.dorongold.task-tree") version "1.5"
  id("com.palantir.revapi") version "1.4.3"
  `maven-publish`
}

@Suppress("UnstableApiUsage")
val javaVersion by extra(VERSION_14)

repositories {
  jcenter()
  mavenCentral()
  mavenLocal {
    content {
      // this repository *only* contains artifacts with group "my.company"
      includeGroup(project.group.toString())
    }
  }
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
  testImplementation(kotest("runner-junit5"))
  testImplementation(kotest("assertions-core"))
  testImplementation(mockk)
}

tasks {

  compileKotlin {
    kotlinOptions.jvmTarget = "13" // hardcoded until kotlin can cope with 14
  }

  test {
    environment("BUILD_SYSTEM", "GRADLE")
    useJUnitPlatform()
  }

  register<DependencyReportTask>("allDeps")

  register("version") {
    doLast {
      println(version)
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

  revapiAnalyze {
    onlyIf {
      val run = !version.toVersion().allowsBreakingChanges()
      logger.lifecycle("Version $version means this should run: $run")
      run
    }
  }
}

idea {
  setPackagePrefix("$group.${name.toLegalPackageName()}")
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
    }
  }
}
