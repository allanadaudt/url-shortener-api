package com.url_shortener.api.service

import com.url_shortener.api.exception.UrlShorteningException
import com.url_shortener.api.mapper.UrlMapper
import com.url_shortener.api.repository.UrlRepository
import com.url_shortener.api.to.ShortenedUrlTO
import com.url_shortener.api.to.OriginalUrlTO
import org.springframework.stereotype.Service

@Service
class UrlService(
    private val repository: UrlRepository,
    private val mapper: UrlMapper
) {

    fun createShortUrl(request: OriginalUrlTO): ShortenedUrlTO {
        return try {
            val existingUrl = repository.findByOriginalUrl(request.originalUrl)
            if (existingUrl != null) {
                throw UrlShorteningException("URL already exists: ${existingUrl.shortenedUrl}")
            }

            val urlEntity = mapper.toEntity(request)
            repository.save(urlEntity)
            mapper.mapToShortUrl(urlEntity)
        } catch (e: Exception) {
            throw UrlShorteningException("Failed to create short URL", e)
        }
    }

    fun getAllUrls(): List<ShortenedUrlTO> {
        return try {
            repository.findAll().map { mapper.mapToShortUrl(it) }
        } catch (e: Exception) {
            throw UrlShorteningException("Failed to retrieve all URLs", e)
        }
    }

    fun getOriginalUrl(id: String): String? {
        return repository.findById(id)
            .orElseThrow { IllegalArgumentException("Record with id $id not found") }
            .originalUrl
    }

    fun deleteUrl(id: String) {
        try {
            repository.deleteById(id)
        } catch (e: Exception) {
            throw UrlShorteningException("Failed to delete URL for identifier: $id", e)
        }
    }
}