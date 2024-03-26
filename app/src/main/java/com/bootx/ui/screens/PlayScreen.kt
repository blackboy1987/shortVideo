package com.bootx.ui.screens


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.navigation.NavHostController
import com.bootx.compose.video.RepeatMode
import com.bootx.compose.video.ResizeMode
import com.bootx.compose.video.VideoPlayer
import com.bootx.compose.video.controller.VideoPlayerControllerConfig
import com.bootx.compose.video.uri.VideoPlayerMediaItem
import com.bootx.compose.video.util.findActivity
import com.bootx.ui.components.LeftIcon
import com.bootx.util.SharedPreferencesUtils
import com.bootx.viewmodel.PlayViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("RememberReturnType")
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@OptIn(UnstableApi::class)
@Composable
fun PlayScreen(
    navController: NavHostController,
    id: String,
    playViewModel: PlayViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var playUrl by remember { mutableStateOf("") }
    var title by remember {
        mutableStateOf("")
    }
    var currentIndex by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(Unit) {
        title = SharedPreferencesUtils(context).get("title")
        playViewModel.items(context, id)
        // 获取到列表之后，再获取播放地址
        val oldCurrentIndex =
            try {
                //currentIndex = SharedPreferencesUtils(context).get("${id}_currentIndex").toInt()
            } catch (_: Exception) {
            }
        // 记录播放的集数
        SharedPreferencesUtils(context).set("${id}_currentIndex", "$currentIndex")
        playUrl = playViewModel.playUrl
    }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false
        )
    )

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val halfScreenHeight = screenHeight / 2
    // 隐藏状态栏
    WindowCompat.setDecorFitsSystemWindows((context as Activity).window, false)
    var sliderState = SliderState(
        value = 0.5f,
    )
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
        onDispose {}
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetSwipeEnabled = false,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(halfScreenHeight),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(4.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(playViewModel.list) { index, item ->
                        ListItem(
                            shadowElevation = 8.dp,
                            modifier = Modifier.clickable {

                            },
                            headlineContent = {
                                Text(
                                    text = "第 ${index + 1} 集",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            trailingContent = {
                                if (currentIndex == index) {
                                    OutlinedButton(
                                        contentPadding = PaddingValues(all = 0.dp),
                                        modifier = Modifier.size(width = 60.dp, height = 30.dp),
                                        onClick = {
                                        }) {
                                        Text(
                                            modifier = Modifier.padding(0.dp),
                                            fontSize = 12.sp,
                                            text = "播放中"
                                        )
                                    }
                                } else {
                                    OutlinedButton(
                                        contentPadding = PaddingValues(all = 0.dp),
                                        modifier = Modifier.size(width = 60.dp, height = 30.dp),
                                        onClick = {
                                            coroutineScope.launch {
                                                currentIndex = index
                                                playUrl = playViewModel.play(
                                                    context,
                                                    playViewModel.list[index].id
                                                )
                                                scaffoldState.bottomSheetState.hide()
                                            }
                                        }) {
                                        Text(
                                            modifier = Modifier.padding(0.dp),
                                            fontSize = 12.sp,
                                            text = "播放"
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        Box( modifier = Modifier.clickable {
            coroutineScope.launch {
                if(scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded){
                    scaffoldState.bottomSheetState.hide()
                }
            }
        }) {
            Box(

            ) {
                VideoPlayer(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.TopCenter),
                    mediaItems = listOf(
                        VideoPlayerMediaItem.NetworkMediaItem(
                            url = playUrl,
                            mediaMetadata = MediaMetadata.Builder().setTitle("Clear MP4: Dizzy")
                                .build(),
                            mimeType = MimeTypes.APPLICATION_M3U8,
                        ),
                    ),
                    resizeMode = ResizeMode.ZOOM,
                    handleLifecycle = true,
                    autoPlay = true,
                    usePlayerController = false,
                    enablePip = true,
                    handleAudioFocus = true,
                    controllerConfig = VideoPlayerControllerConfig(
                        showSpeedAndPitchOverlay = false,
                        showSubtitleButton = false,
                        showCurrentTimeAndTotalTime = true,
                        showBufferingProgress = true,
                        showForwardIncrementButton = false,
                        showBackwardIncrementButton = false,
                        showBackTrackButton = false,
                        showNextTrackButton = false,
                        showRepeatModeButton = false,
                        controllerShowTimeMilliSeconds = 5_000,
                        controllerAutoShow = false,
                        showFullScreenButton = false,
                    ),
                    volume = 0.5f,
                    repeatMode = RepeatMode.NONE,
                    onCurrentTimeChanged = {
                        Log.e("CurrentTime", it.toString())

                        // 记录播放的集数的时间
                        SharedPreferencesUtils(context).set("${id}_time", it.toString())
                    },
                    playerInstance = {
                        addAnalyticsListener(object : AnalyticsListener {
                            @OptIn(UnstableApi::class)
                            override fun onPlaybackStateChanged(
                                eventTime: AnalyticsListener.EventTime,
                                state: Int
                            ) {
                                super.onPlaybackStateChanged(eventTime, state)
                                /*if (state == Player.STATE_READY) {
                                    CommonUtils.toast(context,"$currentIndex")
                                    try {
                                        val time = SharedPreferencesUtils(context).get("${id}_time")
                                        seekTo(time.toLong())
                                    }catch (_:Exception){}
                                }*/
                                if (state == 4) {
                                    // 播放完成
                                    Log.e(
                                        "onPlaybackStateChanged1",
                                        "onPlaybackStateChanged0: $playUrl,$id",
                                    )
                                    coroutineScope.launch {
                                        currentIndex += 1
                                        // 记录播放的集数
                                        SharedPreferencesUtils(context).set(
                                            "${id}_currentIndex",
                                            "$currentIndex"
                                        )
                                        SharedPreferencesUtils(context).set("${id}_time", "0")
                                        playUrl = playViewModel.play(
                                            context,
                                            playViewModel.list[currentIndex].id
                                        )
                                    }
                                }
                            }


                        })
                    },
                )
            }
            Box(
                modifier = Modifier
                    .systemBarsPadding()
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .height(40.dp)
                    .padding(top = 8.dp, end = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LeftIcon(
                        modifier = Modifier.size(16.dp),
                        onClick = {
                            navController.popBackStack()
                        }
                    )
                    Text(
                        text = "$title 第 ${currentIndex + 1} 集",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    OutlinedButton(
                        contentPadding = PaddingValues(all = 0.dp),
                        onClick = {
                            coroutineScope.launch {
                                if(scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded){
                                    scaffoldState.bottomSheetState.hide()
                                }else{
                                    scaffoldState.bottomSheetState.expand()
                                }
                            }
                        },
                        modifier = Modifier.size(width = 60.dp, height = 30.dp)
                    ) {
                        Text(
                            text = "选集",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(0.dp),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            /*Box(modifier = Modifier.align(Alignment.BottomStart)){
                Slider(state = sliderState,thumb={}, modifier = Modifier.alpha(0.5f), enabled = false)
            }*/
        }
    }
}
