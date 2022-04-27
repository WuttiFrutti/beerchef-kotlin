package wuttifrutti.beerchef.model

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.*

data class BeerList(
    val name: String,
    val price: Number,
    val owner: Id<User>,
    val users: Set<ListUser> = emptySet(),
    val shareId: UUID = UUID.randomUUID(),
    val total: Number = 0,
    @BsonId val key: Id<User> = newId()
)

data class ListUser(
    val user: Id<User>,
    val drinks: Set<Id<Drink>> = emptySet(),
    val total: Number = 0
)