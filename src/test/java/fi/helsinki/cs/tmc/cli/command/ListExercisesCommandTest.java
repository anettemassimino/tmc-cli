package fi.helsinki.cs.tmc.cli.command;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fi.helsinki.cs.tmc.cli.Application;
import fi.helsinki.cs.tmc.cli.io.TestIo;
import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.ProgressObserver;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ListExercisesCommandTest {

    Application app;
    TestIo io;
    TmcCore mockCore;

    @Before
    public void setUp() {
        io = new TestIo();
        app = new Application(io);
        mockCore = mock(TmcCore.class);
        app.setTmcCore(mockCore);
    }

    @Test
    public void giveMessageIfNoExercisesOnCourse() {
        Callable<List<Course>> callable = new Callable<List<Course>>() {
            @Override
            public List<Course> call() throws Exception {
                Course course = new Course("test-course123");

                ArrayList<Course> tmp = new ArrayList<>();
                tmp.add(course);
                return tmp;
            }
        };

        when(mockCore.listCourses((ProgressObserver) anyObject())).thenReturn(callable);
        String[] args = {"list-exercises", "test-course123"};
        app.run(args);
        assertThat(io.out(), containsString("have any exercises"));
    }

    @Test
    public void listExercisesGivesCorrectExercises() {
        Callable<List<Course>> callable = new Callable<List<Course>>() {
            @Override
            public List<Course> call() throws Exception {
                List<Exercise> list = new ArrayList<>();
                list.add(new Exercise("hello-exercise"));
                list.add(new Exercise("cool-exercise"));

                Course course = new Course("test-course123");
                course.setExercises(list);

                ArrayList<Course> tmp = new ArrayList<>();
                tmp.add(course);
                return tmp;
            }
        };

        when(mockCore.listCourses((ProgressObserver) anyObject())).thenReturn(callable);
        String[] args = {"list-exercises", "test-course123"};
        app.run(args);
        assertThat(io.out(), containsString("hello-exercise"));
    }

    @Test
    public void emptyArgsGivesAnErrorMessage() {
        when(mockCore.listCourses((ProgressObserver) anyObject())).thenReturn(null);
        String[] args = {"list-exercises"};
        app.run(args);
        assertThat(io.out(), containsString("No course specified"));
    }
}
