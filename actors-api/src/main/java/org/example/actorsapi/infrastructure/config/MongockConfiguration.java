package org.example.actorsapi.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import lombok.val;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


@Configuration
@ConditionalOnProperty(prefix = "mongock", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MongockConfiguration {

    @Bean
    public MongockInitializingBeanRunner mongockInitializingBeanRunner(
            MongoClient reactiveMongoClient,
            ApplicationContext context,
            @Value("${app.mongo.database}") String databaseName,
            @Value("${mongock.migration-scan-package}") String scanPackage,
            @Value("${app.mongo.transaction-enabled}") boolean transactionEnabled) {
        return MongockSpringboot.builder()
                .setDriver(MongoReactiveDriver.withDefaultLock(reactiveMongoClient, databaseName))
                .addMigrationScanPackage(scanPackage)
                .setSpringContext(context)
                .setTransactional(transactionEnabled)
                .buildInitializingBeanRunner();
    }

    @Bean
    MongoClient mongoClient(@Value("${spring.mongodb.uri}") String databaseUri) {
        val codecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(databaseUri))
                .codecRegistry(codecRegistry)
                .build());
    }

    @Bean
    ReactiveMongoTransactionManager reactiveTransactionManager(ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory) {
        return new ReactiveMongoTransactionManager(reactiveMongoDatabaseFactory);
    }
}
