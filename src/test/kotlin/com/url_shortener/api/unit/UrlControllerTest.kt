package com.url_shortener.api.unit

import com.url_shortener.api.controller.UrlController
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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class UrlControllerTest {

    @Mock
    private lateinit var service: UrlService

    @InjectMocks
    private lateinit var urlController: UrlController

    @Test
    fun `SHOULD create short URL successfully`() {
        val urlIdentifier = UUID.randomUUID().toString()
        val originalUrl = "http://original.url"
        val shortenedUrl = "http://short.url/$urlIdentifier"

        val originalUrlTO = OriginalUrlTOCreationHelper.create(urlIdentifier, originalUrl)
        val shortenedUrlTO = ShortenedUrlTOCreationHelper.create(shortenedUrl)

        whenever(service.createShortUrl(originalUrlTO)).thenReturn(shortenedUrlTO)

        assertDoesNotThrow {
            val result: ResponseEntity<ShortenedUrlTO> = urlController.createShortenedUrl(originalUrlTO)

            assert(result.statusCode.value() == 200)
            assert(result.body?.shortenedUrl == shortenedUrl)
        }

        verify(service, times(1)).createShortUrl(originalUrlTO)
    }

    @Test
    fun `SHOULD list all short URLs successfully`() {
        val urlIdentifier = UUID.randomUUID().toString()
        val shortenedUrl = "http://short.url/$urlIdentifier"

        val shortenedUrlTO = ShortenedUrlTOCreationHelper.create(shortenedUrl)

        whenever(service.getAllUrls()).thenReturn(listOf(shortenedUrlTO))

        assertDoesNotThrow {
            val result: ResponseEntity<List<ShortenedUrlTO>> = urlController.listUrls()

            assert(result.statusCode.value() == 200)
            assert(result.body?.size == 1)
            assert(result.body?.get(0)?.shortenedUrl == "http://short.url/$urlIdentifier")
        }

        verify(service, times(1)).getAllUrls()
    }

    @Test
    fun `SHOULD delete URL successfully`() {
        val urlIdentifier = UUID.randomUUID().toString()

        whenever(service.deleteUrl(urlIdentifier)).then { Unit }

        assertDoesNotThrow {
            val result: ResponseEntity<Void> = urlController.deleteUrl(urlIdentifier)

            assert(result.statusCode.value() == 204)
        }

        verify(service, times(1)).deleteUrl(urlIdentifier)
    }

    @Test
    fun `SHOULD return original URL successfully`() {
        val urlIdentifier = UUID.randomUUID().toString()
        val originalUrl = "http://original.url"

        whenever(service.getOriginalUrl(urlIdentifier)).thenReturn(originalUrl)

        assertDoesNotThrow {
            val result: ResponseEntity<String> = urlController.getOriginalUrl(urlIdentifier)

            assert(result.statusCode.value() == 200)
            assert(result.body == originalUrl)
        }

        verify(service, times(1)).getOriginalUrl(urlIdentifier)}

    @Test
    fun `SHOULD return 404 when original URL not found`() {
        val urlIdentifier = UUID.randomUUID().toString()

        whenever(service.getOriginalUrl(urlIdentifier)).thenReturn(null)

        assertDoesNotThrow {
            val result: ResponseEntity<String> = urlController.getOriginalUrl(urlIdentifier)

            assert(result.statusCode.value() == 404)
        }

        verify(service, times(1)).getOriginalUrl(urlIdentifier)
    }
}
