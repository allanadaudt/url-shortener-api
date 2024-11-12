package com.url_shortener.api.controller

import com.url_shortener.api.service.UrlService
import com.url_shortener.api.to.ShortenedUrlTO
import com.url_shortener.api.to.OriginalUrlTO
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/shorten")
class UrlController(
    private val service: UrlService
) {

    @PostMapping
    fun createShortenedUrl(@Valid @RequestBody request: OriginalUrlTO): ResponseEntity<ShortenedUrlTO> {
        val url = service.createShortUrl(request)
        return ResponseEntity.ok(url)
    }

    @GetMapping
    fun listUrls(): ResponseEntity<List<ShortenedUrlTO>> {
        return ResponseEntity.ok(service.getAllUrls())
    }

    @DeleteMapping("/{id}")
    fun deleteUrl(@PathVariable id: String): ResponseEntity<Void> {
        service.deleteUrl(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/convert/{id}")
    fun getOriginalUrl(@PathVariable id: String): ResponseEntity<String> {
        val originalUrl = service.getOriginalUrl(id)
        return if (originalUrl != null) {
            ResponseEntity.ok(originalUrl)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}