package com.das_develop.colorjet

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.das_develop.colorjet.Data.ColorEntity
import com.das_develop.colorjet.ViewModel.ColorViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ColorList(viewModel: ColorViewModel) {
    val context = LocalContext.current
    val colorList by viewModel.allColors.observeAsState(emptyList())
    val unsyncedCount by viewModel.unsyncedColorsCount.observeAsState(0)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.heading))
                    .padding(start = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ColorApp",
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )

                SyncButton(
                    onClick = {
                        if (colorList.isNotEmpty()) {
                            viewModel.syncColorsToFirebase(context)
                            Toast.makeText(context, "Syncing colors Successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "No colors to sync", Toast.LENGTH_SHORT).show()
                        }
                    },
                    unsyncedCount = unsyncedCount
                )

            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(colorList.size) { index ->
                    val colorItem = colorList[index]
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = formatter.format(colorItem.timestamp)

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                color = Color(android.graphics.Color.parseColor(colorItem.color)),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = colorItem.color,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 8.dp, top = 8.dp)
                        )
                        Text(
                            text = "Created at:\n ${formattedDate}",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding( bottom = 8.dp)
                        )
                    }
                }
            }

        }
        AddColorButton(viewModel = viewModel ,  modifier = Modifier.align(Alignment.BottomEnd))

    }
}


// Function to generate random color
fun generateRandomColor(): String {
    val chars = "ABCDEF0123456789"
    return "#${(1..6).map { chars.random() }.joinToString("")}"
}


fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}


@Composable
fun SyncButton(onClick: () -> Unit, unsyncedCount: Int) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.btnBck),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(45.dp),
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "$unsyncedCount",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .width(12.dp),
        )

        Icon(
            painter = painterResource(id = R.drawable.sync),
            contentDescription = "Sync Icon",
            tint =colorResource(id = R.color.heading),
            modifier = Modifier.size(24.dp)
        )

    }
}

@Composable
fun AddColorButton(viewModel: ColorViewModel , modifier: Modifier) {

    Button(
        onClick = {
            val newColor = ColorEntity(
                color = generateRandomColor(),
                timestamp = System.currentTimeMillis()
            )
            viewModel.addColor(newColor)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.btnBck),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(45.dp),
        modifier = modifier
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Add Color",
                style = TextStyle(
                    color = colorResource(id =R.color.heading),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
            )

            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Add Icon",
                tint =colorResource(id = R.color.heading),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}