package wuttifrutti.beerchef.repository

import org.litote.kmongo.getCollection
import wuttifrutti.beerchef.model.BeerList

val EndedBeerListRepository = run {
    val db = database.getCollection<BeerList>("endedbeerlist")
    db
}