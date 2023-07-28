package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;

public class RepositoryHelper {
    /** Map the file name to the objects path */
    static HashMap<String, String> toObjectPath(String name) {
        HashMap<String, String> m = new HashMap<>();
        m.put("folder", name.substring(0, 2));
        m.put("file", name.substring(2));
        return m;
    }

    /** Print out the current gitlet status with a prettier format */
    static void printStatus(List<String> unstagedFiles, List<String> stagedFiles, List<String> modifiedFiles, List<String> removedFiles) {
        // Branches
        String currentBranch = Repository.getCurrentBranch();
        File[] allBranches = join(Repository.GITLET_DIR, "refs", "heads").listFiles();
        //TODO: current branch should always at the top?
        System.out.println("=== Branches ===");
        for (File branch : allBranches) {
            if (branch.getName().equals(currentBranch)) {
                System.out.print("*");
            }
            System.out.println(branch.getName());
        }
        System.out.println();
        // Files
        System.out.println("=== Staged Files ===");
        for (String f : stagedFiles) {
            System.out.println(f);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String f : removedFiles) {
            System.out.println(f);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String f : modifiedFiles) {
            System.out.println(f);
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        for (String f : unstagedFiles) {
            System.out.println(f);
        }
        System.out.println();
    }

    /** Create a new file if the file is not exist yet */
    static void createFile(File f) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("Failed to create new file");
        }
    }

    /** read the file content as given class type and hash it to string */
    static String hashFile(File f, Class c) {
        if (c.equals(String.class)) {
            return sha1(readContentsAsString(f));
        }
        return sha1(serialize(readObject(f, c)));
    }

    /** hash an object to string */
    static String hashObject(Serializable o) {
        if (o instanceof File) {
            throw new IllegalArgumentException("Not allowed File type.");
        }
        return sha1(serialize(o));
    }
}
