package com.spbstu.tests;


import com.spbstu.common.Parser;
import com.spbstu.configuration.TestDataProccessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    private final static String TEST_CONTENT_FILE = "files/testContent.txt";
    private final static String EXPECTED_PARSE_LINK = "http://expected-link.ru/";

    private String testContent;

    @Before
    public void testSetup() throws Exception {
        testContent = TestDataProccessor.readTestContentFromFile(TEST_CONTENT_FILE);
    }

    @Test
    public void parserShouldWorkCorrectly() {
        Document document = Jsoup.parse(testContent);
        List<String> links = new ArrayList<String>();
        Parser.getLinks(document, links);
        assertEquals(links.size(), 1);
        assertEquals(links.get(0), EXPECTED_PARSE_LINK);
    }
}
