package wuttifrutti.beerchef.service

import com.mongodb.client.MongoCollection
import org.litote.kmongo.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.model.Drink
import wuttifrutti.beerchef.model.ListUser
import wuttifrutti.beerchef.model.User
import java.util.*

@Service
class ListService(
    private val userRepository: MongoCollection<User>,
    @Qualifier("ended") private val endedBeerListRepository: MongoCollection<BeerList>,
    private val beerListRepository: MongoCollection<BeerList>,
    private val drinkRepository: MongoCollection<Drink>
) {



    fun lists(user: User): Map<String, Any> {
        val lists =
            beerListRepository.find(or(BeerList::owner eq user.key, BeerList::users / ListUser::user eq user.key))
        val userDrinks = drinkRepository.find(User::key eq user.key)

        val userIds = lists.map { it.users.map { listUser -> listUser.user } }.flatten()

        val users = userRepository.find(User::key `in` userIds)

        return mapOf(
            "lists" to lists,
            "users" to users,
            "userDrinks" to userDrinks
        )
    }

    fun ended(user: User): List<BeerList> {
        return endedBeerListRepository.find(
            or(
                BeerList::owner eq user.key,
                BeerList::users / User::key eq user.key
            )
        ).toList()
    }

    fun listUserDrinks(listId: Id<BeerList>, userId: Id<User>): List<Drink> {
        return drinkRepository.find(and(Drink::user eq userId, Drink::list eq listId)).toList()
    }

    fun listDrinks(listId: Id<BeerList>): List<Drink> {
        return drinkRepository.find(Drink::list eq listId).toList()
    }

    fun createList(users: List<String>, name: String, price: Int, join: Boolean, user: User): Map<String, Any> {
        val usersToAdd = if (users.isNotEmpty()) {
            userRepository.find(User::email `in` users)
        } else {
            emptyList<User>()
        }

        val notFoundUsers = users.filter { !usersToAdd.map { user -> user.email }.contains(it) }

        if (notFoundUsers.isNotEmpty()) {
            return mapOf(
                "emails" to notFoundUsers
            )
        }

        val list = BeerList(
            name = name,
            price = price,
            owner = user.key,
            users = if (join) setOf(ListUser(user = user.key)) else emptySet()
        )

        beerListRepository.save(list)

        return emptyMap()
    }

    fun addUserToList(shareId: UUID, user: Id<User>): BeerList? {
        return beerListRepository.findOneAndUpdate(
            BeerList::shareId eq shareId,
            push(BeerList::users, ListUser(user))
        )
    }
}

