package com.url_shortener.api.to

import com.fasterxml.jackson.annotation.JsonProperty

data class ShortenedUrlTO(

    @get:JsonProperty("shortened_url")
    val shortenedUrl: String
)
