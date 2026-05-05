#!/bin/bash
mkdir plug-a-thon-mdibs
cp -a build/mdib/build/generated_mdibs/. plug-a-thon-mdibs/
cp -a resources/test-sequence.json plug-a-thon-mdibs/

mkdir plug-a-thon-mdibs/site
cp -a site/. plug-a-thon-mdibs/site/

