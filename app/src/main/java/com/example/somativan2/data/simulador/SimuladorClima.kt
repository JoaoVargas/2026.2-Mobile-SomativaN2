package com.example.somativan2.data.simulador

import com.example.somativan2.data.model.ClimaHora
import com.example.somativan2.data.model.ResumoDia
import com.example.somativan2.data.model.ResumoMes
import java.util.Calendar
import java.util.TimeZone

// Gera um clima "falso" a partir do nome da cidade, sem precisar de internet.
// A mesma cidade e o mesmo instante (hora, dia ou mês) sempre geram o mesmo resultado.
object SimuladorClima {

    private val condicoes = listOf("Ensolarado", "Nublado", "Chuvoso", "Tempestade", "Parcialmente nublado")
    private val nomesMeses = listOf(
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    )

    fun gerarHora(nome: String, offsetHoras: Long): ClimaHora {
        val semente = semente(nome, offsetHoras)
        return ClimaHora(
            timestampOffsetHoras = offsetHoras,
            temperatura = (10 + (semente % 25)).toInt(),
            umidade = (30 + (semente / 7 % 60)).toInt(),
            vento = (2 + (semente / 13 % 40)).toInt(),
            condicao = condicoes[(semente % condicoes.size).toInt()]
        )
    }

    fun gerarHorasApartirDe(nome: String, offsetInicial: Long, quantidade: Int): List<ClimaHora> =
        (0 until quantidade).map { gerarHora(nome, offsetInicial + it) }

    fun gerarHorasDoDia(nome: String, diaOffsetAbsoluto: Int): List<ClimaHora> =
        gerarHorasApartirDe(nome, diaOffsetAbsoluto * 24L, 24)

    fun gerarResumoDia(nome: String, diaOffsetAbsoluto: Int): ResumoDia {
        val horas = gerarHorasDoDia(nome, diaOffsetAbsoluto)
        val condicaoPredominante = horas
            .groupingBy { it.condicao }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
            ?: horas.first().condicao
        return ResumoDia(
            diaOffset = diaOffsetAbsoluto,
            diaDoMes = diaDoMesAbsoluto(diaOffsetAbsoluto),
            temperaturaMin = horas.minOf { it.temperatura },
            temperaturaMax = horas.maxOf { it.temperatura },
            condicaoPredominante = condicaoPredominante
        )
    }

    fun gerarProximosDias(nome: String, dias: Int = 7): List<ResumoDia> =
        (1..dias).map { gerarResumoDia(nome, it) }

    // Histórico: dias do mês até hoje. Dias depois de hoje ainda não aconteceram,
    // então não têm clima "passado" pra mostrar e são descartados da lista.
    fun gerarDiasDoMes(nome: String, mesOffset: Int): List<ResumoDia> {
        val hoje = Calendar.getInstance()
        return (1..diasNoMes(mesOffset)).mapNotNull { diaDoMes ->
            val data = Calendar.getInstance().apply {
                add(Calendar.MONTH, mesOffset)
                set(Calendar.DAY_OF_MONTH, diaDoMes)
            }
            val offset = diferencaEmDiasCalendario(data, hoje)
            if (offset > 0) null else gerarResumoDia(nome, offset)
        }
    }

    fun gerarResumoMes(nome: String, mesOffset: Int): ResumoMes {
        val dias = gerarDiasDoMes(nome, mesOffset)
        return ResumoMes(
            mesOffset = mesOffset,
            nome = nomeDoMes(mesOffset),
            temperaturaMediaMin = dias.map { it.temperaturaMin }.average().toInt(),
            temperaturaMediaMax = dias.map { it.temperaturaMax }.average().toInt()
        )
    }

    fun gerarUltimosMeses(nome: String): List<ResumoMes> =
        (0 downTo -11).map { gerarResumoMes(nome, it) }

    // Diferença em dias de calendário entre duas datas, imune a horário de verão.
    // Subtrair diretamente os timeInMillis de dois Calendar locais falha sempre que um
    // fuso horário com DST estiver ativo: um dia "de verão" tem 23h ou 25h reais, então
    // a divisão por 24h deixa de ser exata e a conta erra o dia (duplicando ou pulando offsets).
    // Por isso a data (ano/mês/dia) é primeiro extraída do calendário local e só depois
    // recolocada à meia-noite em UTC, fuso que nunca tem transição de DST.
    private fun diferencaEmDiasCalendario(data: Calendar, referencia: Calendar): Int {
        val milissegundosPorDia = 1000L * 60 * 60 * 24
        val diferenca = meiaNoiteUtcEmMilissegundos(data) - meiaNoiteUtcEmMilissegundos(referencia)
        return (diferenca / milissegundosPorDia).toInt()
    }

    private fun meiaNoiteUtcEmMilissegundos(calendario: Calendar): Long {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.clear()
        utc.set(
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )
        return utc.timeInMillis
    }

    private fun diaDoMesAbsoluto(diaOffsetAbsoluto: Int): Int =
        Calendar.getInstance()
            .apply { add(Calendar.DAY_OF_YEAR, diaOffsetAbsoluto) }
            .get(Calendar.DAY_OF_MONTH)

    private fun diasNoMes(mesOffset: Int): Int =
        Calendar.getInstance().apply { add(Calendar.MONTH, mesOffset) }.getActualMaximum(Calendar.DAY_OF_MONTH)

    private fun semente(nome: String, offset: Long): Long {
        val base = nome.trim().lowercase().hashCode().toLong()
        return kotlin.math.abs(base + offset * 31L)
    }

    private fun nomeDoMes(mesOffset: Int): String {
        val calendario = Calendar.getInstance().apply { add(Calendar.MONTH, mesOffset) }
        val indiceMes = calendario.get(Calendar.MONTH)
        val ano = calendario.get(Calendar.YEAR)
        return "${nomesMeses[indiceMes]} $ano"
    }
}
