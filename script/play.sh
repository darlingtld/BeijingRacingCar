#!/usr/bin/env bash
#mkdir -p ~/mongodb_data/gamble_db
#mongod --dbpath ~/mongodb_data/gamble_db
base_dir=$(cd "$(dirname "$0")";pwd)
chromedriver_dir=$base_dir/../src/main/resources/chromedriver
echo $chromedriver_dir
java -jar build/libs/BeijingRacingCar-0.0.1-SNAPSHOT.jar $chromedriver_dir


