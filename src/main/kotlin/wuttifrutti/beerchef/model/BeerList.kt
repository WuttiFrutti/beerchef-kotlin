package wuttifrutti.beerchef.model

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.*

data class BeerList(
    val name: String,
    val price: Int,
    val owner: Id<User>,
    val users: Set<ListUser> = emptySet(),
    val shareId: String = UUID.randomUUID().toString(),
    var total: Int = 0,
    @BsonId val key: Id<BeerList> = newId()
)

data class ListUser(
    val user: Id<User>,
    var drinks: Set<Id<Drink>> = emptySet(),
    var total: Int = 0
)
