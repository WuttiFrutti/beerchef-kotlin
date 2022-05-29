package wuttifrutti.beerchef.model

import java.time.LocalDateTime
import java.util.*

data class Token(
    val token: String = UUID.randomUUID().toString(),
    val messageToken: String = "",
    val expire: Boolean = true,
    val lastAction: LocalDateTime = LocalDateTime.now()
)
