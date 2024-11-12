package com.url_shortener.api.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import javax.lang.model.type.ErrorType

class ExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ErrorTO {
        val errors: MutableList<ErrorInfoTO> = mutableListOf()

        ex.bindingResult.allErrors.forEach { error ->
            val errorMessage = error.defaultMessage
            errors.add(ErrorInfoTO(errorMessage ?: "Unknown error"))
        }

        return ErrorTO("Validation failed", errors)
    }
}