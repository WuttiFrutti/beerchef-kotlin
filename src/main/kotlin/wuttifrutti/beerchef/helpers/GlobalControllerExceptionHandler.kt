package wuttifrutti.beerchef.helpers

import com.mongodb.MongoWriteException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(MongoWriteException::class)
    fun handleWriteException(error:MongoWriteException): ResponseEntity<Map<String, String>> {
        println(error)
        return ResponseEntity(mapOf("message" to "object already exists"),HttpStatus.CONFLICT)
    }

}