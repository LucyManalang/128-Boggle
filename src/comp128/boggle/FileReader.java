package comp128.boggle;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Class that reads files
 * File reader taken from https://www.w3schools.com/java/java_files_read.asp 
 */
public class FileReader {
    /**
     * Reads files and returns them as HashSets
     */
    public static HashSet<String> readFile(String fileName) {
        HashSet<String> stringSet = new HashSet<>();
        try {
            File myFile = new File(Dice.class.getResource(fileName).toURI());
            Scanner myScanner = new Scanner(myFile);
            while (myScanner.hasNext()) {
                stringSet.add(myScanner.nextLine().toUpperCase());
            }
            myScanner.close();
            return stringSet;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return stringSet;
        } catch (URISyntaxException u) {
            System.out.println("An error occurred.");
            u.printStackTrace();
            return stringSet;
        }
    }
}
