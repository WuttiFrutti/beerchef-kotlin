package wuttifrutti.beerchef.model

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class Drink(
    val amount: Number,
    val user: Id<User>,
    val list: Id<BeerList>,
    @BsonId val id: Id<User> = newId()
)