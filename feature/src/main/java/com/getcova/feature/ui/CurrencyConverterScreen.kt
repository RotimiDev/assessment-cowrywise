package com.getcova.feature.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.getcova.core.model.CurrencyConversionResponse
import com.getcova.core.network.ApiResult
import com.getcova.feature.CurrencyConverterViewModel
import com.getcova.feature.R

@Preview(showBackground = true)
@Composable
fun CurrencyCalculatorPreview() {
    MaterialTheme {
        CurrencyConverterScreen()
    }
}

@Composable
fun CurrencyConverterScreen(
    viewModel: CurrencyConverterViewModel = hiltViewModel()
) {
    val conversionResult by viewModel.conversionResult.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val fromCurrency by viewModel.fromCurrency.collectAsState()
    val toCurrency by viewModel.toCurrency.collectAsState()
    val isConverting by viewModel.isConverting.collectAsState()

    val currencySymbols = when (val result = viewModel.currencySymbols.collectAsState().value) {
        is ApiResult.Success -> result.data
        else -> emptyMap()
    }

    LaunchedEffect(Unit) {
        viewModel.getCurrencySymbols()
        viewModel.getLatestRates()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            TopBar()

            Spacer(modifier = Modifier.height(32.dp))

            TitleText(stringResource(id = R.string.currency))
            TitleText(stringResource(id = R.string.calculator))

            Spacer(modifier = Modifier.height(24.dp))

            AmountInput(
                amount = amount,
                currency = fromCurrency,
                symbol = currencySymbols[fromCurrency] ?: "",
                onAmountChange = { viewModel.updateAmount(it) },
                isReadOnly = false
            )

            AmountInput(
                amount = (conversionResult as? ApiResult.Success)?.data?.result?.toString() ?: "0",
                currency = toCurrency,
                symbol = "",
                onAmountChange = { viewModel.updateAmount(it) },
                isReadOnly = true
            )

            if (isConverting) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color(0xFF2D69DD)
                )
            } else {
                when (conversionResult) {
                    is ApiResult.Success -> {
                        val result = (conversionResult as ApiResult.Success<CurrencyConversionResponse>).data

                        AmountInput(
                            amount = result.result.toString(),
                            currency = toCurrency,
                            symbol = currencySymbols[toCurrency] ?: "",
                            onAmountChange = {},
                            isReadOnly = true
                        )
                    }
                    is ApiResult.Error -> {
                        val errorMessage = (conversionResult as ApiResult.Error).message

                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CurrencySelection(fromCurrency, toCurrency, currencySymbols)

            Spacer(modifier = Modifier.height(24.dp))

            ConvertButton(viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            ExchangeRateInfo()
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = stringResource(id = R.string.menu),
            tint = Color(0xFF4CD080),
        )

        TextButton(
            onClick = { },
            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4CD080)),
        ) {
            Text(
                text = stringResource(id = R.string.sign_up),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        color = Color(0xFF2D69DD),
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun AmountInput(
    amount: String,
    currency: String,
    symbol: String,
    onAmountChange: (String) -> Unit,
    isReadOnly: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFAFAFA),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isReadOnly) {
                Text(
                    text = "$symbol$amount",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                BasicTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(100.dp)
                )
            }

            Text(text = "$currency ($symbol)", fontSize = 18.sp, color = Color.Gray)
        }
    }
}

@Composable
fun CurrencySelection(fromCurrency: String, toCurrency: String, currencySymbols: Map<String, String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        CurrencySelector(
            currency = fromCurrency,
            symbol = currencySymbols[fromCurrency] ?: "",
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier.padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_swap),
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(Color(0xFFD3D3D3)),
                contentDescription = null,
            )
        }

        CurrencySelector(
            currency = toCurrency,
            symbol = currencySymbols[toCurrency] ?: "",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CurrencySelector(currency: String, symbol: String, modifier: Modifier) {
    Surface(
        modifier = modifier.padding(horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        border = ButtonDefaults.outlinedButtonBorder,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "$currency ($symbol)", fontSize = 16.sp, fontWeight = FontWeight.Medium)

            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }
    }
}

@Composable
fun ConvertButton(viewModel: CurrencyConverterViewModel) {
    Button(
        onClick = { viewModel.convertCurrency() },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF50E3A4)),
    ) {
        Text(
            text = stringResource(id = R.string.convert),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
        )
    }
}

@Composable
fun ExchangeRateInfo() {
    val showBottomSheet = remember { mutableStateOf(false) }

    if (showBottomSheet.value) {
        TimeSeriesBottomSheetView(onDismiss = { showBottomSheet.value = false })
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showBottomSheet.value = true }
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.mid_market_rate),
            fontSize = 14.sp,
            color = Color(0xFF2D69DD),
        )

        Spacer(modifier = Modifier.width(4.dp))

        Surface(
            modifier = Modifier.size(24.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.LightGray.copy(alpha = 0.3f),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(id = R.string.information),
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF4A90E2),
                )
            }
        }
    }
}
