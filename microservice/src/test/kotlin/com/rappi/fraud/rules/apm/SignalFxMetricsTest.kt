package com.rappi.fraud.rules.apm

import com.codahale.metrics.Counter
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.signalfx.codahale.SfxMetrics
import org.junit.jupiter.api.Test

class SignalFxMetricsTest {
    @Test
    fun `test signalFxMetrics`() {
        val config = SignalFxMetrics.Config("dev", "mockedurl", "test", 1, setOf("mk_payer", "mk_payer_address"))
        val sfxMetrics = mock<SfxMetrics>()
        val signalFxMetrics = SignalFxMetrics(sfxMetrics, config)

        whenever(sfxMetrics.counter(any<String>(), any<Map<String,String>>())).thenReturn(mock<Counter>())
        signalFxMetrics.reportMissingFields(setOf("mk_payer_address_approved_qty_1d"), "an_event", "dev")

       verify(sfxMetrics).counter("fraud_rules_engine_missing_fields",
            mapOf("event" to "an_event", "field_name" to "mk_payer_address", "country" to "dev", "env" to "dev")
        )
    }

    @Test
    fun `test production signalFxMetrics`() {
        val config = SignalFxMetrics.Config("co", "mockedurl", "test", 1, setOf("mk_payer", "mk_payer_address"))
        val sfxMetrics = mock<SfxMetrics>()
        val signalFxMetrics = SignalFxMetrics(sfxMetrics, config)

        whenever(sfxMetrics.counter(any<String>(), any<Map<String,String>>())).thenReturn(mock<Counter>())
        signalFxMetrics.reportMissingFields(setOf("mk_payer_address_approved_qty_1d"), "an_event", "dev")

        verify(sfxMetrics).counter("fraud_rules_engine_missing_fields",
            mapOf("event" to "an_event", "field_name" to "mk_payer_address", "country" to "dev", "env" to "prod")
        )
    }
}