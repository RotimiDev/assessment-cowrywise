package com.getcova.feature.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getcova.feature.R

@Preview(showBackground = true)
@Composable
private fun TimeSeriesBottomSheetViewPreview() {
    TimeSeriesBottomSheetView(onDismiss = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSeriesBottomSheetView(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color(0xFF2962FF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp, max = 400.dp)
                .padding(24.dp)
        ) {
            TimePeriodSelector()
            Spacer(modifier = Modifier.height(24.dp))
            ExchangeRateChart()
            Spacer(modifier = Modifier.height(20.dp))
            DateLabels()
            Spacer(modifier = Modifier.weight(1f))
            RateAlertCTA()
        }
    }
}

@Composable
fun TimePeriodSelector() {
    Row(modifier = Modifier.fillMaxWidth()) {
        TimePeriodOption(
            text = stringResource(id = R.string.past_30_days),
            isSelected = true
        )
        Spacer(modifier = Modifier.width(24.dp))
        TimePeriodOption(
            text = stringResource(id = R.string.past_90_days),
            isSelected = false
        )
    }
}

@Composable
fun TimePeriodOption(text: String, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = text,
            style = TextStyle(
                color = Color.White.copy(alpha = if (isSelected) 1f else 0.6f),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(if (isSelected) Color(0xFF4CD964) else Color.Transparent, CircleShape)
        )
    }
}

@Composable
fun ExchangeRateChart() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        ExchangeRateTooltip()
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val points = listOf(
                Offset(0f, height * 0.7f),
                Offset(width * 0.3f, height * 0.5f),
                Offset(width * 0.6f, height * 0.6f),
                Offset(width, height * 0.3f)
            )

            val linePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.forEach { lineTo(it.x, it.y) }
            }

            drawPath(
                path = linePath,
                color = Color(0xFF4CD964),
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            drawCircle(
                color = Color.White,
                radius = 8.dp.toPx(),
                center = points[1]
            )
            drawCircle(
                color = Color(0xFF4CD964),
                radius = 5.dp.toPx(),
                center = points[1]
            )
        }
    }
}

@Composable
fun ExchangeRateTooltip() {
    Surface(
        modifier = Modifier
            .offset(y = 20.dp, x = (-20).dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF4CD964)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "15 Jun", style = TextStyle(color = Color.White, fontSize = 12.sp))
            Text(
                text = stringResource(id = R.string.exchange_rate, "1 EUR", "4.242"),
                style = TextStyle(color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            )
        }
    }
}

@Composable
fun DateLabels() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("01 Jun", "07 Jun", "15 Jun", "23 Jun", "30 Jun").forEach {
            Text(
                text = it,
                style = TextStyle(
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
fun RateAlertCTA() {
    Text(
        text = stringResource(id = R.string.rate_alerts),
        style = TextStyle(
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
