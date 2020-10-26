plugins {
  kotlin("jvm") version "1.4.10"
  id("org.jmailen.kotlinter") version "3.2.0"
}

repositories {
  jcenter()
  mavenCentral()
  gradlePluginPortal()
}

sourceSets {
  main {
    java.setSrcDirs(setOf("src"))
  }
  test {
    java.setSrcDirs(setOf("tests"))
  }
}

kotlin {
  sourceSets {
    main {
      kotlin.setSrcDirs(setOf("src"))
    }
    test {
      kotlin.setSrcDirs(setOf("tests"))
    }
  }
}

dependencies {
  implementation("gradle.plugin.org.jetbrains.gradle.plugin.idea-ext:gradle-idea-ext:0.7")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.6")
  implementation("com.github.zafarkhaja:java-semver:0.9.0")
  runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.6")

  testImplementation("io.kotest:kotest-runner-junit5:4.3.0")
  testImplementation("io.kotest:kotest-assertions-core:4.3.0")
}

tasks.test {
  environment("BUILD_SYSTEM", "GRADLE")
  useJUnitPlatform()
}
