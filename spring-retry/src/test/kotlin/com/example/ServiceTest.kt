package com.example

import com.example.Service.BusinessLogicException.ContractUnCompleted
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@SpringBootTest
class ServiceTest {

    @SpyBean
    lateinit var service: Service

    @Test
    fun retryableMethod_shouldBeSuccess_whenCalledFromOtherObject() {
        `when`(service.mayThrowException())
            .thenAnswer { throw RuntimeException("persist failed") }
            .thenAnswer { throw RuntimeException("persist failed") }
            .thenReturn("success")

        assertTrue(service.retryable() == "success")

        verify(service, times(3)).retryable()
        verify(service, times(3)).mayThrowException()
    }

    @Test
    fun retryableMethod_shouldRetryUntilMaxAttempts_whenCalledFromOtherObject() {
        `when`(service.mayThrowException()).thenAnswer { throw RuntimeException("persist failed") }

        assertFailsWith(RuntimeException::class) {
            service.retryable()
        }

        verify(service, times(3)).retryable()
    }

    @Test
    fun retryableMethod_shouldNotRetryThenThrowException_whenCalledFromSameObject() {
        `when`(service.mayThrowException()).thenThrow(RuntimeException("persist failed"))

        assertFailsWith(RuntimeException::class) {
            service.callRetryableMethod()
        }

        verify(service, times(1)).retryable()
        verify(service, times(1)).mayThrowException()
    }

    @Test
    fun retryableMethod_shouldNotRetry_whenReceiveUnRetryableExceptionType() {
        `when`(service.mayThrowException()).thenAnswer { throw ContractUnCompleted }

        assertFailsWith(ContractUnCompleted::class) {
            service.retryable()
        }

        verify(service, times(1)).retryable()
        verify(service, times(1)).mayThrowException()
    }

}