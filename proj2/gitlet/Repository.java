package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Sean Tsai
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");

    /** Return a file from storage */
    public static File getObject(String name) {
        return Utils.join(GITLET_DIR, name.substring(0, 2), name.substring(2));
    }

    /** Save an object to storage as the given file name */
    public static void saveObject(String name, Serializable o) {
        File f = getObject(name);
        try {
            if (f.createNewFile()) {
                Utils.writeObject(f, o);
            }
        }
        catch (IOException e) {
            System.out.println("Failed to create file into storage");
        }
    }
}
