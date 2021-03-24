package com.rappi.fraud.rules.entities

interface DocumentDbRepository {
    val collection: String
    val indexes: Set<DocumentDbIndex>
}

data class DocumentDbIndex(
    val name: String,
    val key: String,
    val background: Boolean = true
)
