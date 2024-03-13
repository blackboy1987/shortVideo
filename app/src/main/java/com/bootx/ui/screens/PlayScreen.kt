package com.bootx.ui.screens


import android.app.Activity
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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
import com.bootx.util.SharedPreferencesUtils
import com.bootx.viewmodel.PlayViewModel
import kotlinx.coroutines.launch


@OptIn(UnstableApi::class)
@Composable
fun PlayScreen(
    navController: NavHostController,
    fsId: String,
    playViewModel: PlayViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var playUrl by remember { mutableStateOf("https://bootx-video.oss-cn-hangzhou.aliyuncs.com/270925551843771.m3u8") }
    var nextFsId by remember { mutableStateOf(fsId) }
    LaunchedEffect(Unit) {
        //playUrl = SharedPreferencesUtils(context).get(fsId)
        playViewModel.items(context, fsId)

    }

    // 获取当前的Activity
    val activity = (LocalContext.current as? Activity)
    // 在Compose的SideEffect中设置沉浸式状态栏
    SideEffect {
        // 设置内容显示在状态栏和导航栏下方
        activity?.window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
        activity?.window?.statusBarColor = android.graphics.Color.TRANSPARENT
    }
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Box() {
            Box(){
                VideoPlayer(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.TopCenter)
                        .clickable {
                            Log.e("VideoPlayer click", "PlayScreen: aaa")
                        },
                    mediaItems = listOf(
                        VideoPlayerMediaItem.NetworkMediaItem(
                            url = playUrl,
                            mediaMetadata = MediaMetadata.Builder().setTitle("Clear MP4: Dizzy").build(),
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
                        showSpeedAndPitchOverlay = true,
                        showSubtitleButton = true,
                        showCurrentTimeAndTotalTime = true,
                        showBufferingProgress = true,
                        showForwardIncrementButton = true,
                        showBackwardIncrementButton = true,
                        showBackTrackButton = true,
                        showNextTrackButton = true,
                        showRepeatModeButton = true,
                        controllerShowTimeMilliSeconds = 5_000,
                        controllerAutoShow = true,
                        showFullScreenButton = true,
                    ),
                    volume = 0.5f,
                    repeatMode = RepeatMode.NONE,
                    onCurrentTimeChanged = {
                        Log.e("CurrentTime", it.toString())
                    },
                    playerInstance = {
                        addAnalyticsListener(object : AnalyticsListener {
                            @OptIn(UnstableApi::class)
                            override fun onPlaybackStateChanged(
                                eventTime: AnalyticsListener.EventTime,
                                state: Int
                            ) {
                                super.onPlaybackStateChanged(eventTime, state)
                                if(state==4){
                                    // 播放完成
                                    Log.e("onPlaybackStateChanged1", "onPlaybackStateChanged0: $playUrl,$fsId", )
                                    coroutineScope.launch {
                                        nextFsId = playViewModel.getNext(context, nextFsId)
                                        playUrl = SharedPreferencesUtils(context).get(nextFsId)
                                        Log.e("onPlaybackStateChanged1", "onPlaybackStateChanged1: $playUrl,$nextFsId", )
                                    }
                                }
                            }
                        })
                    },
                )
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 32.dp)) {
               Text(text = "第一季")
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 32.dp, end = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text = "A炼气十万年 98集")
                    OutlinedButton( contentPadding = PaddingValues(all = 0.dp),
                        onClick = { /*TODO*/ },
                        modifier = Modifier.size(width = 60.dp, height = 30.dp)
                    ) {
                        Text(text = "选集", fontSize = 12.sp, modifier = Modifier.padding(0.dp))
                    }
                }
            }
        }
    }
}


