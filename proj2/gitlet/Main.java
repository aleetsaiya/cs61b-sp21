package gitlet;
import gitlet.Repository.CheckoutOptions;

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
        String option = args[0];
        switch(option) {
            case "init":
                Repository.setupPersistence();
                Repository.addFirstCommit();
                break;
            case "add":
                if (args.length == 2) Repository.add(args[1]);
                else System.out.println("Should have onlyj two arguments");
                break;
            case "commit":
                if (args.length == 2) Repository.commit(new Commit(args[1], Repository.getHeadCommit()));
                else System.out.println("Should have only two arguments");
                break;
            case "status":
                Repository.status();
                break;
            case "log":
                Repository.log();
                break;
            case "rm":
                if (args.length == 2) Repository.rm(args[1]);
                else System.out.println("Should at only two arguments");
                break;
            case "branch":
                if (args.length == 2) Repository.branch(args[1]);
                else System.out.println("Should at only two arguments");
                break;
            case "checkout":
                if (args.length == 2) Repository.checkout(CheckoutOptions.BRANCH, args[1]);
                else if (args.length == 3) Repository.checkout(CheckoutOptions.FILE, args[2]);
                else if (args.length == 4) Repository.checkout(CheckoutOptions.COMMIT, args[1], args[3]);
                else System.out.println("Check the checkout usage to use this script");
                break;
        }
    }
}
