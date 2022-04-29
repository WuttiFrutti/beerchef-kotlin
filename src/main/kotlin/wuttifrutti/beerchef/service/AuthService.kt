package wuttifrutti.beerchef.service

import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.util.WebUtils
import wuttifrutti.beerchef.model.Token
import wuttifrutti.beerchef.model.User
import wuttifrutti.beerchef.repository.UserRepository
import java.util.*
import javax.servlet.http.HttpServletRequest


class AuthService {

    companion object {
        fun getUser(request: HttpServletRequest): User {

            return UserRepository.findOne(
                User::tokens / Token::token eq UUID.fromString(
                    WebUtils.getCookie(
                        request,
                        "token"
                    )?.value
                )
            ) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

    }


}