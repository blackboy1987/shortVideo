package com.bootx.ui.components

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun RightIcon(onClick: () -> Unit) {
    IconButton(
        onClick = { onClick() }
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "")
    }
}

@Composable
fun LeftIcon(onClick: () -> Unit,modifier: Modifier?) {
    IconButton(
        onClick = { onClick() }
    ) {
        modifier?.let {
            Modifier.then(it)
        }?.let { Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "", tint = Color.White, modifier = it) }
    }
}

@Composable
fun TopBarTitle(text: String) {
    Text(text = text, fontSize = MaterialTheme.typography.titleMedium.fontSize)
}

@Composable
fun SoftIcon(url: String) {
    AsyncImage(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp)),
        model = url,
        contentDescription = ""
    )
}

@Composable
fun SoftIcon12(url: String) {
    AsyncImage(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(10.dp)),
        model = url,
        contentDescription = ""
    )
}

@Composable
fun SoftIcon8(url: String) {
    AsyncImage(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape),
        model = url,
        contentDescription = ""
    )
}

@Composable
fun SoftIcon8_8(url: String) {
    AsyncImage(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp)),
        model = url,
        contentScale=ContentScale.FillBounds,
        contentDescription = ""
    )
}

@Composable
fun SoftIcon6(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        modifier = Modifier
            .then(modifier)
            .size(60.dp)
            .clip(CircleShape),
        contentScale = ContentScale.FillBounds,
        model = url,
        contentDescription = ""
    )
}
@Composable
fun SoftIcon6_8(url: String) {
    AsyncImage(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(8.dp)),
        model = url,
        contentDescription = ""
    )
}

@Composable
fun SoftIcon4(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .then(modifier),
        model = url,
        contentDescription = ""
    )
}


fun toast(context: Context, message: String) {
    val toast =
        Toast.makeText(context, message, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
    toast.show()
}

@Composable
fun Loading302() {
    CircularProgressIndicator(
        modifier = Modifier.size(30.dp),
        strokeWidth = 2.dp,
    )
}

@Composable
fun Loading404() {
    CircularProgressIndicator(
        modifier = Modifier.size(40.dp),
        strokeWidth = 4.dp,
    )
}
