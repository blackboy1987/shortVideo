package com.bootx.ui.screens


import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bootx.ui.components.CustomListItem
import com.bootx.ui.components.MyDialog
import com.bootx.ui.components.MyInput1
import com.bootx.ui.components.TabRowList
import com.bootx.ui.navigation.Destinations
import com.bootx.ui.theme.ShortVideoTheme
import com.bootx.util.SharedPreferencesUtils
import com.bootx.viewmodel.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var keywords by remember {
        mutableStateOf("")
    }

    var clickStatus by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        mainViewModel.category(context, "0")
        try{
            selectedTabIndex = SharedPreferencesUtils(context).get("tabIndex").toInt()
        }catch (_:Exception){}
        mainViewModel.list(context, mainViewModel.homeData[selectedTabIndex].id,"")
    }
    var lazyListState = rememberLazyListState()
    // 隐藏状态栏
    WindowCompat.setDecorFitsSystemWindows((context as Activity).window, false)

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
        onDispose {}
    }
    ShortVideoTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    MyInput1(placeholder = "输入你想要的短剧", value = keywords, onValueChange = { text->
                    keywords = text
                })
                }, actions = {
                    Button(modifier = Modifier.padding(start = 16.dp),onClick = {
                        coroutineScope.launch {
                            mainViewModel.list(context, mainViewModel.homeData[selectedTabIndex].id,keywords)
                            lazyListState.animateScrollToItem(0)
                        }
                    }) {
                       Text(text = "搜索")
                    }
                })
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    if (!mainViewModel.loading) {
                        TabRowList(tabs = mainViewModel.homeData.map { item -> item.name },
                            selectedTabIndex = selectedTabIndex,
                            onClick = { index ->
                                selectedTabIndex = index
                                coroutineScope.launch {
                                    mainViewModel.list(context, mainViewModel.homeData[index].id,keywords)
                                    lazyListState.animateScrollToItem(0)
                                }
                                SharedPreferencesUtils(context).set("tabIndex","$selectedTabIndex")
                            }
                        )
                    }
                    if(clickStatus){
                        MyDialog();
                    }
                    LazyColumn(
                        state=lazyListState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        itemsIndexed(mainViewModel.list) { _, item ->
                            CustomListItem(title =item.name, onClick = {
                                if(!clickStatus){
                                    clickStatus = true
                                    SharedPreferencesUtils(context).set("title",item.name)
                                    navController.navigate(Destinations.PlayFrame.route+"/${item.id}")
                                }
                            } )
                        }
                    }
                }
            }
        }



    }
}


