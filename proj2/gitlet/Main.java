package gitlet;

import java.util.Date;

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
        System.out.print("args: ");
        for (int i = 0; i < args.length; i++) {
            System.out.print(args[i] + " ");
        }
        System.out.println();
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.setupPersistence();
                // Create the first commit to repository
                Commit firstCommit =  new Commit("initial commit", null, new Date(0));
                Repository.add(firstCommit);
                Repository.commit(firstCommit);
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
        }
    }
}
