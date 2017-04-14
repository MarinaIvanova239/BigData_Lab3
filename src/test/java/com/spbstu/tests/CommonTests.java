package com.spbstu.tests;

import com.spbstu.configuration.TestDataProccessor;
import com.spbstu.database.MongoDbService;
import com.spbstu.database.documents.PageContent;
import com.spbstu.database.documents.VisitedPages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = { EmbeddedMongoAutoConfiguration.class })
public class CommonTests {

    private final static String TEST_PAGE = "https://www.test.ru/";
    private final static String TEST_CONTENT_FILE = "files/testContent.txt";

    @Autowired
    private MongoDbService database;
    private String testContent;

    @Before
    public void testSetup() throws Exception {
        testContent = TestDataProccessor.readTestContentFromFile(TEST_CONTENT_FILE);
        database.reset();
    }

    @Test
    public void contentShouldBeCorrectlySavedInDatabase() throws Exception {
        PageContent content = new PageContent(TEST_PAGE, testContent);
        database.contains(content);
        database.shouldContain(content);
    }

    @Test
    public void pageShouldBeSavedInDatabaseAfterVisiting() {
        VisitedPages page = new VisitedPages(TEST_PAGE);
        database.contains(page);
        database.shouldContain(TEST_PAGE);
    }
}
