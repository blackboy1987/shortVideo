package com.bootx.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bootx.ui.components.CustomListItem
import com.bootx.ui.components.TabRowList
import com.bootx.ui.navigation.Destinations
import com.bootx.ui.theme.ShortVideoTheme
import com.bootx.util.SharedPreferencesUtils
import com.bootx.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@Composable
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        mainViewModel.category(context, "0")
        try{
            selectedTabIndex = SharedPreferencesUtils(context).get("tabIndex").toInt()
        }catch (_:Exception){}
        mainViewModel.list(context, mainViewModel.homeData[selectedTabIndex].id)
    }

    ShortVideoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {

            Column {
                if (!mainViewModel.loading) {
                    TabRowList(tabs = mainViewModel.homeData.map { item -> item.name },
                        selectedTabIndex = selectedTabIndex,
                        onClick = { index ->
                            selectedTabIndex = index
                            coroutineScope.launch {
                                mainViewModel.list(context, mainViewModel.homeData[index].id)
                            }
                            SharedPreferencesUtils(context).set("tabIndex","$selectedTabIndex")
                        }
                    )
                }
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                ) {
                    itemsIndexed(mainViewModel.list) { _, item ->
                        CustomListItem(title =item.name, onClick = {
                            SharedPreferencesUtils(context).set("title",item.name)
                            navController.navigate(Destinations.PlayFrame.route+"/${item.id}")
                        } )
                    }
                }
            }
        }
    }
}


