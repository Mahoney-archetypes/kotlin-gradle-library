#!/usr/bin/env bash

set -euo pipefail

checkoutNewMinorBranchFor() {
  local releaseVersion=$1
  local minorBranch
  minorBranch=$(echo "$releaseVersion" | sed s/\\./_/g | sed s/0\$/x/)
  git checkout -b "$minorBranch"
}

updateVersion() {
  local gradleNextVersionCommand=$1
  ./gradlew "$gradleNextVersionCommand"
  local devVersion
  devVersion=$(./gradlew -q version)
  git commit -am "Prepared new development version $devVersion"
}

release() {

  local currentBranch
  currentBranch=$(git rev-parse --abbrev-ref HEAD)

  ./gradlew prepareRelease

  local releaseVersion
  releaseVersion=$(./gradlew -q version)

  git commit -am "Release version $releaseVersion"
  git tag "$releaseVersion"

  if [[ $releaseVersion == *.0 ]]; then
    # First release of this minor version
    checkoutNewMinorBranchFor "$releaseVersion"
    updateVersion prepareNextPatchVersion

    git checkout "$currentBranch"
    updateVersion prepareNextMinorVersion
  else
    git checkout "$currentBranch"
    updateVersion prepareNextPatchVersion
  fi
}

main() {
  if ./gradlew build; then
    release
  fi
}

main
