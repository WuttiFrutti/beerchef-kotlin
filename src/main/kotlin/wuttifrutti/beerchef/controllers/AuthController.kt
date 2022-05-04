package wuttifrutti.beerchef.controllers

import org.springframework.web.bind.annotation.*
import wuttifrutti.beerchef.exceptions.NotAllowed
import wuttifrutti.beerchef.model.SafeUser
import wuttifrutti.beerchef.service.AuthService
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {




    data class RequestUser(val username: String, val email: String, val password: String)

    @PostMapping("/user")
    fun register(@RequestBody user: RequestUser): SafeUser {
        return authService.register(user.email, user.username, user.password).toSafe()
    }




    data class RequestLogin(val email: String, val password: String)

    @PostMapping("/login")
    fun login(@RequestBody login: RequestLogin, response: HttpServletResponse, request: HttpServletRequest)
            : Map<String, Any> {

        try {
            authService.getUser(request)
        } catch (_: NotAllowed) {
        }

        val token = authService.login(login.email, login.password)

        response.addCookie(Cookie("token", token.toString()))

        return mapOf("token" to token)
    }


    @PostMapping("/validate")
    fun validate(request: HttpServletRequest): SafeUser = authService.getUser(request).toSafe()


    @DeleteMapping("/login")
    fun logout(response: HttpServletResponse, request: HttpServletRequest) {
        authService.getUser(request)

        response.addCookie(Cookie("token", null));
    }
}
