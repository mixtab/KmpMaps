package org.m_tabarkevych.kmpmaps.features.core.data.datastore

import org.m_tabarkevych.kmpmaps.features.core.domain.DataError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T : Any> safeCall(
    execute: () -> HttpResponse
): DomainResult<T> {
    val response = try {
        execute()
    } catch(e: SocketTimeoutException) {
        return DomainResult.Failure(DataError.REQUEST_TIMEOUT)
    } catch(e: UnresolvedAddressException) {
        return DomainResult.Failure(DataError.NO_INTERNET)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return DomainResult.Failure(DataError.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T: Any> responseToResult(
    response: HttpResponse
): DomainResult<T> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                DomainResult.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                DomainResult.Failure(DataError.SERIALIZATION)
            }
        }
        408 ->  DomainResult.Failure(DataError.REQUEST_TIMEOUT)
        429 -> DomainResult.Failure(DataError.TOO_MANY_REQUESTS)
        in 500..599 ->  DomainResult.Failure(DataError.SERVER)
        else ->  DomainResult.Failure(DataError.UNKNOWN)
    }
}


