import java.util.List;
import LexicalAnalyzer.LexicalAnalyser;
import LexicalAnalyzer.Token;
import Exception.CustomException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the file path as an argument.");
            return;
        }

        String filePath = args[0];
        try {
            LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(filePath);
            List<Token> tokens = lexicalAnalyser.scan();
            for (Token token : tokens) {
                System.out.println("Type: " + token.type + ", Value: " + token.value);
            }
        } catch (CustomException e) {
            System.out.println("An error occurred during lexical analysis: " + e.getMessage());
        }
    }
}