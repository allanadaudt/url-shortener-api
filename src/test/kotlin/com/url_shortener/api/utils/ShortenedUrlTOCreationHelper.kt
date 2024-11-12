package com.url_shortener.api.utils

import com.url_shortener.api.to.ShortenedUrlTO

class ShortenedUrlTOCreationHelper {

    companion object {
        @JvmStatic
        fun create(shortenedUrl: String): ShortenedUrlTO {
            return ShortenedUrlTO(
                shortenedUrl = shortenedUrl
            )
        }
    }
}