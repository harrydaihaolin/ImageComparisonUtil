import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVTest {

    private static final String SAMPLE_CSV_FILE_PATH = "./src/main/resources/image.csv";
    private static final String WRONG_SAMPLE_CSV_FILE_PATH = "./src/main/resources/image.cs";
    private static final int ROW = 5;

    @Test
    public void imageNameTest() {
        String actualCase = CSVUtil.getHeaderName(CSVUtil.IMAGE_2, SAMPLE_CSV_FILE_PATH);
        String expectedCase = "Image2";
        Assert.assertEquals(actualCase, expectedCase);
    }

    @Test
    public void imageNameViolationTest() {
        String actualCase = CSVUtil.getHeaderName(CSVUtil.IMAGE_2, WRONG_SAMPLE_CSV_FILE_PATH);
        String expectedCase = "cannot get the csv records";
        Assert.assertEquals(actualCase, expectedCase);
    }

    @Test
    public void similarityUtilTest() {
        double scores = SimilarityUtil.compareImages(1, SAMPLE_CSV_FILE_PATH);
        Assert.assertTrue(Double.isFinite(scores));
    }

    @Test
    public void afterAddedTest() {
        for(int i = 1; i < ROW; i++) {
            ElapsedUtil.start();
            double scores = SimilarityUtil.compareImages(i, SAMPLE_CSV_FILE_PATH);
            ElapsedUtil.end();
            CSVUtil.addRecords(i, String.format("%.2f", scores), String.valueOf(ElapsedUtil.getElapsedTime()), SAMPLE_CSV_FILE_PATH);
            String similar = CSVUtil.getSimilar(i, SAMPLE_CSV_FILE_PATH);
            String elapsed = CSVUtil.getElapsed(i, SAMPLE_CSV_FILE_PATH);
            Assert.assertFalse(similar.isEmpty());
            Assert.assertFalse(elapsed.isEmpty());
        }
    }

}
