package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import static gitlet.Utils.*;

public class RepositoryHelper {
    /** Map the file name to the objects path */
    static HashMap<String, String> toObjectPath(String name) {
        HashMap<String, String> m = new HashMap<>();
        m.put("folder", name.substring(0, 2));
        m.put("file", name.substring(2));
        return m;
    }

    /** Create a new file if the file is not exist yet */
    static void createFile(File f) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("Failed to create new file");
        }
    }

    /** read the file content as the given class and hash it */
    static String hashFile(File f, Class c) {
        if (c.equals(String.class)) {
            return sha1(readContentsAsString(f));
        }
        return sha1(serialize(readObject(f, c)));
    }

    static String hashObject(Serializable o) {
        return sha1(serialize(o));
    }
}
