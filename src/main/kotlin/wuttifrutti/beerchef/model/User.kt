package wuttifrutti.beerchef.model

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class SafeUser(val username: String, val email: String, val total: Number);



data class User(
    val username: String,
    val email: String,
    val hash: ByteArray,
    val salt: ByteArray,
    val total: Number = 0,
    var tokens: Set<Token> = emptySet(),
    @BsonId val key: Id<User> = newId(),
) {


    fun toSafe(): SafeUser {
        return SafeUser(this.username, this.email, this.total);
    }


}