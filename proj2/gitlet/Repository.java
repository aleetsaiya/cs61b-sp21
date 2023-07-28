package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

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
        // Create index as an empty TreeMap
        File INDEX = join(GITLET_DIR, "index");
        writeObject(INDEX, new TreeMap<String, String>());
        createFile(INDEX);
        // Create HEAD, The HEAD will point to the default branch "master"
        File HEAD = join(GITLET_DIR, "HEAD");
        createFile(HEAD);
        writeContents(HEAD, "refs/heads/master");
    }

    static void pushFirstCommit() {
        Commit firstCommit =  new Commit("initial commit");
        firstCommit.setDate(new Date(0));
        commit(firstCommit);
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
                // Untracked files (in WD, but not in Stage nor Repos)
                if (!inStag && !inRepos) unstagedFiles.add(fileName);
                else if (inStag && !inRepos) {
                    // Staged files (in WD and Stage, but not in Repos, Wd and Stage value should be same)
                    if (stag.get(fileName).equals(wd.get(fileName))) stagedFiles.add(fileName);
                        // Modification not staged (Staged for addition, but with different content compare to the WD)
                    else modifyFiles.add(fileName + " (modified)");
                }
                else if (inStag && inRepos) {
                    // Tracked in the current commit, changed in the working directory, but not staged
                    if (!stag.get(fileName).equals(wd.get(fileName)))
                        modifyFiles.add(fileName + " (modified)");
                        // Tracked in the current commit, changed in the working directory, and have staged
                    else if (!repos.get(fileName).equals(wd.get(fileName)))
                        stagedFiles.add(fileName);
                }
            }
            // To handle remove file in working directory
            else {
                // not yet staged the remove file
                if (inStag) {
                    // Modification not staged (Staged for addition, but deleted in the working directory) -> in stag, not in wd
                    // Modification not staged (Not staged for removal, but tracked in the current commit and deleted from the working directory) -> in repos, not in stag and not in wd
                    modifyFiles.add(fileName + " (delete)");
                }
                // have staged the remove file
                else if (inRepos) {
                    // Removed files -> not in wd, not in stag and in repos
                    removedFiles.add(fileName);
                }
            }
        }
        RepositoryHelper.printStatus(stagedFiles, removedFiles, modifyFiles, unstagedFiles);
    }

    static void add(String fileName) {
        // Update staging area state
        TreeMap<String, String> stg = getStagingState();
        File f = join(CWD, fileName);
        // Handle remove a staged file
        if (!f.exists()) {
            if (stg.containsKey(fileName))
                stg.remove(fileName);
            else {
                System.out.println("File does not exist.");
                // TODO: Return or System.exit()?
                return;
            }
        }
        // Handle add a new file or modify a tracked file
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

    /** Push a new commit into current brnach */
    static void commit(Commit c) {
        // Update the commit file map to the current staging state
        c.setMap(getStagingState());
        // Save this commit to storage
        c.saveCommit();
        // Get the current branch
        File BRANCH_FILE = join(GITLET_DIR, "refs", "heads", getCurrentBranch());
        createFile(BRANCH_FILE);
        // Update the branch HEAD to the newest commit
        writeContents(BRANCH_FILE, RepositoryHelper.hashObject(c));
        // Update index to the newest file map
        writeStagingState(c.getFilesMap());
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
    static TreeMap<String, String> getWorkingDirectoryState() {
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
    static TreeMap<String, String> getStagingState() {
        File INDEX = join(GITLET_DIR, "index");
        return readObject(INDEX, TreeMap.class);
    }

    /** Update staging area state */
    static void writeStagingState(Serializable object) {
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
    static TreeMap<String, String> getRepositoryState() {
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
