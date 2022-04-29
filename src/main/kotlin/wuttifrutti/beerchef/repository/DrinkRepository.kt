package wuttifrutti.beerchef.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.model.Drink

class DrinkRepository(private val database: MongoDatabase){
    operator fun invoke(): MongoCollection<Drink>{
        return database.getCollection<Drink>()
    }
}
