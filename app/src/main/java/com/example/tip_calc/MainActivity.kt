package com.example.tip_calc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tip_calc.ui.theme.Tip_CalcTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Slider
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tip_CalcTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DemoScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DemoTextPreview() {
    Tip_CalcTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DemoScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun DemoSlider(sliderPosition: Float, onPositionChange: (Float) -> Unit) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = "Чаевые:",
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Slider(
            valueRange = 0f..25f,
            steps = 4, // Шаг изменения 5% (0-5-10-15-20-25)
            value = sliderPosition,
            onValueChange = { onPositionChange(it) }
        )
    }
}

@Composable
fun DemoScreen(modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var order by remember { mutableStateOf("") }
    var dishCount by remember { mutableStateOf("") }
    var sale by remember { mutableIntStateOf(0) }
    var showTotal by remember { mutableStateOf(false) } // Состояние для отображения итога

    // Расчёт итоговой суммы
    val orderCheck = order.toDoubleOrNull() ?: 0.0
    val saleAmount = orderCheck * sale / 100
    val finalCheck = orderCheck - saleAmount
    val tipsAmount = finalCheck * sliderPosition / 100
    val totalAmount = finalCheck + tipsAmount

    // Функция для расчета скидки на основе количества блюд
    val calculateAutoSale = {
        val count = dishCount.toIntOrNull() ?: 0
        sale = when {
            count > 10 -> 10  // более 10 блюд – 10%
            count >= 6 -> 7   // 6-10 блюд – 7%
            count >= 3 -> 5   // 3-5 блюд – 5%
            count >= 1 -> 3   // 1-2 блюда – 3%
            else -> 0
        }
    }

    val handlePositionChange = { position: Float ->
        sliderPosition = position
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Калькулятор скидок и чаевых",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Сумма заказа (розовое поле)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(
                text = "Сумма заказа:",
                fontSize = 22.sp,
                modifier = Modifier.width(150.dp)
            )
            TextField(
                value = order,
                onValueChange = {
                    order = it
                    calculateAutoSale()
                    showTotal = false // Сбрасываем отображение итога при изменении
                },
                modifier = Modifier.width(190.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFE4EC), // Розовый цвет
                    unfocusedContainerColor = Color(0xFFFFE4EC), // Розовый цвет
                    focusedIndicatorColor = Color.Magenta,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Количество блюд (розовое поле)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(
                text = "Количество блюд:",
                fontSize = 22.sp,
                modifier = Modifier.width(150.dp)
            )
            TextField(
                value = dishCount,
                onValueChange = {
                    dishCount = it
                    calculateAutoSale()
                    showTotal = false // Сбрасываем отображение итога при изменении
                },
                modifier = Modifier.width(150.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFE4EC), // Розовый цвет
                    unfocusedContainerColor = Color(0xFFFFE4EC), // Розовый цвет
                    focusedIndicatorColor = Color.Magenta,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Слайдер для чаевых (0-25% с шагом 5)
        DemoSlider(
            sliderPosition = sliderPosition,
            onPositionChange = {
                handlePositionChange(it)
                showTotal = false // Сбрасываем отображение итога при изменении
            }
        )

        Text(
            style = MaterialTheme.typography.headlineMedium,
            text = "${sliderPosition.toInt()}%"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // СКИДКА слева и радиокнопки справа
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "СКИДКА",
                fontSize = 22.sp,
                modifier = Modifier.padding(end = 16.dp)
            )

            val discountOptions = listOf(3, 5, 7, 10)

            // Горизонтальный ряд для радиокнопок (только отображение, без возможности выбора)
            discountOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = sale == option,
                        onClick = null  // Отключаем возможность ручного выбора
                    )
                    Text(
                        text = "$option%",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 1.dp, end = 10.dp)
                    )
                }
            }
        }

        // Подпись с объяснением расчета скидки
        Text(
            text = when (sale) {
                10 -> "Более 10 блюд - скидка 10%"
                7 -> "6-10 блюд - скидка 7%"
                5 -> "3-5 блюд - скидка 5%"
                3 -> "1-2 блюда - скидка 3%"
                else -> "Нет блюд - скидка 0%"
            },
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Итого"
        Button(
            onClick = { showTotal = true },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = "Итого",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение скидки или итоговой суммы
        Text(
            text = if (showTotal) "Итоговая сумма:" else "Скидка:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (showTotal) {
                "${"%.2f".format(totalAmount)}"
            } else {
                "${sale}% (${"%.2f".format(saleAmount)})"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (showTotal) Color.Blue else Color.Black
        )
    }
}