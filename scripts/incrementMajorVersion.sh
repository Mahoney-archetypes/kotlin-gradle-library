#!/usr/bin/env bash

set -euo pipefail

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
  local currentVersion
  currentVersion=$(./gradlew -q version)

  checkoutNewMajorBranchFor "$currentVersion"
  git checkout trunk
  updateVersion prepareNextMajorVersion
}

main
