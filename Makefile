#makefile

JFLAGS = -cp src/ -d bin/
para = -jar myclasses
JC = javac
src=$(wildcard src/*.java)
bin=$(wildcard bin/*.class)
img=$(wildcard image/*.png)
all: $(src)
	mkdir bin
	$(JC) $(JFLAGS) $(src)
	jar cvf myclasses.jar $(bin) $(img)