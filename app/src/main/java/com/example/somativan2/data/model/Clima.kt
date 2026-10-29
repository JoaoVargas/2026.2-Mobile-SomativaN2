package com.example.somativan2.data.model

// Um único ponto de clima simulado (usado tanto para "agora" quanto para qualquer hora passada/futura).
data class ClimaHora(
    val timestampOffsetHoras: Long,
    val temperatura: Int,
    val umidade: Int,
    val vento: Int,
    val condicao: String
)

// Resumo de um dia inteiro (usado em Previsão e no nível "dias" do Histórico).
// diaOffset é usado internamente para navegar (ex.: buscar as horas desse dia);
// diaDoMes é o número do dia (1-31) a ser mostrado na tela.
data class ResumoDia(
    val diaOffset: Int,
    val diaDoMes: Int,
    val temperaturaMin: Int,
    val temperaturaMax: Int,
    val condicaoPredominante: String
)

// Resumo de um mês inteiro (usado no nível "meses" do Histórico).
data class ResumoMes(
    val mesOffset: Int,
    val nome: String,
    val temperaturaMediaMin: Int,
    val temperaturaMediaMax: Int
)
