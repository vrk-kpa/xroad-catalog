#!/bin/sh

gradle build

cd packages/xroad-catalog-lister
dpkg-buildpackage -tc -b -us -uc

