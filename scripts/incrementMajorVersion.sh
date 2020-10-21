#!/usr/bin/env bash

set -euo pipefail

. releaseFunctions.sh

checkoutNewMajorBranchFor() {
  local releaseVersion=$1
  local majorVersion="${releaseVersion%%.*}"
  git checkout -b "$majorVersion.x"
}

main() {

  local primaryBranch="main"

  assertOn "$primaryBranch"

  local currentVersion && currentVersion=$(./gradlew -q version)

  checkoutNewMajorBranchFor "$currentVersion"
  git checkout "$primaryBranch"
  updateVersion prepareNextMajorVersion
}

main
