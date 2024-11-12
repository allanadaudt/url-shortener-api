package com.url_shortener.api.mapper

import com.url_shortener.api.repository.entity.UrlEntity
import com.url_shortener.api.to.ShortenedUrlTO
import com.url_shortener.api.to.OriginalUrlTO
import org.springframework.stereotype.Component

@Component
class UrlMapper {

    private val shortDomain = "http://short.url"

    fun toEntity(request: OriginalUrlTO): UrlEntity {
        return UrlEntity(
            urlIdentifier = request.urlIdentifier,
            originalUrl = request.originalUrl,
            shortenedUrl = "$shortDomain/${request.urlIdentifier}"
        )
    }

    fun mapToShortUrl(entity: UrlEntity): ShortenedUrlTO {
        return ShortenedUrlTO(
            shortenedUrl = entity.shortenedUrl
        )
    }
}