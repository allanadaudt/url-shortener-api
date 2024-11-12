package com.url_shortener.api.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.url_shortener.api.exception.UrlShorteningException
import com.url_shortener.api.repository.UrlRepository
import com.url_shortener.api.repository.entity.UrlEntity
import com.url_shortener.api.service.UrlService
import com.url_shortener.api.to.OriginalUrlTO
import jakarta.servlet.ServletException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UrlShortenerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @SpyBean
    private lateinit var repository: UrlRepository

    @SpyBean
    private lateinit var service: UrlService

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun `WHEN creating a short URL, SHOULD return the shortened URL successfully`() {
        val urlIdentifier = UUID.randomUUID().toString()

        val request = OriginalUrlTO(
            originalUrl = "http://example.com",
            urlIdentifier = urlIdentifier
        )

        mockMvc.post("/shorten") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.shortened_url") { value("http://short.url/$urlIdentifier") }
        }

        verify(repository, times(1)).save(any())
        verify(service, times(1)).createShortUrl(any())
    }

    @Test
    fun `WHEN creating a short URL for an existing URL, SHOULD return the existing shortened URL`() {
        val originalUrl = "http://example.com"
        val urlIdentifier = UUID.randomUUID().toString()

        val existingUrlEntity = UrlEntity(urlIdentifier, originalUrl, "http://short.url/$urlIdentifier")
        repository.save(existingUrlEntity)

        val request = OriginalUrlTO(originalUrl = originalUrl, urlIdentifier = urlIdentifier)

        assertThrows<ServletException> {
            mockMvc.post("/shorten") {
                contentType = MediaType.APPLICATION_JSON
                content = jacksonObjectMapper().writeValueAsString(request)
            }.andExpect {
                content { string(eq("URL already exists: http://short.url/$urlIdentifier")) }
            }
        }

        // Asserts to ensure no new URL entry was saved
        val urlCount = repository.count()
        assert(urlCount.toInt() == 1)

        verify(service, times(1)).createShortUrl(any())
    }

    @Test
    fun `WHEN retrieving all URLs, SHOULD return a list of shortened URLs`() {
        val urlIdentifier = UUID.randomUUID().toString()

        repository.save(UrlEntity(urlIdentifier, "http://anotherexample.com", "http://short.url/$urlIdentifier"))

        mockMvc.get("/shorten") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].shortened_url") { value("http://short.url/$urlIdentifier") }
        }

        verify(service, times(1)).getAllUrls()
    }

    @Test
    fun `WHEN retrieving the original URL, SHOULD return the original URL`() {
        val urlIdentifier = UUID.randomUUID().toString()
        repository.save(UrlEntity(urlIdentifier, "http://example.com", "http://short.url/$urlIdentifier"))

        mockMvc.get("/shorten/convert/$urlIdentifier") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { string("http://example.com") }
        }

        verify(service, times(1)).getOriginalUrl(any())
    }

    @Test
    fun `WHEN deleting a URL, SHOULD remove the URL successfully`() {
        val urlIdentifier = UUID.randomUUID().toString()
        repository.save(UrlEntity(urlIdentifier, "http://example.com", "http://short.url/$urlIdentifier"))

        mockMvc.delete("/shorten/$urlIdentifier") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent() }
        }

        val url = repository.findById(urlIdentifier)
        assert(url.isEmpty)

        verify(service, times(1)).deleteUrl(any())
    }
}