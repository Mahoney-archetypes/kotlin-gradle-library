#!/usr/bin/env bash

set -euo pipefail

release() {
  ./gradlew prepareRelease
  local version
  version=$(./gradlew -q version)
  git commit -am "Release version $version"
  git tag "$version"
  ./gradlew prepareNextDevelopmentVersion
}

main() {
  if ./gradlew build; then
    release
  fi
}

main
