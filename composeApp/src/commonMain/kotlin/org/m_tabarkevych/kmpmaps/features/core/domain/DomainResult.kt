
package org.m_tabarkevych.kmpmaps.features.core.domain

sealed class DomainResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : DomainResult<T>()
    data class Failure(val domainError: DataError) : DomainResult<Nothing>()
}

val <T : Any> DomainResult<T>.isSuccess
    get() = this is DomainResult.Success

val <T : Any> DomainResult<T>.isFailure
    get() = this is DomainResult.Failure

inline fun <T : Any> DomainResult<T>.onSuccess(block: (T) -> Unit): DomainResult<T> {
    if (isSuccess) block((this as DomainResult.Success<T>).data)
    return this
}

inline fun <T : Any> DomainResult<T>.onFailure(block: (DataError) -> Unit): DomainResult<T> {
    if (isFailure) block((this as DomainResult.Failure).domainError)
    return this
}

inline fun <T : Any, R : Any> DomainResult<T>.map(transform: (T) -> R): DomainResult<R> {
    return when (this) {
        is DomainResult.Success -> DomainResult.Success(transform(this.data))
        is DomainResult.Failure -> this
    }
}

inline fun <T : Any> DomainResult<T>.getOr(block: (error: DomainResult.Failure) -> T): T {
    return when (this) {
        is DomainResult.Failure -> block(this)
        is DomainResult.Success -> this.data
    }
}

fun <T : Any> DomainResult<T>.getOrNull(): T? {
    return when (this) {
        is DomainResult.Failure -> null
        is DomainResult.Success -> this.data
    }
}

fun <R : Any, T : R> DomainResult<T>.getOrDefault(defaultValue: R): R {
    if (isFailure) return defaultValue
    return (this as DomainResult.Success<T>).data
}
