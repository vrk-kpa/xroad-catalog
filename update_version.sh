#!/bin/bash

usage() {
  echo "Usage: update_version.sh -v new_version (e.g. 1.0.3)"
}

exit_abnormal() {
  usage
  exit 1
}

update_version() {
  propertiesKey="xroad-catalog.app-version"
  buildRpmKey="app_version"
  buildGradleKey="project_version"
  newvalue=$1
  sed -i "s/^[#]*\s*$propertiesKey=.*/$propertiesKey=$newvalue/" xroad-catalog-lister/src/main/resources/version.properties
  sed -i "s/^[#]*\s*$buildRpmKey=.*/$buildRpmKey=$newvalue/" xroad-catalog-lister/build_rpm.sh
  sed -i "s/^[#]*\s*$buildRpmKey=.*/$buildRpmKey=$newvalue/" xroad-catalog-collector/build_rpm.sh
  sed -i "s/^[#]*\s*$buildGradleKey=.*/    $buildGradleKey=\'$newvalue\'/" build.gradle
}

while getopts ":v:" options; do
  case "${options}" in
    v )
      VERSION=${OPTARG}
      ;;
    \? )
        exit_abnormal
      ;;
  esac
done


if [[ $VERSION == "" ]]; then
    exit_abnormal
fi

update_version "$VERSION"
