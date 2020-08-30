import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CSVUtil {
    private final static Logger LOGGER = Logger.getLogger(CSVUtil.class.getName());

    public final static String IMAGE_1 = "Image1";
    public final static String IMAGE_2 = "Image2";
    public final static String SIMILAR = "Similar";
    public final static String ELAPSED = "Elapsed";

    /**
     * Gets the CSV Record of the file
     *
     * @param csvPath the absolute path of the csv file
     * @return the list of csv records
     */
    private static List<CSVRecord> getCSVRecords(String csvPath) {
        LOGGER.info("getting the csv records...");
        try (
            Reader reader = Files.newBufferedReader(Paths.get(csvPath));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(
                    IMAGE_1,
                    IMAGE_2,
                    SIMILAR,
                    ELAPSED
            ).withIgnoreHeaderCase().withTrim())
        ) {
            LOGGER.info("successfully getting the csv records...");
            return parser.getRecords();
        } catch (NoSuchFileException e) {
            LOGGER.info(e.getMessage() + " file doesn't exist");
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }

        return null;
    }

    /**
     * Gets the name of the header in csv file
     *
     * @param headerName the name of the header
     * @param csvPath the absolute path of the csv file
     * @return the name of the header
     */
    public static String getHeaderName(String headerName, String csvPath) {
        List<CSVRecord> csvRecords = getCSVRecords(csvPath);
        if (csvRecords == null) {
            LOGGER.warning("cannot get the csv records");
            return "cannot get the csv records";
        }

        LOGGER.info("getting the header name...");
        switch (headerName) {
            case IMAGE_1:
                return csvRecords.get(0).get(IMAGE_1);
            case IMAGE_2:
                return csvRecords.get(0).get(IMAGE_2);
            case SIMILAR:
                if (csvRecords.get(0).size() > 2)
                    return csvRecords.get(0).get(SIMILAR);
                else
                    return "There hasn't been any similar scores calculated.";
            case ELAPSED:
                if (csvRecords.get(0).size() > 2)
                    return csvRecords.get(0).get(ELAPSED);
                else
                    return "There hasn't been any elapsed scores calculated.";
        }
        return null;
    }

    /**
     * Gets the path of the two images
     *
     * @param row the row number of the pairs
     * @param csvPath the absolute path of the csv file
     * @return the path of the pairs of the image
     */
    public static List<String> getImagePair(int row, String csvPath) {
        List<CSVRecord> csvRecords = getCSVRecords(csvPath);
        if (row > csvRecords.size()) {
            LOGGER.warning("the row number exceeded the row in csv file");
            return null;
        }

        List<String> imagePair = new ArrayList<>();

        imagePair.add(csvRecords.get(row).get(IMAGE_1));
        imagePair.add(csvRecords.get(row).get(IMAGE_2));

        LOGGER.info("the pair of the images are: (" + imagePair.get(0) + " " + imagePair.get(1) + ")");
        return imagePair;
    }

    /**
     * Gets the similarity score of the pair of the images
     *
     * @param row the row number of the pairs
     * @param csvPath the absolute path of the csv file
     * @return the similarity score of the specified row
     */
    public static String getSimilar(int row, String csvPath) {
        List<CSVRecord> csvRecords = getCSVRecords(csvPath);
        if (row > csvRecords.size()) {
            LOGGER.warning("the row number exceeded the row in csv file");
            return null;
        }
        if (csvRecords.get(row).size() <= 2) {
            LOGGER.warning("the similarity scores are not added yet");
        }
        String similar = csvRecords.get(row).get(SIMILAR);
        return similar;
    }

    /**
     * Gets the elapsed time of the pair of the images
     *
     * @param row the row number of the pairs
     * @param csvPath the absolute path of the csv file
     * @return the elapsed time of the specified row
     */
    public static String getElapsed(int row, String csvPath) {
        List<CSVRecord> csvRecords = getCSVRecords(csvPath);
        if (row > csvRecords.size()) {
            LOGGER.warning("the row number exceeded the row in csv file");
            return null;
        }
        if (csvRecords.get(row).size() <= 3) {
            LOGGER.warning("the elapsed time are not added yet");
        }
        String elapsed = csvRecords.get(row).get(ELAPSED);
        return elapsed;
    }

    /**
     * write similarity and elapsed time to the csv file, if scores are
     * not calculated, write -1.0 there.
     *
     * @param row the row that the content will be positioned
     * @param similar the similarity points of the images
     * @param elapsed the processing time of the image.
     * @param csvPath the path of the csv file
     */
    private static void writeCSVRecords(int row, String similar, String elapsed, String csvPath) {
        LOGGER.info("getting the csv records...");
        try (
                BufferedWriter writer = Files.newBufferedWriter(
                        Paths.get(csvPath),
                        StandardOpenOption.CREATE);

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader(IMAGE_1, IMAGE_2, SIMILAR, ELAPSED)
                        .withIgnoreHeaderCase()
                        .withTrim())
        ) {
            LOGGER.info("successfully getting the csv records...");
            LOGGER.info("writing the content to the csv records...");
            // get the existing records
            List<CSVRecord> csvRecords = getCSVRecords(csvPath);
            // writing the records exclusively
            for(int i = 1; i < csvRecords.size(); i++) {
                CSVRecord currentRow = csvRecords.get(row);
                CSVRecord otherRow = csvRecords.get(i);
                int size = csvRecords.get(i).size();
                if (i == row) {
                    csvPrinter.printRecord(currentRow.get(0), currentRow.get(1), similar, elapsed);
                } else {
                    csvPrinter.printRecord(csvRecords.get(i));
                }

            }
            csvPrinter.flush();
        } catch (IOException e) {
            LOGGER.warning("cannot write to the csv file: \n" + e.getMessage());
        }
    }

    /**
     * Add the similarity score to the csv file
     *
     * @param csvPath the absolute path of the csv file
     */
    public static void addRecords(int row, String similar, String elapsed, String csvPath) {
        List<CSVRecord> csvRecordList = getCSVRecords(csvPath);
        LOGGER.info("current row being added: " + row);
        writeCSVRecords(row, similar, elapsed, csvPath);
    }
}
