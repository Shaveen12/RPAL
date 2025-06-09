BIN = bin
SRC = myrpal.java Evaluator.java Exception/CustomException.java LexicalAnalyzer/LexicalAnalyser.java LexicalAnalyzer/Token.java LexicalAnalyzer/TokenEnum.java Parser/Parser.java Parser/Node.java Parser/NodeEnum.java 

all:
	mkdir -p $(BIN)
	javac -d $(BIN) $(SRC)

run: all
	java -cp $(BIN) myrpal $(file)

# Allow running with: make run file=test1.txt

clean:
	rm -rf $(BIN)