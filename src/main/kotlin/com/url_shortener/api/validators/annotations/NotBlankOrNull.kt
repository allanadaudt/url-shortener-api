package com.url_shortener.api.validators.annotations

import com.url_shortener.api.validators.NotBlankOrNullValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [NotBlankOrNullValidator::class])
@Target(FIELD, PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class NotBlankOrNull(
    val message: String = "Field cannot be blank or null",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

