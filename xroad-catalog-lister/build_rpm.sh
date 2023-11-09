#!/bin/sh

while getopts ":p:" options; do
  case "${options}" in
    p )
      PROFILE=${OPTARG}
      ;;
    \? )
        exit_abnormal
      ;;
  esac
done

if [ -z "$PROFILE" ]; then
  PROFILE="default";
fi

DIR="workspace/xroad-catalog-lister/packages/xroad-catalog-lister/redhat"
cd $DIR

ROOT=`pwd`
RELEASE=1
DATE=$(date --utc --date @$(git show -s --format=%ct || date +%s) +'%Y%m%d%H%M%S')
HASH=$(git show -s --format=git%h || echo 'local')
SNAPSHOT=$DATE$HASH
FILES=${1-'xroad-*.spec'}
CMD="-ba"

rm -rf ${ROOT}/RPMS/*

app_version=3.0.6

echo "Chosen catalog profile = $PROFILE"

rpmbuild \
    --define "xroad_catalog_version $app_version" \
    --define "rel $RELEASE" \
    --define "snapshot .$SNAPSHOT" \
    --define "_topdir $ROOT" \
    --define "profile $PROFILE" \
    -${CMD} SPECS/xroad-catalog-lister.spec


