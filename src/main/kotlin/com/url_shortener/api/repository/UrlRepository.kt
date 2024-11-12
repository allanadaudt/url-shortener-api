package com.url_shortener.api.repository

import com.url_shortener.api.repository.entity.UrlEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UrlRepository : JpaRepository<UrlEntity, String> {

    fun findByOriginalUrl(originalUrl: String): UrlEntity?
}