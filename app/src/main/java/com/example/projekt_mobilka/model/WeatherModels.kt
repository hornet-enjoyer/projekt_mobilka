package com.example.projekt_mobilka.model

import com.google.gson.annotations.SerializedName

data class Capital(
    val name: String,
    val latitude: Double,
    val longitude: Double
)

data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("relative_humidity_2m") val humidity: Int,
    @SerializedName("wind_speed_10m") val windSpeed: Double,
    @SerializedName("weather_code") val weatherCode: Int
)

data class Guess(
    val cityName: String,
    val tempStatus: ComparisonStatus,
    val windStatus: ComparisonStatus,
    val humidityStatus: ComparisonStatus
)

enum class ComparisonStatus {
    CORRECT, TOO_HIGH, TOO_LOW
}

enum class Difficulty(val label: String, val lives: Int) {
    EASY("Łatwy", 10),
    MEDIUM("Średni", 5),
    HARD("Trudny", 3);

    companion object {
        fun fromLabel(label: String): Difficulty {
            return entries.find { it.label == label } ?: EASY
        }
    }
}

val capitalCities = listOf(
    Capital("Warszawa", 52.2297, 21.0122),
    Capital("Berlin", 52.5200, 13.4050),
    Capital("Paryż", 48.8566, 2.3522),
    Capital("Londyn", 51.5074, -0.1278),
    Capital("Madryt", 40.4168, -3.7038),
    Capital("Rzym", 41.9028, 12.4964),
    Capital("Ateny", 37.9838, 23.7275),
    Capital("Praga", 50.0755, 14.4378),
    Capital("Wiedeń", 48.2082, 16.3738),
    Capital("Sztokholm", 59.3293, 18.0686),
    Capital("Oslo", 59.9139, 10.7522),
    Capital("Helsinki", 60.1695, 24.9354),
    Capital("Lizbona", 38.7223, -9.1393),
    Capital("Kopenhaga", 55.6761, 12.5683),
    Capital("Amsterdam", 52.3676, 4.9041),
    Capital("Bruksela", 50.8503, 4.3517),
    Capital("Vaduz", 47.1410, 9.5209),
    Capital("Luksemburg", 49.6116, 6.1319),
    Capital("Andora", 42.5063, 1.5218),
    Capital("Monako", 43.7384, 7.4246),
    Capital("San Marino", 43.9424, 12.4578),
    Capital("Watykan", 41.9029, 12.4534),
    Capital("Bratysława", 48.1486, 17.1077),
    Capital("Lublana", 46.0569, 14.5058),
    Capital("Zagrzeb", 45.8150, 15.9819),
    Capital("Sarajewo", 43.8563, 18.4131),
    Capital("Belgrad", 44.7866, 20.4489),
    Capital("Podgorica", 42.4304, 19.2594),
    Capital("Tirana", 41.3275, 19.8187),
    Capital("Skopje", 41.9973, 21.4280),
    Capital("Sofia", 42.6977, 23.3219),
    Capital("Bukareszt", 44.4268, 26.1025),
    Capital("Budapeszt", 47.4979, 19.0402),
    Capital("Kiszyniów", 47.0105, 28.8638),
    Capital("Kijów", 50.4501, 30.5234),
    Capital("Mińsk", 53.9006, 27.5590),
    Capital("Wilno", 54.6872, 25.2797),
    Capital("Ryga", 56.9496, 24.1052),
    Capital("Tallinn", 59.4370, 24.7536),
    Capital("Reykjavík", 64.1265, -21.8174),
    Capital("Dublin", 53.3498, -6.2603),
    Capital("Nikozja", 35.1856, 33.3823),
    Capital("Valletta", 35.8989, 14.5146)
)
