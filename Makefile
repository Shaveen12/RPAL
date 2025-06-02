BIN = bin
SRC = Main.java Exception/CustomException.java LexicalAnalyzer/LexicalAnalyser.java LexicalAnalyzer/Token.java LexicalAnalyzer/TokenEnum.java Parser/Parser.java Parser/Node.java Parser/NodeEnum.java

all:
	mkdir -p $(BIN)
	javac -d $(BIN) $(SRC)

run:
	java -cp $(BIN) Main test.txt

clean:
	rm -rf $(BIN)