package wuttifrutti.beerchef.repository

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoDatabase
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo


fun database(): MongoDatabase {
    val client = KMongo.createClient(MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.STANDARD).build())
    return client.getDatabase("beerchef-kotlin")
}

