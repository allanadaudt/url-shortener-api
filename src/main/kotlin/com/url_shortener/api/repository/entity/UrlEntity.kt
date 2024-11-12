package com.url_shortener.api.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "url_shortener")
@Entity
data class UrlEntity(
    @Id
    val urlIdentifier: String,

    @Column(name = "original_url", nullable = false, length = 1028)
    val originalUrl: String,

    @Column(name = "shortened_url", nullable = false, length = 1028)
    val shortenedUrl: String
) {
}