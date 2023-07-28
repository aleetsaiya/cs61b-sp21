package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.TreeMap;
import java.util.Locale;
import java.text.SimpleDateFormat;
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
    private String parent1;
    /** The second parent sha1 hash reference of this Commit */
    private String parent2;

    /** Map the original file name to the sha1 hashed file name */
    private TreeMap<String, String> map;

    Commit(String message) {
        this.message = message;
        this.parent1 = null;
    }

    Commit(String message, Commit parent) {
        this.message = message;
        this.date = new Date();
        this.parent1 = RepositoryHelper.hashObject(parent);
        map = parent.getFilesMap();
    }

    Commit(String message, Commit parent1, Commit parent2) {
        this.message = message;
        this.date = new Date();
        this.parent1 = RepositoryHelper.hashObject(parent1);
        this.parent2 = RepositoryHelper.hashObject(parent2);
        // TODO: How to handle the files map?
    }

    String getMessage() {
        return message;
    }

    Commit getParentCommit() {
        if (parent1 == null) {
           return null;
        }
        return Commit.fromFile(parent1);
    }

    void setMap(TreeMap<String, String> map) {
        this.map = map;
    }

    void setDate(Date date) {
        this.date = date;
    }

    String getDate() {
        // Should format the Date object to this date output pattern: "Wed Dec 31 16:00:00 1969 -0800"
        String pattern = "EEE MMM d HH:mm:ss yyyy Z";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return formatter.format(date);
    }

    /** Get commit information from the storage folder by the given commit hash */
    static Commit fromFile(String hash) {
        File COMMIT_FILE = Repository.getObject(hash);
        return readObject(COMMIT_FILE, Commit.class);
    }

    /** Save the commit into storage folder */
    void saveCommit() {
        Repository.saveObject(this);
    }

    TreeMap<String, String> getFilesMap() {
        return map;
    }


    // TODO: Complete to toString() method
}
