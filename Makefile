BIN = bin
SRC = myrpal.java Evaluator.java Exception/CustomException.java LexicalAnalyzer/LexicalAnalyser.java LexicalAnalyzer/Token.java LexicalAnalyzer/TokenEnum.java Parser/Parser.java Parser/Node.java Parser/NodeEnum.java 

all:
	javac $(SRC)

run:
	java myrpal $(file)

run-ast:
	java myrpal -ast $(file)


clean:
	find . -name "*.class" -delete