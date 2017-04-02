package java.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.app.MongoConfiguration;
import java.app.MongoDatabase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoConfiguration.class})
public class MongoDbTests {

    private MongoDatabase database;

    @Before
    public void testSetup() {
        database.reset();
    }

    @Test
    public void contentShouldBeSavedInDatabaseAfterProcessing() {

    }

    @Test
    public void pageShouldBeSavedInDatabaseAfterVisiting() {

    }
}
