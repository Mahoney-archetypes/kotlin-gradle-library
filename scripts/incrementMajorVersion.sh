#!/usr/bin/env bash

set -euo pipefail

assertOnTrunk() {
  local currentBranch
  currentBranch=$(git rev-parse --abbrev-ref HEAD)

  if [ "$currentBranch" != "trunk" ]; then
    >&2 echo "Can only increment major version on trunk; currently on $currentBranch"
    exit 1
  fi
}

checkoutNewMajorBranchFor() {
  local releaseVersion=$1
  local majorVersion="${releaseVersion%%.*}"
  git checkout -b "$majorVersion.x"
}

updateVersion() {
  ./gradlew -q prepareNextMajorVersion
  local devVersion
  devVersion=$(./gradlew -q version)
  git commit -am "Prepared new development version $devVersion"
}

main() {

  assertOnTrunk

  local currentVersion
  currentVersion=$(./gradlew -q version)

  checkoutNewMajorBranchFor "$currentVersion"
  git checkout trunk
  updateVersion prepareNextMajorVersion
}

main
