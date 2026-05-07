#!/bin/bash
mkdir -p plug-a-thon-mdibs
sudo apt-get install zip gzip tar
zip -r "site.zip" site
cp -a site.zip plug-a-thon-mdibs
