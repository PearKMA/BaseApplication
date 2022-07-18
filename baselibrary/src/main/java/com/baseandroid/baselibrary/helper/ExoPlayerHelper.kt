package com.baseandroid.baselibrary.helper

import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.Util

/**
 *
 * How to use:
 *
 * init constructor
 * implement interface IExoPlayerCallback
 * -> onAudioLoss:  onloss -> pauseVideo() + set image pause
 *                  else -> resumeVideo() + set image play if resumeVideo() == true else pause
 * -> onCurrentTime: (text current time).text = time + (seekbar time).progress = progress
 *
 * (seekbar time).max = this.duration!!.toInt()
 * (text duration).text = formatTime(this.duration!!)
 *
 * button play/pause: if(playPauseVideo()){ set image play} else { set image pause}
 * seek: seekTo(time)
 * onResume:    startHandler() + check resumeVideo()
 * onPause:     stopHandler() + pauseVideo() + set image pause
 * onstart:     initializePlayer(url)
 * onstop:      killPlayer()
 */

open class ExoPlayerHelper(
    private val playerView: StyledPlayerView,
    delay: Long = 1000
) {
    // region Const and Fields

    interface IExoPlayerCallback {
        fun onAudioLoss(onLoss: Boolean)
        fun getDurationMedia(duration: Long)
        fun onPlaybackPositionChanged(position: Long)
        fun onPlaybackStateChanged(playbackState: Int)
        fun onIsPlayingChanged(isPlaying: Boolean)
        fun onPlayerError(error: PlaybackException)
    }

    private var audioHelper: AudioHelper
    private val playbackStateListener: Player.Listener = playbackStateListener()
    var listener: IExoPlayerCallback? = null
    private var mediaItem: MediaItem? = null
    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    private var enableRepeat = false
    private var volume = 1f

    private var handler: Handler = Handler(Looper.getMainLooper())
    private var mRunnable: Runnable = object : Runnable {
        override fun run() {
            if (player != null) {
                val current = player!!.currentPosition
                listener?.onPlaybackPositionChanged(current)
                handler.postDelayed(this, delay)
            }
        }
    }

    init {
        audioHelper = AudioHelper(playerView.context) {
            listener?.onAudioLoss(it)
        }
    }

    // endregion

    // region controller
    open fun getPlayer() = player

    open fun enableRepeat(enable: Boolean) {
        enableRepeat = enable
        player?.repeatMode = if (!enable) Player.REPEAT_MODE_OFF else Player.REPEAT_MODE_ALL
    }

    open fun setMedia(media: MediaItem) {
        mediaItem = media
    }

    open fun changeMedia(media: MediaItem) {
        mediaItem = media
        player?.setMediaItem(mediaItem!!, true)
        player?.prepare()
    }

    open fun changeStatePlayer(playing: Boolean) {
        player?.let {
            if (playing) {
                audioHelper.requestAudio()
            } else {
                audioHelper.stopRequestAudio()
            }
            it.playWhenReady = playing
        }
    }

    /**
     * Play/Pause media
     */
    open fun onPlayPauseMedia() {
        player?.let {
            if (it.playbackState == ExoPlayer.STATE_ENDED) {
                playbackPosition = 0L
                player?.seekTo(playbackPosition)
            }
            if (!it.isPlaying) {
                audioHelper.requestAudio()
            } else {
                audioHelper.stopRequestAudio()
            }
            it.playWhenReady = !it.isPlaying
        }
    }

    /**
     * Seek media to miliseconds
     */
    open fun seekTo(time: Long) {
        playbackPosition = time
        player?.seekTo(playbackPosition)
    }

    open fun changeVolume(volume: Float) {
        this.volume = volume
        player?.volume = volume
    }
    // endregion

    // region lifecycle methods
    open fun start() {
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    open fun resume() {
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }

    open fun pause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    open fun stop() {
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }
    // endregion

    // region private methods
    private fun initializePlayer() {
        if (mediaItem == null) return
        player = ExoPlayer.Builder(playerView.context)
            .build()
            .also { exoPlayer ->
                playerView.player = exoPlayer
                exoPlayer.setMediaItem(mediaItem!!)
                exoPlayer.addListener(playbackStateListener)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.repeatMode =
                    if (!enableRepeat) Player.REPEAT_MODE_OFF else Player.REPEAT_MODE_ALL
                exoPlayer.volume = volume
                exoPlayer.seekTo(currentItem, playbackPosition)
                audioHelper.requestAudio()
                exoPlayer.prepare()
            }
        handler.post(mRunnable)
    }


    /**
     * Release media player
     */
    private fun releasePlayer() {
        audioHelper.stopRequestAudio()
        handler.removeCallbacks(mRunnable)
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.removeListener(playbackStateListener)
            exoPlayer.release()
        }
        player = null
    }

    private fun playbackStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == ExoPlayer.STATE_READY) {
                listener?.getDurationMedia(player?.duration ?: 0L)
            }
            listener?.onPlaybackStateChanged(playbackState)
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            listener?.onIsPlayingChanged(isPlaying)
        }

        override fun onPlayerError(error: PlaybackException) {
            listener?.onPlayerError(error)
        }
    }
    // endregion
}
