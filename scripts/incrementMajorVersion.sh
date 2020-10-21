#!/usr/bin/env bash

set -euo pipefail

assertOnMain() {
  local currentBranch
  currentBranch=$(git rev-parse --abbrev-ref HEAD)

  if [ "$currentBranch" != "main" ]; then
    >&2 echo "Can only increment major version on main; currently on $currentBranch"
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

  assertOnMain

  local currentVersion
  currentVersion=$(./gradlew -q version)

  checkoutNewMajorBranchFor "$currentVersion"
  git checkout main
  updateVersion prepareNextMajorVersion
}

main
