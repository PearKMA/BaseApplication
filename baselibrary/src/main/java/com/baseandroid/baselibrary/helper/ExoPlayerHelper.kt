package com.baseandroid.baselibrary.helper

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.baseandroid.baselibrary.utils.formatTime
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView

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
    onError: (PlaybackException) -> Unit,
    delay: Long = 1000,
    private var callBack: IExoPlayerCallback?
) {


    interface IExoPlayerCallback {
        fun onAudioLoss(onLoss: Boolean)
        fun onCurrentTime(time: String, progress: Int)
    }

    private var audioHelper: AudioHelper? = null
    private var playbackPosition: Long = 0
    private var currentMediaItemIndex = 0
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
            handler.postDelayed(this, delay)
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            onError(error)
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
    fun initializePlayer(url: String/*, user: String*/) {
        exoPlayer = ExoPlayer.Builder(playerView.context).build()
        exoPlayer!!.repeatMode = Player.REPEAT_MODE_ALL
        exoPlayer!!.addListener(playerListener)

        playerView.player = exoPlayer
        //val userAgent = Util.getUserAgent(playerView.context, user)
        /*mediaSource = ProgressiveMediaSource
            .Factory(
                DefaultDataSourceFactory(playerView.context, userAgent),
                DefaultExtractorsFactory()
            )
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))*/
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        exoPlayer!!.setMediaItem(mediaItem)
        audioHelper?.requestAudio()
        exoPlayer!!.playWhenReady = true
        exoPlayer!!.seekTo(currentMediaItemIndex, playbackPosition)
        exoPlayer!!.prepare()
    }

    /**
     * Release media player
     */
    fun killPlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer!!.currentPosition
            currentMediaItemIndex = exoPlayer!!.currentMediaItemIndex
            playWhenReady = exoPlayer!!.playWhenReady
            exoPlayer!!.release()
            exoPlayer = null
            mediaSource = null
            playerView.player = null
        }
    }
}
