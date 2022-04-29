package wuttifrutti.beerchef.controllers

import org.litote.kmongo.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.model.Drink
import wuttifrutti.beerchef.model.ListUser
import wuttifrutti.beerchef.model.User
import wuttifrutti.beerchef.repository.BeerListRepository
import wuttifrutti.beerchef.repository.DrinkRepository
import wuttifrutti.beerchef.repository.UserRepository
import wuttifrutti.beerchef.service.AuthService
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RequestMapping("/list/drink")
class DrinkController {

    data class AddDrink(
        val id: Id<BeerList>,
        var date: LocalDateTime,
        val amount: Int,
        val user: Id<User>? = null
    )

    @PostMapping
    fun addDrinkToList(@RequestBody addDrink: AddDrink, request: HttpServletRequest): Drink {
        val user = AuthService.getUser(request)

        val list =
            BeerListRepository.findOne(
                and(
                    BeerList::key eq addDrink.id,
                    or(BeerList::owner eq user.key, BeerList::users / ListUser::user eq user.key)
                )
            )
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)


        val onUser = if (addDrink.user != null && user.key == list.owner) {
            UserRepository.findOne(User::key eq addDrink.user)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        } else {
            addDrink.date = LocalDateTime.now()
            user
        }
        val drink = Drink(amount = addDrink.amount, onUser.key, list.key)

        try {
            DrinkRepository.save(drink)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        val listUser = list.users.find { it.user == onUser.key } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        listUser.total += addDrink.amount
        listUser.drinks.plus(drink.key)

        onUser.total += addDrink.amount
        list.total += addDrink.amount

        BeerListRepository.save(list)
        UserRepository.save(onUser)


        return drink
    }

    data class EditDrink(val listId: Id<BeerList>, val id: Id<Drink>, val amount: Int)

    @PutMapping
    fun editDrinkOnList(@RequestBody editDrink: EditDrink, request: HttpServletRequest): Drink {
        val user = AuthService.getUser(request)

        val list = BeerListRepository.findOne(and(BeerList::owner eq user.key, BeerList::key eq editDrink.listId)) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND)
        val drink = DrinkRepository.findOne(and(Drink::list eq list.key, Drink::key eq editDrink.id)) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND)

        val change = editDrink.amount - drink.amount

        list.total += change
        val listUser = list.users.find { it.user == drink.user }
        if(listUser != null) listUser.total += change

        drink.amount = editDrink.amount

        DrinkRepository.save(drink)
        BeerListRepository.save(list)
        UserRepository.updateOne(User::key eq drink.user, inc(User::total, change))



        return  drink
    }

    data class DeleteDrink(val id: Id<Drink>)

    @DeleteMapping
    fun deleteDrinkOnList(@RequestBody deleteDrink: DeleteDrink, request: HttpServletRequest) {
        val user = AuthService.getUser(request)

        val drink = DrinkRepository.findOne(Drink::key eq deleteDrink.id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val list = BeerListRepository.findOne(BeerList::key eq drink.list) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        // TODO: Something wrong here with user checking -> this should be done earlier?

        val findUser = if(list.owner == user.key) drink.user else user.key

        val onListUser = list.users.find { it.user == findUser } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        onListUser.drinks.minus(drink.key)
        onListUser.total -= drink.amount
        list.total -= drink.amount

        DrinkRepository.deleteOneById(drink.key)
        UserRepository.updateOne(User::key eq drink.user, inc(User::total, -drink.amount))

        return
    }
}