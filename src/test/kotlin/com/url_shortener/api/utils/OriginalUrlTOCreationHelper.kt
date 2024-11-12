package com.url_shortener.api.utils

import com.url_shortener.api.to.OriginalUrlTO

class OriginalUrlTOCreationHelper {

    companion object {
        @JvmStatic
        fun create(
            urlIdentifier: String,
            originalUrl: String
        ): OriginalUrlTO {
            return OriginalUrlTO(
                originalUrl = originalUrl,
                urlIdentifier = urlIdentifier
            )
        }
    }
}