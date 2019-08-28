package com.example

import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable

open class Service {

    @Throws(BusinessLogicException::class)
    open fun callRetryableMethod(): String {
        retryable()
        return "success"
    }

    @Retryable(RuntimeException::class, maxAttempts = 3, backoff = Backoff(delay = 1000, maxDelay = 10000))
    @Throws(BusinessLogicException::class)
    open fun retryable(): String {
        mayThrowException()
        return "success"
    }

    // spring さんが作る proxy クラスを spy してしまわないようにこいつに例外を吐かせる
    @Throws(BusinessLogicException::class)
    open fun mayThrowException(): String {
        return "success"
    }

    sealed class BusinessLogicException(message: String) : Exception(message) {

        object ContractUnCompleted : BusinessLogicException("contract uncompleted")
    }

}
