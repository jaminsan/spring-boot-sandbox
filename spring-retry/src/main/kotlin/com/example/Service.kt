package com.example

import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable

open class Service {

    @Throws(BaseException::class)
    open fun callRetryableMethod(): String {
        retryable()
        return "success"
    }

    @Retryable(BaseException::class, maxAttempts = 3, backoff = Backoff(delay = 1000, maxDelay = 10000))
    @Throws(BaseException::class)
    open fun retryable(): String {
        mayThrowException()
        return "success"
    }

    // spring さんが作る proxy クラスを spy してしまわないようにこいつに例外を吐かせる
    @Throws(BaseException::class)
    open fun mayThrowException(): String {
        return "success"
    }

    sealed class BaseException(message: String) : Exception(message) {

        object PersistFailed : BaseException("persist failed")
    }

}
