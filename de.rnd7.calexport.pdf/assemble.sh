#!/bin/bash

npm install
electron-packager ./ --asar --all --overwrite --out ./dist/build