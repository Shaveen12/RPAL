BIN = bin
SRC = Main.java Exception/CustomException.java LexicalAnalyzer/LexicalAnalyser.java LexicalAnalyzer/Token.java LexicalAnalyzer/TokenEnum.java

all:
	if not exist $(BIN) mkdir $(BIN)
	javac -d $(BIN) $(SRC)

run:
	java -cp $(BIN) Main

clean:
	if exist $(BIN) rd /s /q $(BIN)
