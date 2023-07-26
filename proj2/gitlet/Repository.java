package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Sean Tsai
 */
public class Repository {
    /** The current working directory. */
    static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    static final File GITLET_DIR = Utils.join(CWD, ".gitlet");

    static void setupPersistence() {
        // Create the necessary folders
        GITLET_DIR.mkdir();
        join(GITLET_DIR, "objects").mkdir();
        join(GITLET_DIR, "refs").mkdir();
        join(GITLET_DIR, "refs", "heads").mkdir();
        // Create index
        createFile(join(GITLET_DIR, "index"));
        // Create HEAD, The HEAD will point to the default branch "master"
        File HEAD = join(GITLET_DIR, "HEAD");
        createFile(HEAD);
        writeContents(HEAD, "refs/heads/master");
    }

    /** Return the current branch name */
    static private String getCurrentBranch() {
        String content = readContentsAsString(join(GITLET_DIR, "HEAD"));
        return content.split("/")[2];
    }

    /** Return the HEAD commit */
    static Commit getHeadCommit() {
        File BRANCH_FILE = join(GITLET_DIR, "refs", "heads", getCurrentBranch());
        return Commit.fromFile(readContentsAsString(BRANCH_FILE));
    }

    /** Add commits to the staging area and store these tracked commits to storage */
    static void add(Commit c) {
        c.saveCommit();
        // TODO: Update index
    }

    /** Push a new commit into current brnach */
    static void commit(Commit c) {
        // Get the current branch
        File BRANCH_FILE = join(GITLET_DIR, "refs", "heads", getCurrentBranch());
        // Update the branch HEAD to the newest commit
        createFile(BRANCH_FILE);
        writeContents(BRANCH_FILE, c.hash());
        // TODO: Update index
    }


    /** Map the file name to the objects path */
    static private Map<String, String> toObjectPath(String name) {
        Map<String, String> m = new HashMap<>();
        m.put("folder", name.substring(0, 2));
        m.put("file", name.substring(2));
        return m;
    }

    /** Return a file from storage */
    static File getObject(String name) {
        Map<String, String> m = toObjectPath(name);
        File folder = join(GITLET_DIR, "objects", m.get("folder"));
        if (folder.exists()) {
            File f = join(folder, m.get("file"));
            return f;
        }
        return null;
    }

    /** Save an object to storage as the given file name */
    static void saveObject(String name, Serializable o) {
        Map<String, String> map = toObjectPath(name);
        File folder = join(GITLET_DIR, "objects", map.get("folder"));
        if (!folder.exists()) {
            folder.mkdir();
        }
        File f = join(folder, map.get("file"));
        createFile(f);
        writeObject(f, o);
    }
}
