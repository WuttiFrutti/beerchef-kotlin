package wuttifrutti.beerchef.repository

import org.litote.kmongo.getCollection

data class Token(val token: String, val messageToken: String, val expire: Boolean)


data class SafeUser(val username: String, val email:String, val total: Number);

data class User(
    val username: String,
    val email: String,
    val hash: ByteArray,
    val salt: ByteArray,
    val total: Number = 0,
    var tokens: Array<Token>
) {
    constructor(username: String, email: String, hash: ByteArray, salt: ByteArray) : this(username, email, hash, salt, 0, emptyArray());

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (username != other.username) return false
        if (email != other.email) return false
        if (!hash.contentEquals(other.hash)) return false
        if (!salt.contentEquals(other.salt)) return false
        if (total != other.total) return false
        if (!tokens.contentEquals(other.tokens)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + hash.hashCode()
        result = 31 * result + salt.hashCode()
        result = 31 * result + total.hashCode()
        result = 31 * result + tokens.contentHashCode()
        return result
    }

    fun toSafe(): SafeUser {
        return SafeUser(this.username,this.email, this.total);
    }


}


val UserRepository = database.getCollection<User>();


