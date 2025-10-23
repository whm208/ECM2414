# How to run the test suite:
To run the card playing simulation,
open CardGame.java,
compile the files using the command javac -d out src/main/java/cardgame/*.java,
and run CardGame.java using the command java -cp out cardgame.CardGame

To run the tests, you will need to install Maven.
The easiest way to do this is by installing the Visual Studio Code extension Maven for Java.
Once Maven has been installed,
restart (close and reopen) Visual Studio Code,
and enter in the terminal the command mvn test
