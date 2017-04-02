package java.app;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;

@Configuration
@EnableMongoRepositories
@PropertySource("classpath:/../../resources/application.properties")
public class MongoConfiguration {

    @Autowired
    Environment env;

    @Bean(name="mongoClient")
    public MongoClient mongoClient() throws IOException {
        return new MongoClient(env.getProperty("mongodb.host"),
                Integer.parseInt(env.getProperty("mongodb.port")));
    }

    @Autowired
    @Bean(name="mongoDbFactory")
    public MongoDbFactory mongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoDbFactory(mongoClient, env.getProperty("mongodb.database"));
    }

    @Autowired
    @Bean(name="mongoTemplate")
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, env.getProperty("mongodb.database"));
    }
}
