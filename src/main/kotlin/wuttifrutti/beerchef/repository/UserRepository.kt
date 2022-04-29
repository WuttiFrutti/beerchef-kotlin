package wuttifrutti.beerchef.repository


import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.IndexOptions
import org.litote.kmongo.getCollection
import org.litote.kmongo.index
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.model.Drink
import wuttifrutti.beerchef.model.User


class UserRepository(private val database: MongoDatabase){
    operator fun invoke(): MongoCollection<User>{
        val db = database.getCollection<User>()
        db.createIndex(index(User::email to true), IndexOptions().unique(true))
        return db
    }
}
