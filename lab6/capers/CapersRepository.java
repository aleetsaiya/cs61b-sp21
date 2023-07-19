package capers;

import java.io.File;
import java.io.IOException;


/** A repository for Capers 
 * @author Sean Tsai
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers");

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence(){
        // Create hidden ".capers" folder if the folder does not exist
        if (!CAPERS_FOLDER.exists()) {
            CAPERS_FOLDER.mkdir();
        }
        // Create dogs folder to back up dogs data if the folder does not exist
        File DOGS_FOLDER = Utils.join(CAPERS_FOLDER, "dogs");
        if (!DOGS_FOLDER.exists()) {
            DOGS_FOLDER.mkdir();
        }
        // Create story to back up story data if the file does not exit
        File STORY = Utils.join(CAPERS_FOLDER, "story");
        try {
            STORY.createNewFile();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        File STORY = Utils.join(CAPERS_FOLDER, "story");
        if (!STORY.exists()) {
            System.out.println("Error occurred when writing text to story.");
            return;
        }
        String origin = Utils.readContentsAsString(STORY);
        Utils.writeContents(STORY, origin + text + "\n");
        System.out.println(Utils.readContentsAsString(STORY));
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        Dog d = new Dog(name, breed, age);
        d.saveDog();
        System.out.println(d);
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        Dog d = Dog.fromFile(name);
        d.haveBirthday();
    }
}
