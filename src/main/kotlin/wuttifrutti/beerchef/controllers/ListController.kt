package wuttifrutti.beerchef.controllers;


import org.litote.kmongo.*
import org.springframework.web.bind.annotation.*
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.model.Drink
import wuttifrutti.beerchef.model.ListUser
import wuttifrutti.beerchef.model.User
import wuttifrutti.beerchef.repository.BeerListRepository
import wuttifrutti.beerchef.repository.DrinkRepository
import wuttifrutti.beerchef.repository.EndedBeerListRepository
import wuttifrutti.beerchef.repository.UserRepository
import wuttifrutti.beerchef.service.AuthService
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/list")
class ListController {

    @GetMapping("/all")
    fun lists(request: HttpServletRequest): Map<String, Any> {
        val user = AuthService.getUser(request)

        val lists =
            BeerListRepository.find(or(BeerList::owner eq user.key, BeerList::users / ListUser::user eq user.key))
        val userDrinks = DrinkRepository.find(User::key eq user.key)

        val userIds = lists.map { it.users.map { listUser -> listUser.user } }.flatten()

        val users = UserRepository.find(User::key `in` userIds)

        return mapOf(
            "lists" to lists,
            "users" to users,
            "userDrinks" to userDrinks
        )
    }

    @GetMapping("/ended")
    fun ended(request: HttpServletRequest): List<BeerList> {
        val user = AuthService.getUser(request)

        return EndedBeerListRepository.find(
            or(
                BeerList::owner eq user.key,
                BeerList::users / User::key eq user.key
            )
        ).toList()
    }

    @GetMapping("{listId}/user/{userId}")
    fun listUserDrinks(
        @PathVariable listId: Id<BeerList>,
        @PathVariable userId: Id<User>,
        request: HttpServletRequest
    ): List<Drink> {
        val user = AuthService.getUser(request)

        // TODO: Make safe for specific user

        return DrinkRepository.find(and(Drink::user eq userId, Drink::list eq listId)).toList()
    }

    @GetMapping("{listId}/drinks")
    fun listDrinks(@PathVariable listId: Id<BeerList>, request: HttpServletRequest): List<Drink> {
        val user = AuthService.getUser(request)

        // TODO: Make safe for specific user

        return DrinkRepository.find(Drink::list eq listId).toList()
    }

    data class RequestList(val name: String, val price: Int, val join: Boolean, val users: List<String>)

    @PostMapping
    fun createList(@RequestBody requested: RequestList, request: HttpServletRequest): Map<String, Any> {
        val user = AuthService.getUser(request)

        val usersToAdd = if (requested.users.isNotEmpty()) {
            UserRepository.find(User::email `in` requested.users)
        } else {
            emptyList<User>()
        }

        val notFoundUsers = requested.users.filter { !usersToAdd.map { user -> user.email }.contains(it) }

        if (notFoundUsers.isNotEmpty()) {
            return mapOf(
                "emails" to notFoundUsers
            )
        }

        val list = BeerList(
            name = requested.name,
            price = requested.price,
            owner = user.key,
            users = if (requested.join) setOf(ListUser(user = user.key)) else emptySet()
        )

        BeerListRepository.save(list)


        // TODO: send join requests

        return emptyMap()
    }

    data class AddUserToListRequest(val shareId: UUID)

    @PostMapping("/user")
    fun addUserToList(@RequestBody addToListRequest: AddUserToListRequest, request: HttpServletRequest): BeerList? {
        val user = AuthService.getUser(request)

        return BeerListRepository.findOneAndUpdate(
            BeerList::shareId eq addToListRequest.shareId,
            push(BeerList::users, ListUser(user.key))
        )
    }




}
