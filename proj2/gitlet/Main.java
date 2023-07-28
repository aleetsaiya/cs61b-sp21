package gitlet;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Sean Tsai
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: Specific error msg
        if (args.length == 0) {
            System.out.println("no args");
            System.exit(0);
        }

        // TODO: Remove the temporary print out
        System.out.print("args: ");
        for (int i = 0; i < args.length; i++) {
            System.out.print(args[i] + " ");
        }
        System.out.println();

        String option = args[0];
        switch(option) {
            case "init":
                Repository.setupPersistence();
                Repository.addFirstCommit();
                break;
            case "add":
                if (args.length != 2) {
                    // TODO: any specific error message?
                    System.out.println("Should have at least two arguments");
                    System.exit(1);
                }
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length != 2) {
                    // TODO: any specific error message?
                    System.out.println("Should have at least two arguments");
                    System.exit(1);
                }
                String msg = args[1];
                Commit c = new Commit(msg, Repository.getHeadCommit());
                Repository.commit(c);
                break;
            case "status":
                Repository.status();
                break;
            case "log":
                Repository.log();
                break;
            case "rm":
                if (args.length != 2) {
                    // TODO: any specific error message?
                    System.out.println("Should at least two arguments");
                    System.exit(1);
                }
                Repository.rm(args[1]);
                break;
            case "branch":
                if (args.length != 2) {
                    System.out.println("Should at least two arguments");
                    System.exit(1);
                }
                Repository.branch(args[1]);
                break;
        }
    }
}
