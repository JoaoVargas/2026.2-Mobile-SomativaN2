package com.example.somativan2.data.simulador

import java.util.Calendar

// Único lugar do app que formata horas/dias em texto. Evita ter a mesma lógica
// repetida em cada tela que precisa mostrar um rótulo de hora ou de dia.
object FormatadorTempo {

    private val nomesDiasSemana = listOf(
        "Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"
    )

    // "Agora" para a hora atual, "%02dh" para as demais. Usado na lista horizontal da Home.
    fun rotuloHoraRelativa(offsetHoras: Long): String {
        if (offsetHoras == 0L) return "Agora"
        val calendario = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, offsetHoras.toInt()) }
        return String.format("%02dh", calendario.get(Calendar.HOUR_OF_DAY))
    }

    // "%02dh" calculado só pelo offset absoluto, sem depender do horário real do dispositivo.
    // Usado no nível "horas" do Histórico, onde o offset já representa um dia específico do passado.
    fun rotuloHoraAbsoluta(offsetHoras: Long): String {
        val horaDoDia = ((offsetHoras % 24) + 24) % 24
        return String.format("%02dh", horaDoDia)
    }

    // "Quinta-feira, 29/10". Usado nos cabeçalhos de dia da Previsão.
    fun rotuloDia(diaOffsetAbsoluto: Int): String {
        val calendario = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, diaOffsetAbsoluto) }
        val diaSemana = nomesDiasSemana[calendario.get(Calendar.DAY_OF_WEEK) - 1]
        val dia = calendario.get(Calendar.DAY_OF_MONTH)
        val mes = calendario.get(Calendar.MONTH) + 1
        return "$diaSemana, %02d/%02d".format(dia, mes)
    }
}
