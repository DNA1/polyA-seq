all: preclean compile jar clean

preclean:
	rm -f bin/polya-1.0.jar

compile:
	javac -cp "SamTools/picard-1.92.jar:SamTools/sam-1.92.jar" \
        -d bin \
        src/*.java  

jar:
	jar -cvf bin/polya-1.0.jar -C bin polyAextract.class 

clean:
	rm -f bin/*.class

