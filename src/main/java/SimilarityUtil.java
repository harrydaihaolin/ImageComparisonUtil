import org.sikuli.script.Finder;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class SimilarityUtil {
    private final static Logger LOGGER = Logger.getLogger(SimilarityUtil.class.getName());

    /**
     * Compare the images
     *
     * @param row the row number of the pairs
     * @param csvPath the absolute path of the csv file
     * @return
     */
    public static double compareImages(int row, String csvPath) {
        List<String> pairOfImage = CSVUtil.getImagePair(row, csvPath);
        LOGGER.info("comparing two images...");
        if (pairOfImage == null) {
            LOGGER.warning("cannot find the pair of images");
            return -1.0;
        }
        return sikuliComparison(pairOfImage.get(0), pairOfImage.get(1));
    }

    /**
     * Compare the visual appearance of the image1 with image2, if matches are found,
     * it returns a similarity float digit ranges from 0.0 to 1.0. the matches are the same when
     * it's 0.0, completely different when it's 1.0. If no matches found,
     * compare image2 with image1, then if no match found again, it returns 1.0 to
     * indicate that the images are "not similar". If there is error in reading the images,
     * we return -1.0 as the result.
     *
     * @param image1 the 1st image
     * @param image2 the 2nd image
     * @return the similarity points of the 2 images
     */
    private static double sikuliComparison(String image1, String image2) {
        Pattern pattern = new Pattern(image1);
        Finder finder = null;
        try {
            finder = new Finder(image2);
        } catch (IOException e) {
            LOGGER.warning("Sikuli cannot find the image: " + e.getMessage());
            return -1.0;
        }
        finder.find(pattern);
        LOGGER.info("Comparing Image A with B...");
        if (finder.hasNext()) {
            Match match = finder.next();
            LOGGER.info("Match found with " + (1.0 - match.getScore()));
            return 1.0 - match.getScore();
        } else {
            LOGGER.info("No match found.. ");
            LOGGER.info("Comparing Image B with A...");
            pattern = new Pattern(image2);
            try {
                finder = new Finder(image1);
            } catch (IOException e) {
                LOGGER.warning("Sikuli cannot find the image: " + e.getMessage());
                return -1.0;
            }
            finder.find(pattern);
            if (finder.hasNext()) {
                Match match = finder.next();
                LOGGER.info("Match found with " + (1.0 - match.getScore()));
                return 1.0 - match.getScore();
            } else {
                LOGGER.info("No match found..");
                return 1.0;
            }
        }
    }
}
