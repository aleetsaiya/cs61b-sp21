package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.RepositoryHelper.hashFile;
import static gitlet.Utils.*;
import static gitlet.RepositoryHelper.createFile;

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
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        GITLET_DIR.mkdir();
        join(GITLET_DIR, "objects").mkdir();
        join(GITLET_DIR, "refs").mkdir();
        join(GITLET_DIR, "refs", "heads").mkdir();
        // Create the index file to store the state of staging area
        File INDEX = join(GITLET_DIR, "index");
        // Create HEAD, The HEAD will point to the default branch "master"
        File HEAD = join(GITLET_DIR, "HEAD");
        createFile(HEAD);
        writeContents(HEAD, "refs/heads/master");
    }

    static void status() {
        // Arrays to store each status information
        ArrayList<String> unstagedFiles = new ArrayList<>();
        ArrayList<String> stagedFiles = new ArrayList<>();
        ArrayList<String> modifyFiles = new ArrayList<>();
        ArrayList<String> removedFiles = new ArrayList<>();

        TreeMap<String, String> wd = getWorkingDirectoryState();
        TreeMap<String, String> stag = getStagingState();
        TreeMap<String, String> repos = getRepositoryState();

        Set<String> fs = new HashSet<>();
        fs.addAll(wd.keySet());
        fs.addAll(stag.keySet());
        fs.addAll(repos.keySet());

        for (String fileName : fs) {
            boolean inRepos = repos.containsKey(fileName);
            boolean inStag = stag.containsKey(fileName);
            boolean inWd = wd.containsKey(fileName);
            if (inWd) {
                // Untracked files (in WD, but not in Stage nor Repos) (ok)
                if (!inStag && !inRepos) unstagedFiles.add(fileName);
                else if (inStag && !inRepos) {
                    // Staged files (in WD and Stage, but not in Repos, Wd and Stage value should be same) (ok)
                    if (stag.get(fileName).equals(wd.get(fileName))) stagedFiles.add(fileName);
                    // Modification not staged (Staged for addition, but with different content compare to the WD) (ok)
                    else modifyFiles.add(fileName + " (modified)");
                }
                // Tracked in the current commit, changed in the working directory, but not staged
                else if (!inStag && inRepos) modifyFiles.add(fileName + " (modified)");
                // Tracked in the current commit, changed in the working directory, and have staged
                else if (inStag && inRepos && !stag.get(fileName).equals(repos.get(fileName))) stagedFiles.add(fileName);
            }
            // To handle remove file in working directory
            else {
                String stagValue = stag.get(fileName);
                // Modification not staged (Staged for addition, but deleted in the working directory)
                // Modification not staged (Not staged for removal, but tracked in the current commit and deleted from the working directory)
                if ((inStag && !stagValue.equals("DELETE")) || (!inStag && inRepos)) modifyFiles.add(fileName + " (delete)");
                // Removed files (Have staged the deleted file, but still tracked in the current commit)
                else if (inStag && stagValue.equals("DELETE")) removedFiles.add(fileName);
            }
        }
        RepositoryHelper.printStatus(unstagedFiles, stagedFiles, modifyFiles, removedFiles);
    }

    static void add(String fileName) {
        // Update staging area state
        TreeMap<String, String> stg = getStagingState();
        TreeMap<String, String> repos = getRepositoryState();
        File f = join(CWD, fileName);
        boolean inStg = stg.containsKey(fileName);
        boolean inRepos = repos.containsKey(fileName);
        // Handle stage a removed file
        if (!f.exists()) {
            // Modification not staged (Staged for addition, but deleted in the working directory)
            if (inStg && !inRepos) stg.remove(fileName);
            // Modification not staged (Not staged for removal, but tracked in the current commit and deleted from the working directory)
            // (inStg && inRepos) is also the category of "Staged for addition, but deleted in the working directory"
            else if ((inStg && inRepos) || (!inStg && inRepos)) stg.put(fileName, "DELETE");
        }
        // Handle add a new file or modify a tracked file to staging area
        else {
            String hash = RepositoryHelper.hashFile(f, String.class);
            TreeMap<String, String> repositoryState = getRepositoryState();
            if (!repositoryState.getOrDefault(fileName, "").equals(hash)) {
                saveObject(f);
                stg.put(fileName, hash);
            }
            else {
                System.out.println("Does not changed the file ... Will not stage");
            }
        }
        writeStagingState(stg);
    }

    /** Push a new commit into current branch */
    static void commit(Commit c) {
        // Update the commit file map to the current staging state
        TreeMap<String, String> stag = getStagingState();
        TreeMap<String, String> repos = getRepositoryState();
        for (String fileName : stag.keySet()) {
            String hash = stag.get(fileName);
            if (hash.equals("DELETE")) repos.remove(fileName);
            else repos.put(fileName, hash);
        }
        c.setMap(repos);
        // Save this commit to storage
        c.saveCommit();
        // Get the current branch
        File BRANCH_FILE = join(GITLET_DIR, "refs", "heads", getCurrentBranch());
        createFile(BRANCH_FILE);
        // Update the branch HEAD to the newest commit
        writeContents(BRANCH_FILE, RepositoryHelper.hashObject(c));
        // Clear the staging state
        writeStagingState(new TreeMap());
    }

    static void addFirstCommit() {
        Commit c =  new Commit("initial commit");
        c.setDate(new Date(0));
        c.setMap(new TreeMap<String, String>());
        // Save this commit to storage
        c.saveCommit();
        // Get the current branch
        File BRANCH_FILE = join(GITLET_DIR, "refs", "heads", getCurrentBranch());
        createFile(BRANCH_FILE);
        // Update the branch HEAD to the newest commit
        writeContents(BRANCH_FILE, RepositoryHelper.hashObject(c));
        // Clear the staging state
        writeStagingState(new TreeMap());
    }

    /** Unstage a file if the file is in staging area, or remove a file if the file is not in staging area but tracked in current commit */
    static void rm(String fileName) {
        File f = join(CWD, fileName);
        String hash = RepositoryHelper.hashFile(f, String.class);
        TreeMap<String, String> stag = getStagingState();
        TreeMap<String, String> repos = getRepositoryState();
        boolean inStag = stag.containsKey(fileName);
        boolean inRepos = repos.containsKey(fileName);
        System.out.println("staging: " + inStag);
        System.out.println("inRepos: " + inRepos);
        // Unstage the file if the file have staged for addition
        if (inStag && hash.equals(stag.get(fileName)) && !hash.equals(repos.get(fileName))) {
            System.out.println("First");
            stag.remove(fileName);
            writeStagingState(stag);
        }
        // Remove the file if the file haven't staged but tracked in current commit
        else if (inStag && inRepos){
            System.out.println("Second");
            f.delete();
            stag.remove(fileName);
            writeStagingState(stag);
        }
    }

    static void branch(String branchName) {
        File BRANCH_FOLDER = join(GITLET_DIR, "refs", "heads");
        File NEW_BRANCH = join(BRANCH_FOLDER, branchName);
        if (NEW_BRANCH.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        // Create a new branch file in refs/heads/{branchName} with the content of the current HEAD commit reference
        File CURRENT_BRANCH = join(BRANCH_FOLDER, getCurrentBranch());
        createFile(NEW_BRANCH);
        writeContents(NEW_BRANCH, readContentsAsString(CURRENT_BRANCH));
    }

    static enum CheckoutOptions {
        FILE,
        COMMIT,
        BRANCH
    }

    /** Return whether the given file will overwrite the current working directory file */
    static private boolean willOverwriteUntrackedFiles(String fileName, String hash) {
        File f = join(CWD, fileName);
        if (!f.exists()) return false;
        String wdHash = hashFile(f, String.class);
        return !wdHash.equals(hash);
    }

    /** Return whether the given commit will overwrite the current working directory files */
    static private boolean willOverwriteUntrackedFiles(Commit c) {
        TreeMap<String, String> wd = getWorkingDirectoryState();
        TreeMap<String, String> m = c.getFilesMap();
        for (String fileName : m.keySet()) {
            if (wd.containsKey(fileName) && !wd.get(fileName).equals(m.get(fileName)))
                return true;
        }
        return false;
    }

    // TODO: Complete checkout
    static void checkout(CheckoutOptions option, String... vals) {
        // Checkout a file to the HEAD commit version
        if (option.equals(CheckoutOptions.FILE)) {
            String fileName = vals[0];
            // Error Handling
            TreeMap<String, String> repos = getRepositoryState();
            if (vals.length != 1) {
                System.out.println("Should have only one argument");
                return;
            }
            if (!repos.containsKey(fileName)) {
                System.out.println("File does not exist in that commit");
                return;
            }
            if (willOverwriteUntrackedFiles(fileName, repos.get(fileName))) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
            // Update the WD file to the HEAD commit version
            File HEAD_VERSION = getObject(repos.get(fileName));
            File f = join(CWD, fileName);
            createFile(f);
            writeContents(f, readContentsAsString(HEAD_VERSION));
        }
        // Checkout a file to a specific commit
        else if (option.equals(CheckoutOptions.COMMIT)) {
            if (vals.length != 2)
                throw new IllegalArgumentException("Should have only two arguments");

        }
        // Checkout to a specific branch
        else if (option.equals(CheckoutOptions.BRANCH)) {
            if (vals.length != 1)
                throw new IllegalArgumentException("Should have only one arguments");
        }
        else {
            System.out.println("Invalid checkout option");
            System.exit(1);
        }
    }

    /** Display each commit backward along the commit tree until the initial commit */
    static void log() {
        // TODO: how to handle merge? when the commit have two parents
        Commit HEAD = getHeadCommit();
        Commit p = HEAD;
        while (p != null) {
            System.out.println("===");
            System.out.println("Date: " + p.getDate());
            System.out.println(p.getMessage());
            System.out.println();
            p = p.getParentCommit();
        }
    }

    /** Return the HEAD commit */
    static Commit getHeadCommit() {
        File BRANCH_FILE = join(GITLET_DIR, "refs", "heads", getCurrentBranch());
        return Commit.fromFile(readContentsAsString(BRANCH_FILE));
    }

    /** Return the current branch name */
    static String getCurrentBranch() {
        String content = readContentsAsString(join(GITLET_DIR, "HEAD"));
        return content.split("/")[2];
    }

    /** Return the current working directory state */
    private static TreeMap<String, String> getWorkingDirectoryState() {
        TreeMap<String, String> map = new TreeMap();
        File[] files = join(CWD).listFiles();
        for (File f : files) {
            if (!f.isDirectory()) {
                map.put(f.getName(), RepositoryHelper.hashFile(f, String.class));
            }
        }
        return map;
    }

    /** Return the current staging area state */
    private static TreeMap<String, String> getStagingState() {
        File INDEX = join(GITLET_DIR, "index");
        return readObject(INDEX, TreeMap.class);
    }

    /** Update staging area state */
    private static void writeStagingState(Serializable object) {
        if (object == null) {
            return;
        }
        if (!(object instanceof TreeMap)) {
            throw new IllegalArgumentException("Only accept using the datatype Map");
        }
        File INDEX = join(GITLET_DIR, "index");
        writeObject(INDEX, object);
    }

    /** Return the current state for the HEAD commit */
    private static TreeMap<String, String> getRepositoryState() {
        Commit head = getHeadCommit();
        return head.getFilesMap();
    }

    /** Return a file from storage by the given hash name */
    static File getObject(String hash) {
        Map<String, String> m = RepositoryHelper.toObjectPath(hash);
        File folder = join(GITLET_DIR, "objects", m.get("folder"));
        if (folder.exists()) {
            File f = join(folder, m.get("file"));
            return f;
        }
        return null;
    }

    /** Save an object to storage as the given file name */
    static void saveObject(Serializable o) {
        String hash;
        if (o instanceof File) {
            hash = RepositoryHelper.hashFile((File) o, String.class);
        } else {
            hash = RepositoryHelper.hashObject(o);
        }
        Map<String, String> map = RepositoryHelper.toObjectPath(hash);
        File folder = join(GITLET_DIR, "objects", map.get("folder"));
        if (!folder.exists()) {
            folder.mkdir();
        }
        File f = join(folder, map.get("file"));
        createFile(f);
        writeObject(f, o);
    }
}
