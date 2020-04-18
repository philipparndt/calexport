#!/bin/bash

SCRIPTDIR="$(cd "$(dirname "$0")" && pwd)"

echo "Building rnd7/calexport"
cd $SCRIPTDIR
docker build -t rnd7/calexport .