package wuttifrutti.beerchef.controllers;


import org.litote.kmongo.Id
import org.springframework.web.bind.annotation.*
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.model.Drink
import wuttifrutti.beerchef.model.User
import wuttifrutti.beerchef.service.AuthService
import wuttifrutti.beerchef.service.ListService
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/list")
class ListController(private val authService: AuthService, private val listService: ListService) {



    @GetMapping("/all")
    fun lists(request: HttpServletRequest): Map<String, Any> {
        val user = authService.getUser(request)

        return listService.lists(user)
    }

    @GetMapping("/ended")
    fun ended(request: HttpServletRequest): List<BeerList> {
        val user = authService.getUser(request)

        return listService.ended(user)
    }

    @GetMapping("{listId}/user/{userId}")
    fun listUserDrinks(
        @PathVariable listId: Id<BeerList>,
        @PathVariable userId: Id<User>,
        request: HttpServletRequest
    ): List<Drink> {
        val user = authService.getUser(request)

        // TODO: Make safe for specific user

        return listService.listUserDrinks(listId, userId)
    }

    @GetMapping("{listId}/drinks")
    fun listDrinks(@PathVariable listId: Id<BeerList>, request: HttpServletRequest): List<Drink> {
        val user = authService.getUser(request)

        // TODO: Make safe for specific user

        return listService.listDrinks(listId)
    }

    data class RequestList(val name: String, val price: Int, val join: Boolean, val users: List<String>)

    @PostMapping
    fun createList(@RequestBody requested: RequestList, request: HttpServletRequest): Map<String, Any> {
        val user = authService.getUser(request)

        // TODO: send join requests

        return listService.createList(requested.users, requested.name, requested.price, requested.join, user)
    }

    data class AddUserToListRequest(val shareId: UUID)

    @PostMapping("/user")
    fun addUserToList(@RequestBody addToListRequest: AddUserToListRequest, request: HttpServletRequest): BeerList? {
        val user = authService.getUser(request)

        return listService.addUserToList(addToListRequest.shareId, user.key)
    }


}
