import com.github.zafarkhaja.semver.Version
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.comparables.beLessThan

class VersionsSpec : StringSpec({

  "version precedence" {
    version(0, 0, 2) should(beLessThan(version(0, 0, 3)))
    version(0, 0, 3, "SNAPSHOT") should(beLessThan(version(0, 0, 3)))
    version(1, 0, 0, "rc1") should(beLessThan(version(1, 0, 0)))
    println(version(1, 0, 0, "rc1").compareTo(version(1, 0, 0, "SNAPSHOT")))
//    version(1, 0, 0, "rc1") should(beLessThan(version(1, 0, 0, "SNAPSHOT")))
    version(1, 0, 0, "SNAPSHOT") should(beLessThan(version(1, 0, 0, "rc1")))
//    version(1, 0, 0, "rc1.SNAPSHOT") should(beLessThan(version(1, 0, 0, "rc1")))
    version(1, 0, 0, "alpha") should(beLessThan(version(1, 0, 0, "beta")))
//    version(1, 0, 0, "rc1-SNAPSHOT") should(beLessThan(version(1, 0, 0, "rc1")))
  }

  "version precedence from semver spec" {
    val versions = listOf(
      "1.0.0-ALPHA",
      "1.0.0-ALPHA.1",
      "1.0.0-ALPHA.1.SNAPSHOT",
      "1.0.0-ALPHA.1-SNAPSHOT",
      "1.0.0-ALPHA.BETA",
      "1.0.0-ALPHA.SNAPSHOT",
      "1.0.0-ALPHA-SNAPSHOT",
      "1.0.0-BETA",
      "1.0.0-BETA.2",
      "1.0.0-BETA.11",
      "1.0.0-M.1",
      "1.0.0-M.2",
      "1.0.0-RC.1",
      "1.0.0-RC.2",
      "1.0.0-SNAPSHOT",
      "1.0.0-alpha",
      "1.0.0-alpha.1",
      "1.0.0-alpha.1.snapshot",
      "1.0.0-alpha.1-snapshot",
      "1.0.0-alpha.beta",
      "1.0.0-alpha.snapshot",
      "1.0.0-alpha-snapshot",
      "1.0.0-beta",
      "1.0.0-beta.2",
      "1.0.0-beta.11",
      "1.0.0-m.1",
      "1.0.0-m.2",
      "1.0.0-rc.1",
      "1.0.0-rc.2",
      "1.0.0-snapshot",
      "1.0.0-z",
      "1.0.0-z.z",
      "1.0.0-z.zz",
      "1.0.0-z.zzz",
      "1.0.0-zz",
      "1.0.0-zz.z",
      "1.0.0-zz.zz",
      "1.0.0-zz.zzz",
      "1.0.0-zzz",
      "1.0.0-zzz.zzz",
      "1.0.0",
    ).map { Version.valueOf(it) }

    val shuffled = versions.shuffled()
    shuffled shouldNotBe versions

    shuffled.sorted() shouldBe versions
  }
})
