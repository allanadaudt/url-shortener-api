package com.url_shortener.api.unit

import com.url_shortener.api.controller.UrlController
import com.url_shortener.api.exception.UrlShorteningException
import com.url_shortener.api.mapper.UrlMapper
import com.url_shortener.api.repository.UrlRepository
import com.url_shortener.api.repository.entity.UrlEntity
import com.url_shortener.api.service.UrlService
import com.url_shortener.api.to.OriginalUrlTO
import com.url_shortener.api.to.ShortenedUrlTO
import com.url_shortener.api.utils.OriginalUrlTOCreationHelper
import com.url_shortener.api.utils.ShortenedUrlTOCreationHelper
import com.url_shortener.api.utils.UrlEntityCreationHelper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.dao.EmptyResultDataAccessException
import java.util.*
import kotlin.NoSuchElementException
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class UrlServiceTest {

    @Mock
    private lateinit var repository: UrlRepository

    @Mock
    private lateinit var mapper: UrlMapper

    @InjectMocks
    private lateinit var urlService: UrlService

    @Test
    fun `SHOULD create short URL successfully`() {
        val urlIdentifier = UUID.randomUUID().toString()
        val originalUrl = "http://original.url"
        val shortenedUrl = "http://short.url/$urlIdentifier"

        val originalUrlTO = OriginalUrlTOCreationHelper.create(urlIdentifier, originalUrl)
        val urlEntity = UrlEntityCreationHelper.create(urlIdentifier, originalUrl, shortenedUrl)
        val shortenedUrlTO = ShortenedUrlTOCreationHelper.create(shortenedUrl)

        whenever(mapper.toEntity(originalUrlTO)).thenReturn(urlEntity)
        whenever(mapper.mapToShortUrl(urlEntity)).thenReturn(shortenedUrlTO)

        whenever(repository.save(urlEntity)).thenReturn(urlEntity)

        val result = urlService.createShortUrl(originalUrlTO)

        verify(repository, times(1)).save(any())

        verify(mapper, times(1)).toEntity(originalUrlTO)
        verify(mapper, times(1)).mapToShortUrl(urlEntity)

        assertEquals(shortenedUrl, result.shortenedUrl)
    }


    @Test
    fun `WHEN creating a shortened URL for an existing URL, SHOULD return the existing shortened URL`() {
        val urlIdentifier = UUID.randomUUID().toString()
        val originalUrl = "http://example.com"
        val shortenedUrl = "http://short.url/$urlIdentifier"

        val existingEntity = UrlEntityCreationHelper.create(urlIdentifier, originalUrl, shortenedUrl)

        repository.save(existingEntity)

        whenever(repository.findByOriginalUrl(originalUrl)).thenReturn(existingEntity)

        val request = OriginalUrlTO(originalUrl = originalUrl, urlIdentifier = urlIdentifier)

        assertThrows<UrlShorteningException> {
            urlService.createShortUrl(request)
        }
    }

    @Test
    fun `SHOULD return all short URLs successfully`() {
        val urlIdentifier = UUID.randomUUID().toString()
        val originalUrl = "http://original.url"
        val shortenedUrl = "http://short.url/$urlIdentifier"

        val urlEntity = UrlEntityCreationHelper.create(urlIdentifier, originalUrl, shortenedUrl)
        val shortenedUrlTO = ShortenedUrlTOCreationHelper.create(shortenedUrl)

        whenever(repository.findAll()).thenReturn(listOf(urlEntity))
        whenever(mapper.mapToShortUrl(urlEntity)).thenReturn(shortenedUrlTO)

        assertDoesNotThrow {
            urlService.getAllUrls()
        }

        verify(repository, times(1)).findAll()
        verify(mapper, times(1)).mapToShortUrl(urlEntity)
    }

    @Test
    fun `SHOULD return original URL successfully`() {
        val urlIdentifier = UUID.randomUUID().toString()
        val originalUrl = "http://original.url"
        val shortenedUrl = "http://short.url/$urlIdentifier"

        val urlEntity = UrlEntityCreationHelper.create(urlIdentifier, originalUrl, shortenedUrl)

        whenever(repository.findById(urlIdentifier)).thenReturn(Optional.of(urlEntity))

        assertDoesNotThrow {
            val result = urlService.getOriginalUrl(urlIdentifier)
            assertEquals(originalUrl, result)
        }

        verify(repository, times(1)).findById(urlIdentifier)
    }


    @Test
    fun `SHOULD delete URL successfully`() {
        val urlIdentifier = UUID.randomUUID().toString()

        doNothing().`when`(repository).deleteById(urlIdentifier)

        assertDoesNotThrow {
            urlService.deleteUrl(urlIdentifier)
        }

        verify(repository, times(1)).deleteById(urlIdentifier)
    }


    @Test
    fun `SHOULD NOT delete URL if not found`() {
        val urlIdentifier = UUID.randomUUID().toString()

        whenever(repository.deleteById(urlIdentifier)).thenThrow(EmptyResultDataAccessException("No entity found for deletion", 1))

        assertThrows<UrlShorteningException> {
            urlService.deleteUrl(urlIdentifier)
        }

        verify(repository, times(1)).deleteById(urlIdentifier)
    }
}