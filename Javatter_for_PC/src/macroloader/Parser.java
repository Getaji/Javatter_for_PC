package macroloader;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class Parser {
    public static String getPrefix(String fileName) {
        if (fileName == null)
            return null;
        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            return fileName.substring(0, point);
        }
        return fileName;
    }
}
