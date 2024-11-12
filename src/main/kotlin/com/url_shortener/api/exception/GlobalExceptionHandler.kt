package com.url_shortener.api.exception

import com.url_shortener.api.exception.to.ErrorInfoTO
import com.url_shortener.api.exception.to.ErrorTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

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