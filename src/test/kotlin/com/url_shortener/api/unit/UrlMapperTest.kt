package com.url_shortener.api.unit

import com.url_shortener.api.mapper.UrlMapper
import com.url_shortener.api.repository.entity.UrlEntity
import com.url_shortener.api.to.OriginalUrlTO
import com.url_shortener.api.to.ShortenedUrlTO
import com.url_shortener.api.utils.OriginalUrlTOCreationHelper
import com.url_shortener.api.utils.ShortenedUrlTOCreationHelper
import com.url_shortener.api.utils.UrlEntityCreationHelper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class UrlMapperTest {

    @Mock
    private lateinit var urlMapper : UrlMapper

    @Test
    fun `should map OriginalUrlTO to UrlEntity`() {
        val urlIdentifier = UUID.randomUUID().toString()
        val originalUrl = "http://original.url"
        val shortenedUrl = "http://short.url/$urlIdentifier"

        val originalUrlTO = OriginalUrlTOCreationHelper.create(urlIdentifier, originalUrl)

        val expectedEntity = UrlEntityCreationHelper.create(urlIdentifier, originalUrl, shortenedUrl)

        whenever(urlMapper.toEntity(originalUrlTO)).thenReturn(expectedEntity)

        val entity = urlMapper.toEntity(originalUrlTO)

        assertEquals(urlIdentifier, entity.urlIdentifier)
        assertEquals(originalUrl, entity.originalUrl)
        assertEquals(shortenedUrl, entity.shortenedUrl)
    }

    @Test
    fun `should map UrlEntity to ShortenedUrlTO`() {
        val urlIdentifier = UUID.randomUUID().toString()
        val originalUrl = "http://original.url"
        val shortenedUrl = "http://short.url/$urlIdentifier"

        val urlEntity = UrlEntityCreationHelper.create(
            urlIdentifier = urlIdentifier,
            originalUrl = originalUrl,
            shortenedUrl = shortenedUrl
        )

        val expectedShortenedUrlTO = ShortenedUrlTOCreationHelper.create(shortenedUrl)

        whenever(urlMapper.mapToShortUrl(urlEntity)).thenReturn(expectedShortenedUrlTO)

        val shortenedUrlTO = urlMapper.mapToShortUrl(urlEntity)

        assertEquals(shortenedUrl, shortenedUrlTO.shortenedUrl)
    }
}