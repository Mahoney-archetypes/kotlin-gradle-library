updateVersion() {
  local gradleNextVersionCommand=$1
  ./gradlew -q "$gradleNextVersionCommand"
  local devVersion && devVersion=$(./gradlew -q version)
  git commit -am "Prepared new development version $devVersion"
}

assertOn() {
  local requiredBranch=$1
  local currentBranch && currentBranch=$(git rev-parse --abbrev-ref HEAD)

  if [ "$currentBranch" != "$requiredBranch" ]; then
    >&2 echo "Required to on branch $requiredBranch; currently on $currentBranch"
    exit 1
  fi
}
