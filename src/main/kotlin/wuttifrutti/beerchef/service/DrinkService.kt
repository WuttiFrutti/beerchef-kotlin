package wuttifrutti.beerchef.service

import com.mongodb.client.MongoCollection
import org.litote.kmongo.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import wuttifrutti.beerchef.exceptions.NotFound
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.model.Drink
import wuttifrutti.beerchef.model.ListUser
import wuttifrutti.beerchef.model.User
import java.time.LocalDateTime

@Service
class DrinkService(
    private val userRepository: MongoCollection<User>,
    private val beerListRepository: MongoCollection<BeerList>,
    private val drinkRepository: MongoCollection<Drink>
) {


    fun addDrinkToList(id: Id<BeerList>, date: LocalDateTime, amount: Int, userId: Id<User>?, user: User): Drink {
        var dateVar = date

        val list =
            beerListRepository.findOne(
                and(
                    BeerList::key eq id,
                    or(BeerList::owner eq user.key, BeerList::users / ListUser::user eq user.key)
                )
            )
                ?: throw NotFound("The requested list")


        val onUser = if (userId != null && user.key == list.owner) {
            userRepository.findOne(User::key eq userId)
                ?: throw NotFound("The requested user")
        } else {
            dateVar = LocalDateTime.now()
            user
        }
        val drink = Drink(amount = amount, onUser.key, list.key, updatedOn = dateVar)

        try {
            drinkRepository.save(drink)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        val listUser = list.users.find { it.user == onUser.key } ?: throw NotFound("The user in the list")
        listUser.total += amount
        listUser.drinks.plus(drink.key)

        onUser.total += amount
        list.total += amount

        beerListRepository.save(list)
        userRepository.save(onUser)

        return drink
    }

    fun editDrinkOnList(listId: Id<BeerList>, id: Id<Drink>, amount: Int, user: User): Drink {
        val list = beerListRepository.findOne(and(BeerList::owner eq user.key, BeerList::key eq listId))
            ?: throw NotFound("The requested list")
        val drink = drinkRepository.findOne(and(Drink::list eq list.key, Drink::key eq id))
            ?: throw NotFound("The requested drink")

        val change = amount - drink.amount

        list.total += change
        val listUser = list.users.find { it.user == drink.user }
        if (listUser != null) listUser.total += change

        drink.amount = amount

        drinkRepository.save(drink)
        beerListRepository.save(list)
        userRepository.updateOne(User::key eq drink.user, inc(User::total, change))

        return drink
    }

    fun deleteDrinkOnList(drinkId: Id<Drink>, user: User) {
        val drink = drinkRepository.findOne(Drink::key eq drinkId) ?: throw NotFound("The requested drink")
        val list = beerListRepository.findOne(BeerList::key eq drink.list) ?: throw NotFound("The requested list")

        // TODO: Something wrong here with user checking -> this should be done earlier?

        val findUser = if (list.owner == user.key) drink.user else user.key

        val onListUser = list.users.find { it.user == findUser } ?: throw NotFound("The user in the list")

        onListUser.drinks.minus(drink.key)
        onListUser.total -= drink.amount
        list.total -= drink.amount

        drinkRepository.deleteOneById(drink.key)
        userRepository.updateOne(User::key eq drink.user, inc(User::total, -drink.amount))

    }

}