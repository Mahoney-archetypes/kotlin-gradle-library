#!/usr/bin/env bash

set -euo pipefail

release() {

  local currentBranch
  currentBranch=$(git rev-parse --abbrev-ref HEAD)

  ./gradlew prepareRelease

  local releaseVersion
  releaseVersion=$(./gradlew -q version)

  git commit -am "Release version $releaseVersion"
  git tag "$releaseVersion"

  if [[ $releaseVersion == *.0 ]]; then
    local minorBranch
    minorBranch=$(echo "$releaseVersion" | sed s/\\./_/g | sed s/0\$/x/)
    git checkout -b "$minorBranch"
    ./gradlew prepareNextPatchVersion
    local devVersion
    devVersion=$(./gradlew -q version)
    git commit -am "Prepared new development version $devVersion"
  else
    git checkout "$currentBranch"
    ./gradlew prepareNextMinorVersion
    local devVersion
    devVersion=$(./gradlew -q version)
    git commit -am "Prepared new development version $devVersion"
  fi
}

main() {
  if ./gradlew build; then
    release
  fi
}

main
