package com.spbstu.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.io.IOException;

@Configuration
@Profile({"CRAWLER", "DOWNLOADER"})
@PropertySource("classpath:application.properties")
public class MongoConfiguration extends AbstractMongoConfiguration {

    private static final String FIRST_COLLECTION_NAME = "PageContent";
    private static final String SECOND_COLLECTION_NAME = "VisitedPages";

    @Autowired
    Environment env;

    @Bean(name="mongoClient")
    public MongoClient mongoClient() throws IOException {
        return new MongoClient(env.getProperty("mongodb.host"),
                Integer.parseInt(env.getProperty("mongodb.port")));
    }

    @Bean(name="mongoDbFactory")
    public MongoDbFactory mongoDbFactory() throws Exception {
        UserCredentials userCredentials = new UserCredentials(env.getProperty("mongodb.admin"),
                env.getProperty("mongodb.password"));
        return new SimpleMongoDbFactory(new Mongo(), env.getProperty("mongodb.database"),
                userCredentials);
    }

    @Bean(name="mongoTemplate")
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        if (!mongoTemplate.collectionExists(FIRST_COLLECTION_NAME)) {
            mongoTemplate.createCollection(FIRST_COLLECTION_NAME);
        }
        if (!mongoTemplate.collectionExists(SECOND_COLLECTION_NAME)) {
            mongoTemplate.createCollection(SECOND_COLLECTION_NAME);
        }
        return mongoTemplate;
    }

    @Override
    protected String getDatabaseName() {
        return env.getProperty("mongodb.database");
    }

    @Override
    public Mongo mongo() throws Exception {
        return new Mongo();
    }
}
