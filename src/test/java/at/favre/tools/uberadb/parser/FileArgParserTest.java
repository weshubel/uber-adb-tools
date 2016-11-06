package at.favre.tools.uberadb.parser;

import at.favre.tools.uberadb.ui.FileArgParser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class FileArgParserTest {
    File signedFolder;
    List<File> sortedSinged;
    private String extFilter = "apk";

    @Before
    public void setUp() throws Exception {
        signedFolder = new File(getClass().getClassLoader().getResource("apks").toURI().getPath());

        sortedSinged = Arrays.asList(signedFolder.listFiles());
        Collections.sort(sortedSinged);
    }

    @Test
    public void testSingleFolder() throws Exception {
        List<File> result = new FileArgParser().parseAndSortUniqueFilesNonRecursive(new String[]{signedFolder.getAbsolutePath()}, extFilter);
        assertEquals(sortedSinged, result);
    }

    @Test
    public void testSingleFile() throws Exception {
        File apk = signedFolder.listFiles()[0];
        List<File> result = new FileArgParser().parseAndSortUniqueFilesNonRecursive(new String[]{apk.getAbsolutePath()}, extFilter);
        assertEquals(Collections.singletonList(apk), result);
    }

    @Test
    public void testTwoFiles() throws Exception {
        File apk = signedFolder.listFiles()[0];
        File apk2 = signedFolder.listFiles()[1];

        List<File> result = new FileArgParser().parseAndSortUniqueFilesNonRecursive(new String[]{apk.getAbsolutePath(), apk2.getAbsolutePath()}, extFilter);

        List<File> all = Arrays.asList(apk, apk2);
        Collections.sort(all);
        assertEquals(all, result);
    }

    @Test
    public void testThreeFilesShouldIgnoreDouble() throws Exception {
        File apk = signedFolder.listFiles()[0];
        File apk2 = signedFolder.listFiles()[1];
        File apk3 = signedFolder.listFiles()[0];

        List<File> result = new FileArgParser().parseAndSortUniqueFilesNonRecursive(new String[]{apk.getAbsolutePath(), apk2.getAbsolutePath(), apk3.getAbsolutePath()}, extFilter);

        List<File> all = Arrays.asList(apk, apk2);
        Collections.sort(all);
        assertEquals(all, result);
    }

    @Test
    public void testThreeFilesShouldIgnoreDoubleWithFolder() throws Exception {
        File apk = signedFolder.listFiles()[0];

        List<File> result = new FileArgParser().parseAndSortUniqueFilesNonRecursive(new String[]{apk.getAbsolutePath(), signedFolder.getAbsolutePath()}, extFilter);

        assertEquals(sortedSinged, result);
    }

    @Test
    public void testNotMatchingFilterExtension() throws Exception {
        List<File> result = new FileArgParser().parseAndSortUniqueFilesNonRecursive(new String[]{signedFolder.getAbsolutePath()}, "unk");
        assertEquals(Collections.emptyList(), result);
    }
}
