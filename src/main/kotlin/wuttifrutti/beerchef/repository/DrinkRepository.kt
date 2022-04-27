package wuttifrutti.beerchef.repository

import org.litote.kmongo.getCollection
import wuttifrutti.beerchef.model.Drink

val DrinkRepository = run {
    val db = database.getCollection<Drink>()
    db
}
