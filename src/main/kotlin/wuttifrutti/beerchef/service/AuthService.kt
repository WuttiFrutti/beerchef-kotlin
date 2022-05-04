package wuttifrutti.beerchef.service

import com.mongodb.client.MongoCollection
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import org.springframework.stereotype.Service
import org.springframework.web.util.WebUtils
import wuttifrutti.beerchef.exceptions.DidNotWrite
import wuttifrutti.beerchef.exceptions.ExceptionTypes
import wuttifrutti.beerchef.exceptions.NotAllowed
import wuttifrutti.beerchef.helpers.PasswordUtils
import wuttifrutti.beerchef.model.Token
import wuttifrutti.beerchef.model.User
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
class AuthService(private val userRepository: MongoCollection<User>) {

    fun getUser(request: HttpServletRequest): User {
        val cookie = WebUtils.getCookie(
                request,
                "token"
            ) ?: throw NotAllowed("Performing this action without a token")

        return userRepository.findOne(
            User::tokens / Token::token eq UUID.fromString(cookie.value)
        ) ?: throw NotAllowed("Performing this action without logging in")
    }


    fun register(email: String, username: String, password: String): User {
        val salt = PasswordUtils.generateSalt();
        val hash = PasswordUtils.hash(password, salt)

        val inserted = User(username, email, hash, salt)

        if (!userRepository.insertOne(inserted).wasAcknowledged()) {
            throw DidNotWrite("New user")
        }

        return inserted
    }

    fun login(email: String, password: String): UUID {
        val requested = userRepository.findOne(User::email eq email)
        if (requested != null && PasswordUtils.isExpectedPassword(password, requested.salt, requested.hash)) {
            val token = UUID.randomUUID()
            requested.tokens.plus(
                Token(
                    token = token,
                    messageToken = "",
                    expire = false,
                    lastAction = LocalDateTime.now()
                )
            )

            userRepository.save(requested)
            return token
        }

        throw NotAllowed("Logging in with incorrect credentials", ExceptionTypes.WRONG_CREDENTIALS)
    }


}
