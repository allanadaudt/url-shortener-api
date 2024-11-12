package com.url_shortener.api.validators

import com.url_shortener.api.validators.annotations.NotBlankOrNull
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class NotBlankOrNullValidator : ConstraintValidator<NotBlankOrNull, String?> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return !value.isNullOrBlank()
    }
}