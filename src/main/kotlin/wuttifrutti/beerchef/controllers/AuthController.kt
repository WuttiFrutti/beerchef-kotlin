package wuttifrutti.beerchef.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpServerErrorException
import wuttifrutti.beerchef.helpers.PasswordUtils
import wuttifrutti.beerchef.repository.SafeUser
import wuttifrutti.beerchef.repository.User
import wuttifrutti.beerchef.repository.UserRepository

@RestController
@RequestMapping("/auth")
class AuthController {


    data class RequestUser(val username:String, val email:String, val password:String)

    @PostMapping
    fun register(@RequestBody user: RequestUser) : SafeUser {
        val salt = PasswordUtils.generateSalt();
        val hash = PasswordUtils.hash(user.password, salt)

        val inserted = User(user.username,user.email,hash, salt)

        if(UserRepository.insertOne(inserted).wasAcknowledged()){
            return inserted.toSafe();
        }else {
            throw HttpServerErrorException(HttpStatus.BAD_REQUEST);
        }
    }

}
