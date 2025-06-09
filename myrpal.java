public class myrpal {

    public static void main(String[] args) {
        String fn;
        boolean PrintAST = false;

        if (args.length == 0) {
            fn = "test.txt";
        } else if (args.length == 2) {
            fn = args[1];
            if (args[0].equalsIgnoreCase("-ast")) {
                PrintAST = true;
            } else {
                System.out.println("Invalid Arguments Passing!");
                return;
            }
        } else if (args.length == 1) {
            fn = args[0];
        } else {
            System.out.println("Invalid Arguments Passing!");
            return;
        }

        System.out.println(Evaluator.evaluate(fn, PrintAST));
    }
}