import com.github.zafarkhaja.semver.Version
import org.gradle.api.tasks.WriteProperties
import java.io.File
import java.util.Properties

fun WriteProperties.writeVersion(newVersion: Version) {
  outputFile = this.project.file("gradle.properties")
  properties(outputFile.readProperties())
  property("version", newVersion)
}

fun File.readProperties(): Map<String, Any> = inputStream().use {
  val properties = Properties()
  properties.load(it)
  properties
}.mapKeys { (key, _) -> key.toString() }
