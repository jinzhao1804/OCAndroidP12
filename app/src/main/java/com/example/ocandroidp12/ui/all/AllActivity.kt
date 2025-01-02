package com.example.ocandroidp12.ui.all

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ocandroidp12.domain.model.Cloth




@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ClothesScreen(viewModel: AllViewModel, onItemClick: (Cloth) -> Unit, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val filteredClothes by viewModel.filteredClothes.collectAsState()


    Scaffold(
        topBar = {
            SearchBar(isSearching, searchText, onSearchTextChange = { viewModel.onSearchTextChange(it) }, onToggleSearch = { viewModel.onToogleSearch() })
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            when (state) {
                is ClothesState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is ClothesState.Success -> {
                    val clothes = filteredClothes // Use filtered clothes here
                    val categorizedClothes = viewModel.groupByCategory(clothes)
                    CategoryList(categorizedClothes, onItemClick)
                    Log.e("data", "$categorizedClothes")
                }
                is ClothesState.Error -> Text("Error loading data", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun SearchBar(isSearching: Boolean, searchText: String, onSearchTextChange: (String) -> Unit, onToggleSearch: () -> Unit) {
    if (isSearching) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onToggleSearch() }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            }

            TextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                placeholder = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    } else {
        IconButton(onClick = { onToggleSearch() }) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        }
    }
}

@Composable
fun CategoryList(categorizedClothes: Map<String, List<Cloth>>, onItemClick: (Cloth) -> Unit) {
    LazyColumn {
        items(categorizedClothes.keys.toList()) { category ->
            CategoryRow(category, categorizedClothes[category] ?: emptyList(), onItemClick)
        }
    }
}

@Composable
fun CategoryRow(category: String, clothes: List<Cloth>, onItemClick: (Cloth) -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
        LazyRow {
            items(clothes) { cloth ->
                ClothItem(cloth, onItemClick)
            }
        }
    }
}

@Composable
fun ClothItem(cloth: Cloth, onItemClick: (Cloth) -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp) // Adjust the width as needed
            .clickable { onItemClick(cloth) }
    ) {
        Column {
            ClothItemContent(cloth)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = cloth.name, style = MaterialTheme.typography.bodySmall)
            Row {
                Text(text = "${cloth.price}", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.weight(1F))
                Text(text = "${cloth.original_price}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun ClothItemContent(cloth: Cloth) {
    Box {
        Image(
            painter = rememberAsyncImagePainter(cloth.picture.url),
            contentDescription = cloth.name,
            modifier = Modifier
                .size(150.dp) // Adjust size as needed
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        HeartTextCardSmaller(
            text = "${cloth.likes}",
            modifier = Modifier
                .align(Alignment.BottomEnd) // Aligns the HeartTextCard to the bottom right corner
                .padding(8.dp) // Add padding as needed
        )
    }
}

@Composable
fun HeartTextCardSmaller(
    icon: ImageVector = Icons.Filled.Favorite,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(8.dp)
            .width(40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .padding(end = 4.dp),
                tint = Color.Red // Adjust color if needed
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
