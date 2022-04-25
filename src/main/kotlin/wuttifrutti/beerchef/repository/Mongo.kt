package wuttifrutti.beerchef.repository

import org.litote.kmongo.KMongo
import org.litote.kmongo.*


val client = KMongo.createClient()
val database = client.getDatabase("beerchef-kotlin")