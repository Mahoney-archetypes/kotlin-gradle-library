import com.github.zafarkhaja.semver.Version

fun Any.toVersion() = Version.valueOf(this.toString())!!
fun Version.normal() = this.normalVersion.toVersion()
fun Version.snapshot() = this.setPreReleaseVersion("SNAPSHOT")!!
fun Version.allowsBreakingChanges() = isPreRelease() || isNewMajorVersion()
fun Version.isPreRelease() = majorVersion == 0 && patchVersion == 0
fun Version.isNewMajorVersion() = minorVersion == 0 && patchVersion == 0
