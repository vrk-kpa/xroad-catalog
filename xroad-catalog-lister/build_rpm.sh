#!/bin/sh
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



rpmbuild \
    --define "xroad_catalog_version 0.12.4" \
    --define "rel $RELEASE" \
    --define "snapshot .$SNAPSHOT" \
    --define "_topdir $ROOT" \
    -${CMD} SPECS/xroad-catalog-lister.spec


