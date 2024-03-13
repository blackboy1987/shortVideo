package com.bootx.ui.components

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(modifier: Modifier = Modifier, context: Context = LocalContext.current, videoUrl: String) {
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().also { player ->
            val mediaItem = MediaItem.fromUri("https://bootx-video.oss-cn-hangzhou.aliyuncs.com/12001649489998.m3u8")
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
        }
    }

    // 当Composable从组合树中移除时释放ExoPlayer资源
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release() // 释放ExoPlayer资源
        }
    }


    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM // 强制铺满屏幕
            }
        },
        update = { playerView ->
            playerView.player = exoPlayer
        }
    )
}
