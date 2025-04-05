import java.util.ArrayList;
import java.util.List;
import LexicalAnalyzer.LexicalAnalyser;
import LexicalAnalyzer.Token;
import Exception.CustomException;
import Parser.Parser;
import Parser.Node;

public class Main {

    public static void main(String[] args) {
        String filePath;

        if (args.length < 1) {
            System.out.println("No filename given, falling back to default file: test.txt");
            filePath = "test.txt";
        } else {
            System.out.println("Filename given: " + args[0]);
            filePath = args[0];
        }

        try {
            LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(filePath);
            List<Token> tokens = LexicalAnalyser.screener(lexicalAnalyser.scan());
            for (Token token : tokens) {
                System.out.println("Type: " + token.type + ", Value: " + token.value);
            }
            Parser parser = new Parser(tokens);
            List<Node> AST = parser.parse();
            ArrayList<String> stringAST = parser.convertAST_toStringAST();
            for(String string: stringAST){ 
                System.out.println(string);
            }
        } catch (CustomException e) {
            System.out.println("An error occurred during lexical analysis: " + e.getMessage());
        }
    }
}