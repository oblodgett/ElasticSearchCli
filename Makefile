
all: build
	cat header-template ./target/ElasticSearchCli-jar-with-dependencies.jar > escli
	chmod +x escli
	
build:
	mvn clean package

run:
	mvn exec:java -q -Dexec.mainClass="net.nilosplace.ElasticSearchCli.Main"

grammar:
	mvn antlr4:antlr4
