#!/bin/sh

set -e
./halite --replay-directory replays/ -vvv "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar optimized"
./halite --replay-directory replays/ -vvv "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar optimized" "java -jar target/MyBot.jar random" "java -jar target/MyBot.jar optimized"
