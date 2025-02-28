package com.getcova.feature

import app.cash.turbine.test
import com.getcova.core.common.CurrencyRepository
import com.getcova.core.network.ApiResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyConverterViewModelTest {

    // Mocks
    private lateinit var repository: CurrencyRepository
    private lateinit var viewModel: CurrencyConverterViewModel

    // Test Scope
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        repository = mockk(relaxed = true) // Mock repository
        viewModel = CurrencyConverterViewModel(repository)
    }

    @Test
    fun `getCurrencySymbols updates state correctly`() = runTest {
        val fakeSymbols = mapOf("USD" to "United States Dollar", "EUR" to "Euro")
        coEvery { repository.getCurrencySymbols() } returns ApiResult.Success(fakeSymbols)

        viewModel.getCurrencySymbols()

        viewModel.currencySymbols.test {
            assertEquals(ApiResult.Loading, awaitItem())
            assertEquals(ApiResult.Success(fakeSymbols), awaitItem())
        }
    }

    @Test
    fun `updateAmount updates amount flow`() =
        runTest {
            viewModel.updateAmount("500")

            viewModel.amount.test {
                assertEquals("500", awaitItem())
            }
        }
}
