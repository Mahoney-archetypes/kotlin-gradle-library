import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.gradle.ext.PackagePrefixContainer

val IdeaModel.packagePrefix: PackagePrefixContainer
  get() {
    val settings = (module as ExtensionAware).extensions.getByName("settings") as ExtensionAware
    return settings.extensions.getByName("packagePrefix") as PackagePrefixContainer
  }

val Project.sourceSets: SourceSetContainer get() =
  (this as ExtensionAware).extensions.getByName("sourceSets") as SourceSetContainer

fun IdeaModel.setPackagePrefix(prefix: String) {

  val gradleProject = module?.project ?: project?.project

  val srcDirs = gradleProject!!.sourceSets
    .flatMap { it.java.srcDirs }
    .map { it.relativeTo(gradleProject.projectDir).path }

  srcDirs.forEach {
    packagePrefix[it] = prefix
  }
}
