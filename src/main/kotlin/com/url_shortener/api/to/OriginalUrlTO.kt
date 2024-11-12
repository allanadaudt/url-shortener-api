package com.url_shortener.api.to

import com.fasterxml.jackson.annotation.JsonProperty
import com.url_shortener.api.validators.annotations.NotBlankOrNull

data class OriginalUrlTO(

    @field:NotBlankOrNull(message = "original_url cannot be blank or null")
    @get:JsonProperty("original_url")
    val originalUrl: String,

    @field:NotBlankOrNull(message = "url_identifier cannot be blank or null")
    @get:JsonProperty("url_identifier")
    val urlIdentifier: String
)
