package wuttifrutti.beerchef.controllers

import org.litote.kmongo.Id
import org.springframework.web.bind.annotation.*
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.model.Drink
import wuttifrutti.beerchef.model.User
import wuttifrutti.beerchef.service.AuthService
import wuttifrutti.beerchef.service.DrinkService
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/list/drink")
class DrinkController(private val authService: AuthService, private val drinkService: DrinkService) {




    data class AddDrink(
        val id: Id<BeerList>,
        var date: LocalDateTime,
        val amount: Int,
        val user: Id<User>? = null
    )

    @PostMapping
    fun addDrinkToList(@RequestBody addDrink: AddDrink, request: HttpServletRequest): Drink {
        val user = authService.getUser(request)

        return drinkService.addDrinkToList(addDrink.id, addDrink.date, addDrink.amount, addDrink.user, user)
    }




    data class EditDrink(val listId: Id<BeerList>, val id: Id<Drink>, val amount: Int)

    @PutMapping
    fun editDrinkOnList(@RequestBody editDrink: EditDrink, request: HttpServletRequest): Drink {
        val user = authService.getUser(request)

        return drinkService.editDrinkOnList(editDrink.listId, editDrink.id, editDrink.amount, user)
    }



    data class DeleteDrink(val id: Id<Drink>)

    @DeleteMapping
    fun deleteDrinkOnList(@RequestBody deleteDrink: DeleteDrink, request: HttpServletRequest) {
        val user = authService.getUser(request)

        return drinkService.deleteDrinkOnList(deleteDrink.id, user)
    }
}
