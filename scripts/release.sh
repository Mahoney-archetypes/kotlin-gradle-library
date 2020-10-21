#!/usr/bin/env bash

set -euo pipefail

. releaseFunctions.sh

checkoutNewMinorBranchFor() {
  local releaseVersion=$1
  local minorBranch="${releaseVersion/%0/x}"
  git checkout -b "$minorBranch"
}

release() {

  local currentBranch && currentBranch=$(git rev-parse --abbrev-ref HEAD)

  ./gradlew -q prepareRelease

  local releaseVersion && releaseVersion=$(./gradlew -q version)

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
  ./gradlew build
  release
}

main
