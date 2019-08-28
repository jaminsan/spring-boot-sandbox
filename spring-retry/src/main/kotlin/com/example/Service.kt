package com.example

import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable

open class Service {

    open fun callRetryableMethod(): String {
        retryableMethod()
        return "success"
    }

    @Retryable(BaseException::class, maxAttempts = 3, backoff = Backoff(delay = 1000, maxDelay = 10000))
    open fun retryableMethod(): String {
        mayThrowException()
        return "success"
    }

    // spring が作る proxy クラスを spy してしまわないようにこいつに例外を吐かせる
    open fun mayThrowException(): String {
        return "success"
    }

    // mockito で thenThrow するときに検査例外を投げられない？
    sealed class BaseException(mesasge: String, cause: Throwable?) : RuntimeException() {

        object PersistFailed : BaseException("persist failed", null)
    }

}
