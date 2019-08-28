package com.example

import com.example.Service.BaseException.PersistFailed
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertFailsWith

@RunWith(SpringRunner::class)
@SpringBootTest
class ServiceTest {

    @SpyBean
    lateinit var service: Service

    @Test
    fun retryableMethod_success_whenCalledFromOtherObject() {
        `when`(service.mayThrowException())
            .thenThrow(PersistFailed)
            .thenThrow(PersistFailed)
            .thenReturn("success")

        service.retryableMethod()

        verify(service, times(3)).retryableMethod()
        verify(service, times(3)).mayThrowException()
    }

    @Test
    fun retryableMethod_canRetryUntilMaxAttempts_whenCalledFromOtherObject() {
        `when`(service.mayThrowException()).thenThrow(PersistFailed)

        assertFailsWith(PersistFailed::class) {
            service.retryableMethod()
        }

        verify(service, times(3)).retryableMethod()
    }

    @Test
    fun retryableMethod_canNotRetryThenThrowException_whenCalledFromSameObject() {
        `when`(service.mayThrowException()).thenThrow(PersistFailed)

        assertFailsWith(PersistFailed::class) {
            service.callThrowableMethodInternal()
        }

        verify(service, times(1)).retryableMethod()
        verify(service, times(1)).mayThrowException()
    }

}