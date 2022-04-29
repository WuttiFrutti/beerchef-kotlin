package wuttifrutti.beerchef

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.multiton
import org.kodein.di.generic.singleton
import org.kodein.di.jxinject.jx
import org.kodein.di.jxinject.jxInjectorModule
import wuttifrutti.beerchef.model.BeerList
import wuttifrutti.beerchef.repository.BeerListRepository
import wuttifrutti.beerchef.repository.database
import java.lang.reflect.Type

val kodein = Kodein {
    import(jxInjectorModule)
    bind<MongoDatabase>() with singleton  { database() }
    bind<MongoCollection<BeerList>>() with singleton { type:Type -> jx.newInstance<type>() }
}