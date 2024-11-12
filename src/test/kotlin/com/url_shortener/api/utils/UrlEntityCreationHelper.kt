package com.url_shortener.api.utils

import com.url_shortener.api.repository.entity.UrlEntity

class UrlEntityCreationHelper {

    companion object {
        @JvmStatic
        fun create(
            urlIdentifier: String,
            originalUrl: String,
            shortenedUrl: String
        ): UrlEntity {
            return UrlEntity(
                urlIdentifier = urlIdentifier,
                originalUrl = originalUrl,
                shortenedUrl = shortenedUrl
            )
        }
    }
}