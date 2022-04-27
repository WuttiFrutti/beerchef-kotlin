package wuttifrutti.beerchef.repository


import com.mongodb.client.model.IndexOptions
import org.litote.kmongo.getCollection
import org.litote.kmongo.index
import wuttifrutti.beerchef.model.User






val UserRepository = run {
    val db = database.getCollection<User>()
    db.createIndex(index(User::email to true), IndexOptions().unique(true))
    db
}

