
package org.m_tabarkevych.kmpmaps.features.core.domain

sealed interface DomainResult<out D, out E: Error> {
    data class Success<out D>(val data: D): DomainResult<D, Nothing>
    data class Error<out E: org.m_tabarkevych.kmpmaps.features.core.domain.Error>(val error: E):
        DomainResult<Nothing, E>
}

inline fun <T, E: Error, R> DomainResult<T, E>.map(map: (T) -> R): DomainResult<R, E> {
    return when(this) {
        is DomainResult.Error -> DomainResult.Error(error)
        is DomainResult.Success -> DomainResult.Success(map(data))
    }
}

fun <T, E: Error> DomainResult<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}

inline fun <T, E: Error> DomainResult<T, E>.onSuccess(action: (T) -> Unit): DomainResult<T, E> {
    return when(this) {
        is DomainResult.Error -> this
        is DomainResult.Success -> {
            action(data)
            this
        }
    }
}
inline fun <T, E: Error> DomainResult<T, E>.onError(action: (E) -> Unit): DomainResult<T, E> {
    return when(this) {
        is DomainResult.Error -> {
            action(error)
            this
        }
        is DomainResult.Success -> this
    }
}

typealias EmptyResult<E> = DomainResult<Unit, E>
