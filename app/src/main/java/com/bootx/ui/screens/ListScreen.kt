package com.bootx.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bootx.ui.components.LeftIcon
import com.bootx.ui.components.SoftIcon8_8
import com.bootx.ui.navigation.Destinations
import com.bootx.ui.theme.ShortVideoTheme
import com.bootx.util.SharedPreferencesUtils
import com.bootx.viewmodel.ListViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController,id: String, listViewModel: ListViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val title by remember{
        mutableStateOf("");
    }

    LaunchedEffect(Unit) {
        listViewModel.list(context, id)
        SharedPreferencesUtils(context).get("title")
    }
    ShortVideoTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    LeftIcon(
                        modifier = Modifier.size(16.dp),
                        onClick = {
                            navController.popBackStack()
                        }
                    )
                },
            )
        }) {
            Surface(
                modifier = Modifier.padding(it)
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                ) {
                    itemsIndexed(listViewModel.list) { _, item ->
                        ListItem(
                            modifier = Modifier.clickable {
                                if (item.category == 6) {
                                    SharedPreferencesUtils(context).set("title",item.name)
                                    coroutineScope.launch {
                                        navController.navigate(Destinations.ListFrame.route + "/${item.id}")
                                    }
                                } else if (item.category == 1) {
                                    // 获取播放地址
                                    coroutineScope.launch {
                                        listViewModel.getPlayUrl(context, item.id)
                                        navController.navigate(Destinations.PlayFrame.route + "/${item.id}")
                                    }
                                    coroutineScope.launch {
                                        listViewModel.getAllPlayUrl(context, id)
                                    }

                                }
                            },
                            headlineContent = { Text(text = item.name) },
                            leadingContent = { SoftIcon8_8(url = item.cover) },
                        )

                    }
                }
            }
        }
        }
}


