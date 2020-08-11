fun String.remove(regex: Regex) = replace(regex, "")
fun String.removeLeadingNonAlphabetic() = remove("^[^a-z]*".toRegex())
fun String.removeNonAlphanumeric() = remove("[^a-z0-9]".toRegex())
fun String.toLegalPackageName() = toLowerCase().removeLeadingNonAlphabetic().removeNonAlphanumeric()
