all:
	javac  -d bin/ src/com/psu/group9/*.java

debug:
	javac -Xlint -d bin/ src/com/psu/group9/*.java

clean:
	rm bin/com/psu/group9/*.class
