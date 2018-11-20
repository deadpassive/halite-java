:loop
halite.exe --no-replay --no-timeout --no-logs "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar random"
sleep 1;
halite.exe --no-replay --no-timeout --no-logs "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar random"
goto loop