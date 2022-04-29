package wuttifrutti.beerchef.controllers

import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import wuttifrutti.beerchef.helpers.PasswordUtils
import wuttifrutti.beerchef.model.SafeUser
import wuttifrutti.beerchef.model.Token
import wuttifrutti.beerchef.model.User
import wuttifrutti.beerchef.repository.UserRepository
import wuttifrutti.beerchef.service.AuthService
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class AuthController {

    data class RequestUser(val username: String, val email: String, val password: String)

    @PostMapping("/user")
    fun register(@RequestBody user: RequestUser): SafeUser {
        val salt = PasswordUtils.generateSalt();
        val hash = PasswordUtils.hash(user.password, salt)

        val inserted = User(user.username, user.email, hash, salt)

        if (UserRepository.insertOne(inserted).wasAcknowledged()) {
            return inserted.toSafe()
        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }
    }

    data class RequestLogin(val email: String, val password: String)

    @PostMapping("/login")
    fun login(@RequestBody login: RequestLogin, response: HttpServletResponse, request: HttpServletRequest)
            : Map<String, Any> {

        try {
            AuthService.getUser(request)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        } catch (_: ResponseStatusException) {
        }

        val requested = UserRepository.findOne(User::email eq login.email)
        if (requested != null && PasswordUtils.isExpectedPassword(login.password, requested.salt, requested.hash)) {
            val token = UUID.randomUUID()
            requested.tokens.plus(
                Token(
                    token = token,
                    messageToken = "",
                    expire = false,
                    lastAction = LocalDateTime.now()
                )
            )
            response.addCookie(Cookie("token", token.toString()))
            UserRepository.save(requested)
            return mapOf("token" to token)
        }

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }



    @PostMapping("/validate")
    fun validate(request: HttpServletRequest): User = AuthService.getUser(request)


    @DeleteMapping("/login")
    fun logout(response: HttpServletResponse, request: HttpServletRequest) {
        AuthService.getUser(request)

        response.addCookie(Cookie("token", null));
    }
}
