import java.util.ArrayList;
import java.util.List;

import Exception.CustomException;
import Parser.Node;
import Parser.Parser;
import Standardizer.AST;
import Standardizer.ASTFactory;
import LexicalAnalyzer.LexicalAnalyser;
import LexicalAnalyzer.Token;
import CSEMachine.CSEMachine;
import CSEMachine.CSEMachineFactory;

public class Evaluator {
    public static String evaluate(String filePath, boolean PrintAST) {
        try {
            LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(filePath);
            List<Token> tokens = LexicalAnalyser.screener(lexicalAnalyser.scan());
            // for (Token token : tokens) {
            // System.out.println("Type: " + token.type + ", Value: " + token.value);
            // }
            Parser parser = new Parser(tokens);
            parser.parse();
            ArrayList<String> stringAST = parser.convertAST_toStringAST();

            if (PrintAST) {
                System.out.println("String AST:");
                for (String string : stringAST) {
                    System.out.println(string);
                }
            }
            // System.out.println("AST created successfully!");

            ASTFactory astFactory = new ASTFactory();
            AST ast = astFactory.getAbstractSyntaxTree(stringAST);
            ast.standardize();

            CSEMachineFactory csemfac = new CSEMachineFactory(); // create cse machine factory
            CSEMachine csemachine = csemfac.getCSEMachine(ast);

            return csemachine.getAnswer();
        } catch (CustomException e) {
            System.out.println("An error occurred during lexical analysis: " + e.getMessage());
            return null;
        }
    }
}