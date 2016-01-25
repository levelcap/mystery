package com.brave.mystery.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration
{
    private static String uriString;
    private static String dbName;
    
    static {
    	uriString = System.getenv("MONGOLAB_URI");
        dbName = "heroku_k9rs6183";
    }

    @Override
    protected String getDatabaseName()
    {
        return dbName;
    }

    @Override
    public Mongo mongo() throws Exception
    {
        MongoClientURI uri = new MongoClientURI(uriString);
        return new MongoClient(uri);
    }
}
