# How to run the test suite:
1. Firstly, install Visual Studio Code if it is not already installed on your computer.
2. Then install the extension Maven for Java.
3. Restart (close and reopen) Visual Studio Code
4. To run the card-playing simulation, open the terminal and enter the command javac -d out src/main/java/cardgame/*.java followed by the command java -cp out cardgame.CardGame
5. To run the tests, enter the command mvn test
