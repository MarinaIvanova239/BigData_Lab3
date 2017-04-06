package tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.spbstu.configuration.MongoConfiguration;
import com.spbstu.database.MongoDbService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoConfiguration.class})
public class MongoDbTests {

    private MongoDbService database;

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
