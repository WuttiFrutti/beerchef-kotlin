package wuttifrutti.beerchef.repository

import org.litote.kmongo.getCollection
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID
import kotlin.reflect.typeOf

data class Token(val token: UUID, val messageToken: String = "", val expire: Boolean = true, val lastAction: LocalDateTime = LocalDateTime.now()) {
    override fun equals(other: Any?): Boolean {
        return other is Token && other.token == this.token;
    }
}


data class SafeUser(val username: String, val email:String, val total: Number);

data class User(
    val username: String,
    val email: String,
    val hash: ByteArray,
    val salt: ByteArray,
    val total: Number = 0,
    var tokens: ArrayList<Token> = ArrayList()
) {


    fun toSafe(): SafeUser {
        return SafeUser(this.username,this.email, this.total);
    }


}


val UserRepository = database.getCollection<User>();


