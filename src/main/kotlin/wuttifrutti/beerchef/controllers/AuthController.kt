package wuttifrutti.beerchef.controllers

import com.fasterxml.jackson.databind.ser.std.UUIDSerializer
import org.litote.kmongo.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.server.ResponseStatusException
import wuttifrutti.beerchef.helpers.PasswordUtils
import wuttifrutti.beerchef.repository.SafeUser
import wuttifrutti.beerchef.repository.Token
import wuttifrutti.beerchef.repository.User
import wuttifrutti.beerchef.repository.UserRepository
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/auth")
class AuthController {


    data class RequestUser(val username:String, val email:String, val password:String)

    @PostMapping("/user")
    fun register(@RequestBody user: RequestUser) : SafeUser {
        val salt = PasswordUtils.generateSalt();
        val hash = PasswordUtils.hash(user.password, salt)

        val inserted = User(user.username,user.email,hash, salt)

        if(UserRepository.insertOne(inserted).wasAcknowledged()){
            return inserted.toSafe();
        }else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    data class RequestLogin(val email: String, val password: String, )

    @PostMapping("/user/login")
    fun login(@RequestBody login:RequestLogin): Map<String, Any> {

        val requested = UserRepository.findOne(User::email eq login.email)
        if (requested != null && PasswordUtils.isExpectedPassword(login.password, requested.salt, requested.hash)) {
            val token = UUID.randomUUID()
            requested.tokens.add(Token(token = token, messageToken = "", expire = false, lastAction = LocalDateTime.now()))

            UserRepository.save(requested)
            return mapOf("token" to token)
        }

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    data class Validation(val token:UUID)

    @PostMapping("/validate")
    fun validate(@RequestBody validation:Validation): User {
        val requested = UserRepository.findOne(User::tokens / Token::token eq validation.token)

        if(requested != null){
            return requested
        }
        throw  ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}
