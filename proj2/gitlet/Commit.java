package gitlet;

import org.antlr.v4.runtime.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    /**
     * 1. Get the current un-encoding file
     * File currentFile = new File([currentFolder], "currentFile.java");
     *
     * 2. Using sha1 hash to hash the whole file to get the name
     * String hashedName = Utils.sha1(currentFile);
     *
     * 3. Create a new file to store the current file but the encoded version
     * File targetFile = new File(".getlet", "objects", hashedName);
     * Utils.writeObject(targetFile, currentFile);
     * targetFile.createNewFile();
     *
     * 4. Store the encoded file reference to the map
     * map["currentFile.java"] = targetFile;
     */

    /**
     *
     * @param message the commit message
     * @param parent1 the parent commit to this commit
     */
    Commit(String message, Commit parent1) {
        this.message = message;
        this.date = new Date();
        this.parent1 = parent1;
        map = new TreeMap<>(parent1.getFilesMap());
    }

    /** Get commit from the storage folder by then given commit hash */
    public static Commit fromFile(String hash) {
        File COMMIT_FILE = Repository.getObject(hash);
        return Utils.readObject(COMMIT_FILE, Commit.class);
    }

    /** Save the commit into storage folder */
    public void saveCommit() {
        Repository.saveObject(Utils.sha1(this), this);
    }

    public Map<String, File> getFilesMap() {
        return map;
    }


// TODO: Complete to toString() method

//    @Override
//    public String toString() {
//        return String.format("commit %s\n Date: %s\n %s\n", )
//    }
}
