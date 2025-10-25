# How to run the test suite:
1. Firstly, install Visual Studio Code if it is not already installed on your computer.
2. Open Visual Studio Code, and install the following extensions: Java, Extension Pack for Java
3. Go to File -> Open File -> cardsTest.zip
4. To run the card-playing simulation, open the terminal and enter the command javac -d out src/main/java/cardgame/*.java followed by the command java -cp out cardgame.CardGame
5. To run the tests, enter the command mvn test
