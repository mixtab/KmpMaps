package org.m_tabarkevych.kmpmaps.features.core.domain

enum class DataError: Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERVER,
    SERIALIZATION,
    UNKNOWN
}