package com.example.somativan2.data.simulador

object ConversorUnidades {

    fun formatarTemperatura(celsius: Int, unidade: String): String {
        val valor = if (unidade == "F") celsius * 9 / 5 + 32 else celsius
        val simbolo = if (unidade == "F") "°F" else "°C"
        return "$valor$simbolo"
    }

    fun formatarVento(kmh: Int, unidade: String): String {
        val valor = if (unidade == "MPH") (kmh * 0.621371).toInt() else kmh
        val simbolo = if (unidade == "MPH") "mph" else "km/h"
        return "$valor $simbolo"
    }
}
