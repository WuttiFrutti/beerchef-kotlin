package wuttifrutti.beerchef.repository

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.IndexOptions
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.litote.kmongo.index
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.model.Drink
import wuttifrutti.beerchef.model.User

@Component
class DatabaseBeans{

    @Bean
    fun database(): MongoDatabase {
        val client = KMongo.createClient(MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.STANDARD).build())

        return client.getDatabase("beerchef-kotlin")
    }

    @Bean
    fun drinkCollection(database: MongoDatabase): MongoCollection<Drink> = database.getCollection()

    @Primary
    @Bean
    fun beerListCollection(database: MongoDatabase): MongoCollection<BeerList> = database.getCollection()

    @Bean
    @Qualifier("ended")
    fun endedBeerListCollection(database: MongoDatabase): MongoCollection<BeerList> = database.getCollection<BeerList>("ended")

    @Bean
    fun userCollection(database: MongoDatabase): MongoCollection<User> {
        val db = database.getCollection<User>()
        db.createIndex(index(User::email to true), IndexOptions().unique(true))
        return db
    }


}

