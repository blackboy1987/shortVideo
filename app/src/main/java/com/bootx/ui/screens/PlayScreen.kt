package com.bootx.ui.screens


import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.navigation.NavHostController
import com.bootx.util.SharedPreferencesUtils
import com.bootx.viewmodel.PlayViewModel
import com.bootx.compose.video.RepeatMode
import com.bootx.compose.video.ResizeMode
import com.bootx.compose.video.controller.VideoPlayerControllerConfig
import com.bootx.compose.video.uri.VideoPlayerMediaItem
import com.bootx.compose.video.VideoPlayer
import kotlinx.coroutines.launch


@OptIn(UnstableApi::class)
@Composable
fun PlayScreen(navController: NavHostController,fsId: String,playViewModel: PlayViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var playUrl by remember { mutableStateOf("") }
    var nextFsId by remember { mutableStateOf(fsId) }
    LaunchedEffect(Unit) {
        playUrl = SharedPreferencesUtils(context).get(fsId)

    }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Box() {
            Box(){
                VideoPlayer(
                    Modifier
                        .fillMaxSize().align(Alignment.TopCenter)
                        .clickable {
                            Log.e("VideoPlayer click", "PlayScreen: aaa" )
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
        }
    }
}


