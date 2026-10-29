package com.example.somativan2.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.somativan2.ui.screens.adicionar.AdicionarCidadeScreen
import com.example.somativan2.ui.screens.configuracoes.ConfiguracoesScreen
import com.example.somativan2.ui.screens.favoritos.FavoritosScreen
import com.example.somativan2.ui.screens.historico.HistoricoScreen
import com.example.somativan2.ui.screens.home.HomeScreen
import com.example.somativan2.ui.screens.previsao.PrevisaoScreen

private data class ItemBottomBar(val rota: String, val titulo: String, val icone: androidx.compose.ui.graphics.vector.ImageVector)

private val itensBottomBar = listOf(
    ItemBottomBar(Rotas.HOME, "Início", Icons.Filled.Home),
    ItemBottomBar(Rotas.FAVORITOS, "Favoritos", Icons.Filled.Favorite),
    ItemBottomBar(Rotas.CONFIGURACOES, "Config.", Icons.Filled.Settings)
)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val rotaAtual = backStackEntry?.destination

            NavigationBar {
                itensBottomBar.forEach { item ->
                    NavigationBarItem(
                        selected = rotaAtual?.hierarchy?.any { it.route == item.rota } == true,
                        onClick = {
                            navController.navigate(item.rota) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icone, contentDescription = item.titulo) },
                        label = { Text(item.titulo) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rotas.HOME) {
                HomeScreen(
                    aoAbrirPrevisao = { navController.navigate(Rotas.PREVISAO) },
                    aoAbrirHistorico = { navController.navigate(Rotas.HISTORICO) }
                )
            }
            composable(Rotas.PREVISAO) {
                PrevisaoScreen(aoVoltar = { navController.popBackStack() })
            }
            composable(Rotas.HISTORICO) {
                HistoricoScreen(aoVoltar = { navController.popBackStack() })
            }
            composable(Rotas.FAVORITOS) {
                FavoritosScreen(
                    aoAdicionarCidade = { navController.navigate(Rotas.ADICIONAR) }
                )
            }
            composable(Rotas.ADICIONAR) {
                AdicionarCidadeScreen(
                    aoVoltar = { navController.popBackStack() },
                    aoCidadeAdicionada = { navController.popBackStack() }
                )
            }
            composable(Rotas.CONFIGURACOES) {
                ConfiguracoesScreen()
            }
        }
    }
}
