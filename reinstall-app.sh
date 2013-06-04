#!/bin/bash

APP_DIR=~/CytoscapeConfiguration/3/apps/installed

pushd $APP_DIR
  rm wordcloud-*.jar
popd

mvn clean install
mv target/wordcloud-*.jar $APP_DIR
