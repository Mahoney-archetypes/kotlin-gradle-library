#!/usr/bin/env bash

set -euo pipefail

release() {
  ./gradlew prepare-release
  local version
  version=$(./gradlew -q version)
  git commit -am "Release version $version"
  git tag "$version"
}

main() {
  if ./gradlew build; then
    release
  fi
}

main
