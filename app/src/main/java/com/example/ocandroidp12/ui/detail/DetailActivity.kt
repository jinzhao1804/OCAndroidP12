package com.example.ocandroidp12.ui.detail

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material.icons.sharp.Star
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.ocandroidp12.domain.model.Cloth

@Composable
fun ClothDetailScreen(cloth: Cloth, navController: NavController) {
    val context = LocalContext.current
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    var showShareDialog by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            ClothImage(cloth, onShareDialog = { showShareDialog = true }, navController, isLandscape)
            ClothDetails(cloth)
            RatingSection(cloth, rating, onRatingChange = { rating = it })
            ReviewSection(reviewText, onReviewChange = { reviewText = it }, rating, onSaveReview = {
                saveReview(rating, reviewText)
                reviewText = ""
                rating = 0
            })
        }
    }

    if (showShareDialog) {
        ShareContentDialog(context = context, cloth = cloth, onDismiss = { showShareDialog = false })
    }
}

@Composable
fun ClothImage(cloth: Cloth, onShareDialog: () -> Unit, navController: NavController, isLandscape: Boolean) {
    Box {
        Image(
            painter = rememberAsyncImagePainter(cloth.picture.url),
            contentDescription = cloth.picture.description,
            modifier = Modifier
                .fillMaxWidth()
                .height(431.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        ShareIcon(onShareClick = onShareDialog)
        if (!isLandscape) {
            BackIcon(navController)
        }
        HeartTextCard(text = "${cloth.likes}", modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp))
    }
}

@Composable
fun ShareIcon(onShareClick: () -> Unit) {
    Box (
        modifier = Modifier
            .fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
        ) {
            IconButton(onClick = onShareClick, modifier = Modifier.padding(8.dp)) {
                Icon(imageVector = Icons.Filled.Share, contentDescription = "Share")
            }
        }
    }
}

@Composable
fun BackIcon(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize() // Ensures the Box occupies the full screen
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart) // Align the button to the top start
                .padding(16.dp)
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
        ) {
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(8.dp)) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go to previous screen")
            }
        }
    }
}

@Composable
fun ClothDetails(cloth: Cloth) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = cloth.name, style = MaterialTheme.typography.headlineLarge)
    Row {
        Text(text = "$${cloth.price}", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.weight(1F))
        Text(text = "$${cloth.original_price}", style = MaterialTheme.typography.bodyLarge)
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = cloth.picture.description, style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun RatingSection(cloth: Cloth, rating: Int, onRatingChange: (Int) -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberAsyncImagePainter(cloth.picture.url),
            contentDescription = cloth.picture.description,
            modifier = Modifier
                .width(32.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(50.dp)),
            contentScale = ContentScale.Crop
        )

        Row {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Sharp.Favorite,
                    contentDescription = "Star $i",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onRatingChange(i) }
                )
            }
        }
    }
}

@Composable
fun ReviewSection(reviewText: String, onReviewChange: (String) -> Unit, rating: Int, onSaveReview: () -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = "Leave a review:", style = MaterialTheme.typography.bodyMedium)
    OutlinedTextField(
        value = reviewText,
        onValueChange = onReviewChange,
        placeholder = { Text(text = "Write your review here") },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        keyboardActions = KeyboardActions(
            onDone = {
                onSaveReview()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        )
    )
}

fun saveReview(rating: Int, reviewText: String) {
    // Implement logic to save review and rating
}

@Composable
fun ShareContentDialog(context: Context, cloth: Cloth, onDismiss: () -> Unit) {
    var shareText by remember { mutableStateOf(
        "Check out this cloth: ${cloth.name}\n${cloth.picture.description}\nPrice: $${cloth.price}\nOriginal Price: $${cloth.original_price}\n${cloth.picture.url}"
    ) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modify your share text") },
        text = {
            Column {
                TextField(
                    value = shareText,
                    onValueChange = { shareText = it },
                    label = { Text("Share Text") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(onClick = { shareTextContent(context, shareText); onDismiss() }) {
                Text("Share")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun shareTextContent(context: Context, shareText: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}

@Composable
fun HeartTextCard(icon: ImageVector = Icons.Filled.Favorite, text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
            .width(56.dp)
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
                    .size(24.dp)
                    .padding(end = 8.dp),
                tint = Color.Red
            )
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
