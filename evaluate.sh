#!/bin/sh

set -e
./halite --replay-directory replays/ -vvv "java -jar submissions/MyBot.jar" "java -jar target/MyBot.jar"
./halite --replay-directory replays/ -vvv "java -jar submissions/MyBot.jar" "java -jar target/MyBot.jar" "java -jar submissions/MyBot.jar" "java -jar target/MyBot.jar"
