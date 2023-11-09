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

#DIR=$(cd "$(dirname $0)" && pwd)
DIR="workspace/xroad-catalog-collector/packages/xroad-catalog-collector/redhat"
cd $DIR
#ROOT=${DIR}/xroad-catalog-collector/redhat
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
    -${CMD} SPECS/xroad-catalog-collector.spec



