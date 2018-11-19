#!/bin/sh

while true;
do
./halite --no-replay --no-timeout --no-logs "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar random";
sleep 1;
./halite --no-replay --no-timeout --no-logs "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar random";
done