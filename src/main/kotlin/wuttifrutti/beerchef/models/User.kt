package wuttifrutti.beerchef.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDateTime


data class User(
    @Id
    val id: ObjectId = ObjectId.get(),
    val name: String,
    val description: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val modifiedDate: LocalDateTime = LocalDateTime.now()
)