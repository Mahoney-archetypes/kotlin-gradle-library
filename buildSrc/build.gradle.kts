plugins {
  kotlin("jvm") version "1.3.72"
  id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

repositories {
  jcenter()
  mavenCentral()
  gradlePluginPortal()
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
  runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.6")
}
