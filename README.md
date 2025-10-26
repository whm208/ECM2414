# How to run the test suite:
1. Firstly, ensure the following applications are installed on your computer: Visual Studio Code, and Java Development Kit version 21 or 25.
2. Open Visual Studio Code, and install the following extensions: Java, Extension Pack for Java
3. To run the card-playing simulation, open cards.jar, and run the command java -jar cards.jar
4. Alternatively, unzip cardsTest.zip, compile the source files using the command javac -d out src/main/java/cardgame/*.java and run the main class using the command java -cp out cardgame.CardGame
4. When prompted to enter the number of players, enter an integer between 1 and 13 inclusive.
5. When prompted to enter the location of a pack to load, enter cards.txt
6. To run the tests, open cardsTest.zip and enter the command mvn test