package com.url_shortener.api.exception

data class ErrorTO(
    val error: String,
    val details: List<ErrorInfoTO>
)
