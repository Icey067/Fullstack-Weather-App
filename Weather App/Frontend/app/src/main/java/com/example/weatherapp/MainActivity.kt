package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

data class WeatherData(
    val city: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val aqi: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherScreen() {
    var city by remember { mutableStateOf("London") }
    var weatherData by remember { mutableStateOf<WeatherData?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    fun fetchWeather() {
        if (city.isBlank()) {
            errorMessage = "Please enter a city name."
            return
        }
        isLoading = true
        errorMessage = null
        weatherData = null

        scope.launch {
            try {
                // Make sure this IP is still correct!
                val apiUrl = "http://192.168.1.14:5000/weather?city=${city.trim()}"
                val response = withContext(Dispatchers.IO) { URL(apiUrl).readText() }
                val json = JSONObject(response)

                if (json.has("error")) {
                    errorMessage = json.getString("error")
                } else {
                    val data = WeatherData(
                        city = json.getString("city"),
                        temperature = json.getDouble("temperature"),
                        description = json.getString("description").replaceFirstChar { it.uppercase() },
                        humidity = json.getInt("humidity"),
                        aqi = json.getString("aqi_description")
                    )
                    weatherData = data
                }

            } catch (e: Exception) {
                errorMessage = "Failed to connect to server: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Weather App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter City Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { fetchWeather() },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Fetching..." else "Get Weather")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            weatherData != null -> {
                WeatherInfoCard(data = weatherData!!)
            }
            errorMessage != null -> {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun WeatherInfoCard(data: WeatherData) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = data.city, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${data.temperature}°C", fontSize = 48.sp, fontWeight = FontWeight.Light)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = data.description, fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InfoItem(label = "Humidity", value = "${data.humidity}%")
                InfoItem(label = "AQI", value = data.aqi)
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}