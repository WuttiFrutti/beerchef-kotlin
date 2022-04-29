package wuttifrutti.beerchef.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.model.Drink

class EndedBeerListRepository(private val database: MongoDatabase){
    operator fun invoke(): MongoCollection<BeerList>{
        return database.getCollection<BeerList>("ended")
    }
}
