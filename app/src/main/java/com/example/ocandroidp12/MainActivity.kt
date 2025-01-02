package com.example.ocandroidp12


import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ocandroidp12.domain.model.Cloth
import com.example.ocandroidp12.ui.all.AllViewModel
import com.example.ocandroidp12.ui.all.ClothesScreen
import com.example.ocandroidp12.ui.detail.ClothDetailScreen
import com.example.ocandroidp12.ui.theme.OCAndroidP9Theme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: AllViewModel by viewModels()

        setContent {
            OCAndroidP9Theme {
                val navController = rememberNavController()
                val clothesViewModel: AllViewModel = viewModel()
                val configuration = LocalConfiguration.current
                val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

                if (isLandscape) {
                    // Landscape Layout
                    LandscapeLayout(
                        clothesViewModel = clothesViewModel
                    )
                } else {
                    // Portrait Layout
                    NavHost(navController = navController, startDestination = "clothes") {
                        composable("clothes") {
                            ClothesScreen(viewModel,onItemClick = { cloth ->
                                navController.navigate("clothDetail/${cloth.id}")
                            })
                        }
                        composable("clothDetail/{clothId}") { backStackEntry ->
                            val clothId = backStackEntry.arguments?.getString("clothId")?.toIntOrNull()
                            if (clothId != null) {
                                clothesViewModel.loadClothById(clothId)
                                val cloth by clothesViewModel.cloth.collectAsState()

                                if (cloth != null) {
                                    ClothDetailScreen(
                                        cloth = cloth!!,
                                        navController = navController
                                    )
                                } else {
                                    Text("Loading...")
                                }
                            } else {
                                Text("Error: Invalid cloth ID")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LandscapeLayout(
    clothesViewModel: AllViewModel
) {
    var selectedCloth by remember { mutableStateOf<Cloth?>(null) }
    val clothes by clothesViewModel.cloth.collectAsState()

    Row(modifier = Modifier.fillMaxSize()) {
        // List of Clothes
        Column(
            modifier = Modifier
                .weight(if (selectedCloth == null) 1f else 0.8f) // Adjust weight based on selection
                .padding(16.dp)
        ) {
            ClothesScreen(clothesViewModel, onItemClick = { cloth ->
                selectedCloth = cloth
            })
        }

        if (selectedCloth != null) {
            Spacer(modifier = Modifier.width(16.dp))
            // Detail View
            Column(
                modifier = Modifier
                    .weight(1f) // Full space for detail view
                    .padding(16.dp)
            ) {
                ClothDetailScreen(cloth = selectedCloth!!, navController = rememberNavController() )
            }
        } else {

        }
    }
}
