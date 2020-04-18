#!/bin/bash

SCRIPTDIR="$(cd "$(dirname "$0")" && pwd)"

echo "Building pharndt/calexport"
cd $SCRIPTDIR
docker build -t pharndt/calexport .