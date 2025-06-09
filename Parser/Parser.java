package Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import LexicalAnalyzer.Token;
import LexicalAnalyzer.TokenEnum;

public class Parser {
    private List<Token> tokens;
    private List<Node> AST; 
    private ArrayList<String> stringAST;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        AST = new ArrayList<>();
        stringAST = new ArrayList<>();
    }

    public List<Node> parse() {
        tokens.add(new Token(TokenEnum.HEAD, ""));
        E();
        if (tokens.get(0).type.equals(TokenEnum.HEAD)) {
            
            return AST;
        } else {
            System.out.println("Parsing Unsuccessful");
            return null;
        }
    }

    public ArrayList<String> convertAST_toStringAST() {
        String dots = "";
        List<Node> stack = new ArrayList<Node>();

        while (!AST.isEmpty()) {
            if (stack.isEmpty()) {
                if (AST.get(AST.size() - 1).noOfChildren == 0) {
                    addStrings(dots, AST.remove(AST.size() - 1));
                } else {
                    Node node = AST.remove(AST.size() - 1);
                    stack.add(node);
                }
            } else {
                if (AST.get(AST.size() - 1).noOfChildren > 0) {
                    Node node = AST.remove(AST.size() - 1);
                    stack.add(node);
                    dots += ".";
                } else {
                    stack.add(AST.remove(AST.size() - 1));
                    dots += ".";
                    while (stack.get(stack.size() - 1).noOfChildren == 0) {
                        addStrings(dots, stack.remove(stack.size() - 1));
                        if (stack.isEmpty())
                            break;
                        dots = dots.substring(0, dots.length() - 1);
                        Node node = stack.remove(stack.size() - 1);
                        node.noOfChildren--;
                        stack.add(node);

                    }
                }

            }
        }

        Collections.reverse(stringAST);
        return stringAST;
    }

    void addStrings(String dots,Node node) {
        switch(node.type) {
            case identifier:
                stringAST.add(dots+"<ID:"+node.value+">");
                break;
            case integer:
                stringAST.add(dots+"<INT:"+node.value+">");
                break;
            case string: 
                stringAST.add(dots+"<STR:"+node.value+">");
                break;	
            case true_value:
                stringAST.add(dots+"<"+node.value+">");
                break;
            case false_value:
                stringAST.add(dots+"<"+node.value+">");
                break;
            case nil:
                stringAST.add(dots+"<"+node.value+">");
                break;
            case dummy:
                stringAST.add(dots+"<"+node.value+">");
                break;
            case fcn_form:
                stringAST.add(dots+"function_form");
                break;
            default :
                stringAST.add(dots+node.value);
        }		
}

    // # Expressions ############################################

    void E() {

        int n = 0;
        Token token = tokens.get(0);
        if (token.type.equals(TokenEnum.KEYWORD) && Arrays.asList("let", "fn").contains(token.value)) {
            if (token.value.equals("let")) {
                tokens.remove(0);
                D();
                if (!tokens.get(0).value.equals("in")) {
                    System.out.println("Parse error at E : 'in' Expected");
                }
                tokens.remove(0);
                E();
                AST.add(new Node(NodeEnum.let, "let", 2));

            } else {
                tokens.remove(0);
                do {
                    Vb();
                    n++;
                } while (tokens.get(0).type.equals(TokenEnum.IDENTIFIER) || tokens.get(0).value.equals("("));
                if (!tokens.get(0).value.equals(".")) {
                    System.out.println("Parse error at E : '.' Expected");
                    // return;
                }
                tokens.remove(0);
                E();
                AST.add(new Node(NodeEnum.lambda, "lambda", n + 1));
            }
        } else
            Ew();
    }

    void Ew() {
        T();
        if (tokens.get(0).value.equals("where")) {
            tokens.remove(0); // Remove where
            Dr();
            AST.add(new Node(NodeEnum.where, "where", 2));
        }

    }

    // * # Tuple Expressions
    // ######################################################################################

    void T() {
        Ta();
        int n = 1;
        while (tokens.get(0).value.equals(",")) {
            // System.out.println(tokens.get(0).value);
            tokens.remove(0); // Remove coma(,)
            Ta();
            ++n;
        }
        if (n > 1) {
            AST.add(new Node(NodeEnum.tau, "tau", n));
        }
    }

    void Ta() {
        Tc();
        while (tokens.get(0).value.equals("aug")) {
            tokens.remove(0); // Remove aug
            Tc();
            AST.add(new Node(NodeEnum.aug, "aug", 2));
        }
    }

    void Tc() {
        B();
        if (tokens.get(0).value.equals("->")) {
            tokens.remove(0);
            Tc();
            if (!tokens.get(0).value.equals("|")) {
                System.out.println("Parse error at Tc: conditional '|' expected");
            }
            tokens.remove(0);
            Tc();
            AST.add(new Node(NodeEnum.conditional, "->", 3));
        }
    }

    // * # Boolean Expressions
    // ######################################################################################

    void B() {
        Bt();
        while (tokens.get(0).value.equals("or")) {
            tokens.remove(0);
            Bt();
            AST.add(new Node(NodeEnum.op_or, "or", 2));
        }
    }

    void Bt() {
        Bs();
        while (tokens.get(0).value.equals("&")) {
            tokens.remove(0);
            Bs();
            AST.add(new Node(NodeEnum.op_and, "&", 2));
        }
    }

    void Bs() {
        if (tokens.get(0).value.equals("not")) {
            tokens.remove(0);
            Bp();
            AST.add(new Node(NodeEnum.op_not, "not", 1));
        } else
            Bp();
    }

    void Bp() {
        A();
        Token token = tokens.get(0);
        if (Arrays.asList(">", ">=", "<", "<=").contains(token.value)
                || Arrays.asList("gr", "ge", "ls", "le", "eq", "ne").contains(token.value)) {
            tokens.remove(0);
            A();
            switch (token.value) {
                case ">":
                    AST.add(new Node(NodeEnum.op_compare, "gr", 2));
                    break;
                case ">=":
                    AST.add(new Node(NodeEnum.op_compare, "ge", 2));
                    break;
                case "<":
                    AST.add(new Node(NodeEnum.op_compare, "ls", 2));
                    break;
                case "<=":
                    AST.add(new Node(NodeEnum.op_compare, "le", 2));
                    break;
                default:
                    AST.add(new Node(NodeEnum.op_compare, token.value, 2));
                    break;
            }
        }
    }

    // Arithmetic Expressions
    // ##########################################################################################

    void A() {
        if (tokens.get(0).value.equals("+")) {
            tokens.remove(0);
            At();
        } else if (tokens.get(0).value.equals("-")) {
            tokens.remove(0);
            At();
            AST.add(new Node(NodeEnum.op_neg, "neg", 1));
        } else {
            At();
        }
        while (Arrays.asList("+", "-").contains(tokens.get(0).value)) {
            Token currentToken = tokens.get(0);
            tokens.remove(0);
            At();
            if (currentToken.value.equals("+"))
                AST.add(new Node(NodeEnum.op_plus, "+", 2));
            else
                AST.add(new Node(NodeEnum.op_minus, "-", 2));
        }

    }

    void At() {
        Af();
        while (Arrays.asList("*", "/").contains(tokens.get(0).value)) {
            Token currentToken = tokens.get(0); // save present token
            tokens.remove(0);
            Af();
            if (currentToken.value.equals("*"))
                AST.add(new Node(NodeEnum.op_mul, "*", 2));
            else
                AST.add(new Node(NodeEnum.op_div, "/", 2));
        }
    }

    void Af() {
        Ap();
        if (tokens.get(0).value.equals("**")) {
            tokens.remove(0);
            Af();
            AST.add(new Node(NodeEnum.op_pow, "**", 2));
        }
    }

    void Ap() {
        R();
        while (tokens.get(0).value.equals("@")) {
            tokens.remove(0);

            if (!tokens.get(0).type.equals(TokenEnum.IDENTIFIER)) {
                System.out.println("Parsing error at Ap: IDENTIFIER EXPECTED");
                // return;
            }
            AST.add(new Node(NodeEnum.identifier, tokens.get(0).value, 0));
            tokens.remove(0);

            R();
            AST.add(new Node(NodeEnum.at, "@", 3));
        }
    }

    /*
     * # Rators And Rands
     * #############################################################################
     * #############
     * 
     */
    void R() {
        Rn();
        while ((Arrays.asList(TokenEnum.IDENTIFIER, TokenEnum.INTEGER, TokenEnum.STRING).contains(tokens.get(0).type))
                || (Arrays.asList("true", "false", "nil", "dummy").contains(tokens.get(0).value))
                || (tokens.get(0).value.equals("("))) {

            Rn();
            AST.add(new Node(NodeEnum.gamma, "gamma", 2));

        }
    }

    void Rn() {
        switch (tokens.get(0).type) {
            case IDENTIFIER:
                AST.add(new Node(NodeEnum.identifier, tokens.get(0).value, 0));
                tokens.remove(0);
                break;
            case INTEGER:
                AST.add(new Node(NodeEnum.integer, tokens.get(0).value, 0));
                tokens.remove(0);
                break;
            case STRING:
                AST.add(new Node(NodeEnum.string, tokens.get(0).value, 0));
                tokens.remove(0);
                break;
            case KEYWORD:
                switch (tokens.get(0).value) {
                    case "true":
                        AST.add(new Node(NodeEnum.true_value, tokens.get(0).value, 0));
                        tokens.remove(0);
                        break;
                    case "false":
                        AST.add(new Node(NodeEnum.false_value, tokens.get(0).value, 0));
                        tokens.remove(0);
                        break;
                    case "nil":
                        AST.add(new Node(NodeEnum.nil, tokens.get(0).value, 0));
                        tokens.remove(0);
                        break;
                    case "dummy":
                        AST.add(new Node(NodeEnum.dummy, tokens.get(0).value, 0));
                        tokens.remove(0);
                        break;
                    default:
                        System.out.println("Parse Error at Rn: Unexpected KEYWORD");
                        break;
                }
                break;
            case PUNCTUATION:
                if (tokens.get(0).value.equals("(")) {
                    tokens.remove(0);

                    E();

                    if (!tokens.get(0).value.equals(")")) {
                        System.out.println("Parsing error at Rn: Expected a matching ')'");
                    }
                    tokens.remove(0);
                } else
                    System.out.println("Parsing error at Rn: Unexpected PUNCTUATION");
                break;
            default:
                System.out.println("Parsing error at Rn: Expected a Rn, but got different");
                break;
        }

    }

    /*
     * # Definitions ############################################
     * 
     */
    void D() {
        Da();
        if (tokens.get(0).value.equals("within")) {
            // // System.out.println(tokens.get(0).value);
            tokens.remove(0); // Remove 'within'
            D();
            AST.add(new Node(NodeEnum.within, "within", 2));
        }
    }

    void Da() {
        Dr();
        int n = 1;
        while (tokens.get(0).value.equals("and")) {
            tokens.remove(0);
            Dr();
            n++;
        }
        if (n > 1)
            AST.add(new Node(NodeEnum.and, "and", n));
    }

    void Dr() {
        boolean isRec = false;
        if (tokens.get(0).value.equals("rec")) {
            tokens.remove(0);
            isRec = true;
        }
        Db();
        if (isRec) {
            AST.add(new Node(NodeEnum.rec, "rec", 1));
        }
    }

    void Db() {
        if (tokens.get(0).type.equals(TokenEnum.PUNCTUATION) && tokens.get(0).value.equals("(")) {
            tokens.remove(0);
            D();
            if (!tokens.get(0).value.equals(")")) {
                System.out.println("Parsing error at Db #1");
            }
            tokens.remove(0);
        } else if (tokens.get(0).type.equals(TokenEnum.IDENTIFIER)) {
            if (tokens.get(1).value.equals("(") || tokens.get(1).type.equals(TokenEnum.IDENTIFIER)) {
                AST.add(new Node(NodeEnum.identifier, tokens.get(0).value, 0));
                tokens.remove(0);

                int n = 1;
                do {
                    Vb();
                    n++;
                } while (tokens.get(0).type.equals(TokenEnum.IDENTIFIER) || tokens.get(0).value.equals("("));
                if (!tokens.get(0).value.equals("=")) {
                    System.out.println("Parsing error at Db #2");

                }
                tokens.remove(0);
                E();

                AST.add(new Node(NodeEnum.fcn_form, "fcn_form", n + 1));

            } else if (tokens.get(1).value.equals("=")) {
                AST.add(new Node(NodeEnum.identifier, tokens.get(0).value, 0));
                tokens.remove(0);
                tokens.remove(0);
                E();
                AST.add(new Node(NodeEnum.equal, "=", 2));
            } else if (tokens.get(1).value.equals(",")) {
                Vl();
                if (!tokens.get(0).value.equals("=")) {
                    System.out.println("Parsing error at Db");
                    // return;
                }
                tokens.remove(0);
                E();

                AST.add(new Node(NodeEnum.equal, "=", 2));
            }

        }

    }

    /*
     * # Variables ##############################################
     */
    void Vb() {
        if (tokens.get(0).type.equals(TokenEnum.PUNCTUATION) && tokens.get(0).value.equals("(")) {
            tokens.remove(0);
            boolean isVl = false;

            if (tokens.get(0).type.equals(TokenEnum.IDENTIFIER)) {
                Vl();
                isVl = true;
            }
            if (!tokens.get(0).value.equals(")")) {
                System.out.println("Parse error unmatch )");
                // return;
            }
            tokens.remove(0);
            if (!isVl)
                AST.add(new Node(NodeEnum.empty_params, "()", 0));

        } else if (tokens.get(0).type.equals(TokenEnum.IDENTIFIER)) {
            AST.add(new Node(NodeEnum.identifier, tokens.get(0).value, 0));
            tokens.remove(0);
        }

    }

    void Vl() {
        int n = 0;
        do {
            if (n > 0) {
                tokens.remove(0);
            }
            if (!tokens.get(0).type.equals(TokenEnum.IDENTIFIER)) {
                System.out.println("Parse error: a ID was expected )");
            }
            AST.add(new Node(NodeEnum.identifier, tokens.get(0).value, 0));
            tokens.remove(0);
            n++;
        } while (tokens.get(0).value.equals(","));
        AST.add(new Node(NodeEnum.comma, ",", n));
    }

}