package wuttifrutti.beerchef.repository

import org.litote.kmongo.getCollection
import wuttifrutti.beerchef.model.BeerList

val BeerListRepository = run {
    val db = database.getCollection<BeerList>()
    db
}

