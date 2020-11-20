package com.hunglv.mylibrary.helper

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.hunglv.mylibrary.utils.formatTime

/**
 * How to use:
 *
 * init constructor + call function initAudioManager()      # important
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
class ExoPlayerHelper(
    private val playerView: PlayerView,
    onError: (ExoPlaybackException) -> Unit,
    private var callBack: IExoPlayerCallback?
) {


    interface IExoPlayerCallback {
        fun onAudioLoss(onLoss: Boolean)
        fun onCurrentTime(time: String, progress: Int)
    }

    private var audioHelper: AudioHelper? = null
    private var playbackPosition: Long = 0
    private var currentWindow = 0
    private var playWhenReady = true
    private var isPlaying = true
    private var handler: Handler = Handler(Looper.getMainLooper())

    private var exoPlayer: ExoPlayer? = null
    private var mediaSource: ProgressiveMediaSource? = null

    private var mRunnable: Runnable = object : Runnable {
        override fun run() {
            if (exoPlayer != null) {
                val current = exoPlayer!!.currentPosition
                callBack?.onCurrentTime(formatTime(current), current.toInt())
            }
            handler.postDelayed(this, 1000)
        }
    }

    private val playerListener = object : Player.EventListener {
        override fun onPlayerError(error: ExoPlaybackException) {
            super.onPlayerError(error)
            onError(error)
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)

        }
    }

    /**
     * KhỞi tạo trình quản lý audio
     */
    fun initAudioManager(context: Context) {
        audioHelper = AudioHelper(context) {
            callBack?.onAudioLoss(it)
        }
    }

    /**
     * Play/Pause media
     */
    fun playPauseVideo(): Boolean {
        if (exoPlayer != null) {
            if (exoPlayer!!.playWhenReady) {
                playWhenReady = false
                audioHelper?.stopRequestAudio()
            } else {
                playWhenReady = true
                audioHelper?.requestAudio()
            }
            exoPlayer!!.playWhenReady = playWhenReady
        }
        return playWhenReady
    }

    /**
     * Seek media to miliseconds
     */
    fun seekTo(time: Int) {
        if (exoPlayer != null) {
            exoPlayer!!.seekTo(time.toLong())
        }
    }

    /**
     * Resume play media
     */
    fun resumeVideo(): Boolean {
        if (exoPlayer != null) {
            exoPlayer!!.seekTo(playbackPosition)
            if (isPlaying) {
                audioHelper?.requestAudio()
            }
            exoPlayer!!.playWhenReady = isPlaying
        }
        handler.post(mRunnable)
        return isPlaying
    }

    /**
     * Pause media
     */
    fun pauseVideo() {
        if (exoPlayer != null) {
            isPlaying = exoPlayer!!.playWhenReady
            playbackPosition = exoPlayer!!.currentPosition
            exoPlayer!!.playWhenReady = false
            audioHelper?.stopRequestAudio()
        }
        handler.removeCallbacks(mRunnable)
    }

    /**
     * Start handle current time media playing
     */
    fun startHandler() {
        handler.removeCallbacks(mRunnable)
        handler.post(mRunnable)
    }

    /**
     * Stop handle time media playing
     */
    fun stopHandler() {
        handler.removeCallbacks(mRunnable)
    }

    /**
     * Init media player
     */
    fun initializePlayer(url: String, user: String) {
        exoPlayer = SimpleExoPlayer.Builder(playerView.context).build()
        exoPlayer!!.repeatMode = Player.REPEAT_MODE_ALL
        exoPlayer!!.addListener(playerListener)

        playerView.player = exoPlayer
        val userAgent = Util.getUserAgent(playerView.context, user)
        mediaSource = ProgressiveMediaSource
            .Factory(
                DefaultDataSourceFactory(playerView.context, userAgent),
                DefaultExtractorsFactory()
            )
            .createMediaSource(Uri.parse(url))
        audioHelper?.requestAudio()
        exoPlayer!!.prepare(mediaSource!!, true, false)
        exoPlayer!!.seekTo(currentWindow, playbackPosition)
        exoPlayer!!.playWhenReady = true
    }

    /**
     * Release media player
     */
    fun killPlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer!!.currentPosition
            currentWindow = exoPlayer!!.currentWindowIndex
            playWhenReady = exoPlayer!!.playWhenReady
            exoPlayer!!.release()
            exoPlayer = null
            mediaSource = null
            playerView.player = null
        }
    }
}
