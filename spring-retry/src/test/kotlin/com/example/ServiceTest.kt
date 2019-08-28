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
    fun retryableMethod_callFromExternal_success() {
        `when`(service.mayThrowExceptionForMock())
            .thenThrow(PersistFailed)
            .thenThrow(PersistFailed)
            .thenReturn("success")

        service.mayThrowException()

        verify(service, times(3)).mayThrowException()
        verify(service, times(3)).mayThrowExceptionForMock()
    }

    @Test
    fun retryableMethod_callFromExternal_canRetryUntilMaxAttempts() {
        `when`(service.mayThrowExceptionForMock()).thenThrow(PersistFailed)

        assertFailsWith(PersistFailed::class) {
            service.mayThrowException()
        }

        verify(service, times(3)).mayThrowException()
    }

    @Test
    fun retryableMethod_callFromInternal_canNotRetryThenThrowException() {
        `when`(service.mayThrowExceptionForMock()).thenThrow(PersistFailed)

        assertFailsWith(PersistFailed::class) {
            service.callThrowableMethodInternal()
        }

        verify(service, times(1)).mayThrowException()
        verify(service, times(1)).mayThrowExceptionForMock()
    }

}