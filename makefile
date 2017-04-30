leysö: clean compile clear run

compile: 
	javac Game.java
	javac GameBoard.java
	javac mainTest.java

run:
	java mainTest

clean: clear
	rm -f *.class

clear:
	clear
	
