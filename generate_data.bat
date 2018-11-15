:loop
halite.exe --replay-directory replays/ --no-timeout "java -jar target/MyBot.jar hand-tuned" "java -jar target/MyBot.jar random"
goto loop