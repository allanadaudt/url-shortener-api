package com.url_shortener.api.exception

class UrlShorteningException(message: String, cause: Throwable? = null) : RuntimeException(message, cause) {
}