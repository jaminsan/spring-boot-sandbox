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
    fun retryableMethod_shouldBeSuccess_whenCalledFromOtherObject() {
        `when`(service.mayThrowException())
            .thenAnswer { throw PersistFailed }
            .thenAnswer { throw PersistFailed }
            .thenReturn("success")

        service.retryable()

        verify(service, times(3)).retryable()
        verify(service, times(3)).mayThrowException()
    }

    @Test
    fun retryableMethod_shouldRetryUntilMaxAttempts_whenCalledFromOtherObject() {
        `when`(service.mayThrowException()).thenAnswer { throw PersistFailed }

        assertFailsWith(PersistFailed::class) {
            service.retryable()
        }

        verify(service, times(3)).retryable()
    }

    @Test
    fun retryableMethod_canNotRetryThenThrowException_whenCalledFromSameObject() {
        `when`(service.mayThrowException()).thenThrow(PersistFailed)

        assertFailsWith(PersistFailed::class) {
            service.callRetryableMethod()
        }

        verify(service, times(1)).retryable()
        verify(service, times(1)).mayThrowException()
    }

}