package LexicalAnalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exception.CustomException;

public class LexicalAnalyser {
    private String inputFileName;
    private List<Token> tokens;

    public LexicalAnalyser(String inputFileName) {
        this.inputFileName = inputFileName;
        tokens = new ArrayList<>();
    }

    public List<Token> scan() throws CustomException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                try {
                    tokenizeLine(line);
                } catch (CustomException e) {
                    throw new CustomException(e.getMessage() + " in LINE: " + lineCount + "\nERROR in lexical_analysis.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    private void tokenizeLine(String line) throws CustomException {
        Pattern identifierPattern = Pattern.compile("[a-zA-Z]([a-zA-Z0-9_])*");
        Pattern integerPattern = Pattern.compile("[0-9]+");
        Pattern operatorPattern = Pattern.compile("[+\\-*/<>&.@/:=~|$!#%^_\\[\\]{}\"`\\?]+");
        Pattern punctuationPattern = Pattern.compile("[(),;]");
        Pattern spacesPattern = Pattern.compile("(\\s|\\t)+");
        Pattern stringPattern = Pattern.compile("'([a-zA-Z0-9+\\-*/<>&.@/:=~|$!#%^_\\[\\]{}\"`\\?(),;\\s\\\\'])*'");
        Pattern commentPattern = Pattern.compile("//.*");

        int currentIndex = 0;
        while (currentIndex < line.length()) {
            char currentChar = line.charAt(currentIndex);

            Matcher spaceMatcher = spacesPattern.matcher(line.substring(currentIndex));
            Matcher commentMatcher = commentPattern.matcher(line.substring(currentIndex));

            if (commentMatcher.lookingAt()) {
                currentIndex += commentMatcher.group().length();
                continue;
            }

            if (spaceMatcher.lookingAt()) {
                currentIndex += spaceMatcher.group().length();
                continue;
            }

            Matcher matcher;

            matcher = identifierPattern.matcher(line.substring(currentIndex));
            if (matcher.lookingAt()) {
                String identifier = matcher.group();
                List<String> keywords = List.of(
                        "let", "in", "fn", "where", "aug", "or", "not", "gr", "ge", "ls",
                        "le", "eq", "ne", "true", "false", "nil", "dummy", "within", "and", "rec"
                );
                if (keywords.contains(identifier))
                    tokens.add(new Token(TokenEnum.KEYWORD, identifier));
                else
                    tokens.add(new Token(TokenEnum.IDENTIFIER, identifier));
                currentIndex += identifier.length();
                continue;
            }

            matcher = integerPattern.matcher(line.substring(currentIndex));
            if (matcher.lookingAt()) {
                tokens.add(new Token(TokenEnum.INTEGER, matcher.group()));
                currentIndex += matcher.group().length();
                continue;
            }

            matcher = operatorPattern.matcher(line.substring(currentIndex));
            if (matcher.lookingAt()) {
                tokens.add(new Token(TokenEnum.OPERATOR, matcher.group()));
                currentIndex += matcher.group().length();
                continue;
            }

            matcher = stringPattern.matcher(line.substring(currentIndex));
            if (matcher.lookingAt()) {
                tokens.add(new Token(TokenEnum.STRING, matcher.group()));
                currentIndex += matcher.group().length();
                continue;
            }

            matcher = punctuationPattern.matcher(Character.toString(currentChar));
            if (matcher.matches()) {
                tokens.add(new Token(TokenEnum.PUNCTUATION, Character.toString(currentChar)));
                currentIndex++;
                continue;
            }

            throw new CustomException("Unable to tokenize the CHARACTER: " + currentChar + " at INDEX: " + currentIndex);
        }
    }
}
