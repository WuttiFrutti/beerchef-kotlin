package wuttifrutti.beerchef.model

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.LocalDateTime

data class Drink(
    var amount: Int,
    val user: Id<User>,
    val list: Id<BeerList>,
    val updatedOn: LocalDateTime = LocalDateTime.now(),
    @BsonId val key: Id<Drink> = newId()
)
