import com.github.zafarkhaja.semver.Version

fun Any.toVersion() = Version.valueOf(this.toString())!!
fun Version.normal() = this.normalVersion.toVersion()
fun Version.snapshot() = this.setPreReleaseVersion("SNAPSHOT")!!
