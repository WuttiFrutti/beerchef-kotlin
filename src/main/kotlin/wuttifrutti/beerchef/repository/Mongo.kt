package wuttifrutti.beerchef.repository

import com.mongodb.MongoClientSettings
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo
import org.litote.kmongo.*


val client = KMongo.createClient(MongoClientSettings.builder().uuidRepresentation(UuidRepresentation).build())
val database = client.getDatabase("beerchef-kotlin")