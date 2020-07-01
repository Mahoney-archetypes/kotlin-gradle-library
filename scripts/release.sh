#!/usr/bin/env bash

set -euo pipefail

release() {
  ./gradlew prepareRelease
  local releaseVersion
  releaseVersion=$(./gradlew -q version)
  git commit -am "Release version $releaseVersion"
  git tag "$releaseVersion"
  git checkout trunk
  ./gradlew prepareNextDevelopmentVersion
  local devVersion
  devVersion=$(./gradlew -q version)
  git commit -am "Prepared new development version $devVersion"
}

main() {
  if ./gradlew build; then
    release
  fi
}

main
