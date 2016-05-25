package fi.helsinki.cs.tmc.cli.command;

import static org.junit.Assert.assertTrue;

import fi.helsinki.cs.tmc.cli.Application;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadExercisesCommandTest {

    Application app;
    OutputStream os;

    @Before
    public void setUp() {
        app = new Application();

        os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
    }

    @Test
    public void failIfCourseArgumentNotGiven() {
        String[] args = {"download"};
        app.run(args);
        assertTrue(os.toString().contains("You must give"));
    }

    @Test
    public void downloadWorks() throws IOException {
        String[] args = {"download", "cert-test"};
        app.run(args);

        assertTrue(Files.exists(Paths.get("./cert-test")));
        FileUtils.deleteDirectory(new File("cert-test"));
    }
}
