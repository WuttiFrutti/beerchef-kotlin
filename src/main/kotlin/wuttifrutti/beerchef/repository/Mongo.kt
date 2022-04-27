package wuttifrutti.beerchef.repository

import com.mongodb.MongoClientSettings
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo


val client = KMongo.createClient(MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.STANDARD).build())
val database = client.getDatabase("beerchef-kotlin")