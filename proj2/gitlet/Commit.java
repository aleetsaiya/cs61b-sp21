package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *  Limitations:
 *      1. Incorporating tress into commits and not dealing with subdirectories. (only store "flat" directory of plain files for each repository)
 *      2. Can only merge two parents
 *      3. Only have the following metadata:
 *          - timestamp
 *          - log message
 *  @author Sean Tsai
 */

public class Commit implements Serializable {
    /** The message of this Commit. */
    private String message;
    /** The creating date of this Commit */
    private Date date;
    /** The first and default parent sha1 hash reference of this Commit */
    private Commit parent1;
    /** The second parent sha1 hash reference of this Commit */
    private Commit parent2;
    /** Map the original file name to the blob file which name has been hashed by sha1 hash */
    /** e.g {"wug1.txt":  } */
    private Map<String, File> map;

    Commit(String message, Commit parent) {
        this.message = message;
        this.date = new Date();
        this.parent1 = parent;
        map = new TreeMap<>(parent1.getFilesMap());
    }

    Commit(String message, Commit parent1, Commit parent2) {
        this.message = message;
        this.date = new Date();
        this.parent1 = parent1;
        this.parent2 = parent2;
        // TODO: How to handle the files map?
    }

    Commit(String message, Commit parent, Date date) {
        this.message = message;
        this.parent1 = parent;
        this.date = date;
        // TODO: How to handle the file map?
    }

    String hash() {
        return sha1(serialize(this));
    }

    /** Get commit information from the storage folder by the given commit hash */
    static Commit fromFile(String hash) {
        File COMMIT_FILE = Repository.getObject(hash);
        return readObject(COMMIT_FILE, Commit.class);
    }

    /** Save the commit into storage folder */
    void saveCommit() {
        Repository.saveObject(hash(), this);
    }

    Map<String, File> getFilesMap() {
        return map;
    }


    // TODO: Complete to toString() method
}
