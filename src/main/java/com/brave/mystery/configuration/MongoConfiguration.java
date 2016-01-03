package com.brave.mystery.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
public class MongoConfiguration extends AbstractMongoConfiguration
{
    private static String uriString;
    private static String mongodb_databasename;
    
    static {
    	uriString = System.getenv("MONGODB_URI");
    	mongodb_databasename = System.getenv("MONGODB_DB");
    }

    @Override
    protected String getDatabaseName()
    {
        return mongodb_databasename;
    }

    @Override
    public Mongo mongo() throws Exception
    {
        MongoClientURI uri = new MongoClientURI(uriString);
        return new MongoClient(uri);
    }
}
