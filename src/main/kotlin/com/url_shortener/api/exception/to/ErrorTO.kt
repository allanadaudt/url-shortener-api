package com.url_shortener.api.exception.to

import com.url_shortener.api.exception.to.ErrorInfoTO

data class ErrorTO(
    val error: String,
    val details: List<ErrorInfoTO>
)
