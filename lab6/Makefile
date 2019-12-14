JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES =  \
	Main.AnonApp.java \
	Actors.RequestConverter.java \
	StoreResp.java \
	Messages.TestRequest.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	            $(RM) *.class
