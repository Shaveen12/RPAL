package LexicalAnalyzer;



public class Token {
    public TokenEnum type;
    public String value;

    public Token(TokenEnum type, String value) {
        this.type = type;
        this.value = value;
    }

}