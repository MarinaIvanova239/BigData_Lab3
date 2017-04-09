package com.spbstu.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.spbstu.configuration.MongoConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoConfiguration.class})
public class MongoTests {

    private MongoDbTestService database;

    @Before
    public void testSetup() {
        database.reset();
    }

    @Test
    public void contentShouldBeSavedInDatabaseAfterProcessing() {
        DbContent pageContent = new DbContent();
        database.shouldContain(pageContent);
    }

    @Test
    public void pageShouldBeSavedInDatabaseAfterVisiting() {

    }
}
