#!/bin/sh

set -e
./halite --replay-directory replays/ --height 64 --width 64 -vvv "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar optimized"
sleep 100
./halite --replay-directory replays/ -vvv "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar optimized" "java -jar target/MyBot.jar random" "java -jar target/MyBot.jar optimized"
