package wuttifrutti.beerchef.helpers

import com.mongodb.MongoWriteException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import wuttifrutti.beerchef.exceptions.DidNotWrite
import wuttifrutti.beerchef.exceptions.NotAllowed
import wuttifrutti.beerchef.exceptions.NotFound


@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(MongoWriteException::class)
    fun handleWriteException(error:MongoWriteException): ResponseEntity<Map<String, String>> {
        println(error)
        return ResponseEntity(mapOf("message" to "object already exists"),HttpStatus.CONFLICT)
    }

    @ExceptionHandler(NotFound::class)
    fun handleNotFound(error:NotFound): ResponseEntity<Map<String, String?>> {
        println(error)
        return ResponseEntity(mapOf("message" to error.message),HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(NotAllowed::class)
    fun handleNotAllowed(error:NotAllowed): ResponseEntity<Map<String, String?>> {
        println(error)
        return ResponseEntity(mapOf("message" to error.message),HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(DidNotWrite::class)
    fun handleDidNotWrite(error:DidNotWrite): ResponseEntity<Map<String, String?>> {
        println(error)
        return ResponseEntity(mapOf("message" to error.message),HttpStatus.CONFLICT)
    }

}