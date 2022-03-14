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
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

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
        fun onMediaEnded()
    }

    private var audioHelper: AudioHelper? = null
    private var playbackPosition: Long = 0
    private var currentMediaItemIndex = 0
    private var playWhenReady = true
    private var isPlaying = true
    private var mute = false
    private var isPreview = false
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
            onError(error)
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == STATE_ENDED && isPreview) {
                callBack?.onMediaEnded()
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
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
    fun initializeRemotePlayer(url: String/*, user: String*/) {
        exoPlayer = ExoPlayer.Builder(playerView.context).build()
        exoPlayer!!.repeatMode = Player.REPEAT_MODE_ALL
        exoPlayer!!.addListener(playerListener)

        playerView.player = exoPlayer
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        exoPlayer!!.setMediaItem(mediaItem)
        audioHelper?.requestAudio()
        exoPlayer!!.playWhenReady = true
        exoPlayer!!.seekTo(currentMediaItemIndex, playbackPosition)
        exoPlayer!!.prepare()
    }

    fun initializeLocalPlayer(videoUrl: String, audioUrl: String) {
        exoPlayer = ExoPlayer.Builder(playerView.context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
            addListener(playerListener)
            if (audioUrl.isEmpty()) {
                val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                setMediaItem(mediaItem)
            } else {
                val dataSourceFactory = DefaultDataSource.Factory(playerView.context)
                val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))
                val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(audioUrl)))
                val mediaItem = MergingMediaSource(videoSource, audioSource)
                setMediaSource(mediaItem)
            }

            playbackPosition = 0
            playWhenReady = true
            volume = if (mute) {
                0f
            } else {
                audioHelper?.requestAudio()
                1f
            }
            seekTo(currentMediaItemIndex, playbackPosition)
            prepare()
        }
        playerView.player = exoPlayer
    }

    fun initializeRemotePlayer(videoUrl: String, audioUrl: String) {
        exoPlayer = ExoPlayer.Builder(playerView.context).build().apply {
            repeatMode = if (isPreview) Player.REPEAT_MODE_OFF else Player.REPEAT_MODE_ALL
            addListener(playerListener)
            val videoSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))

            val mediaItem = if (audioUrl.isEmpty()) {
                MergingMediaSource(videoSource)
            } else {
                val audioSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                    .createMediaSource(MediaItem.fromUri(Uri.parse(audioUrl)))
                MergingMediaSource(videoSource, audioSource)
            }
            setMediaSource(mediaItem)
            playbackPosition = 0
            playWhenReady = true
            volume = if (mute) {
                0f
            } else {
                audioHelper?.requestAudio()
                1f
            }
            seekTo(currentMediaItemIndex, playbackPosition)
            prepare()
        }
        playerView.player = exoPlayer
    }

    fun changeMediaSource(videoUrl: String, audioUrl: String, preview: Boolean = false) {
        isPreview = preview
        if (exoPlayer == null) {
            initializeRemotePlayer(videoUrl, audioUrl)
        } else {
            val videoSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))
            val mediaItem = if (audioUrl.isEmpty()) {
                MergingMediaSource(videoSource)
            } else {
                val audioSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                    .createMediaSource(MediaItem.fromUri(Uri.parse(audioUrl)))
                MergingMediaSource(videoSource, audioSource)
            }
            exoPlayer?.let {
                it.playWhenReady = false
                it.repeatMode = if (isPreview) Player.REPEAT_MODE_OFF else Player.REPEAT_MODE_ALL
                it.setMediaSource(mediaItem)
                it.playWhenReady = true
                it.volume = if (mute) {
                    0f
                } else {
                    audioHelper?.requestAudio()
                    1f
                }
                it.prepare()
            }
        }
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

    fun changeStatePlay(play: Boolean) {
        exoPlayer?.playWhenReady = play
    }

    fun setStateVolume(mute: Boolean) {
        this.mute = mute
        exoPlayer?.volume = if (mute) 0f else 1f
    }

    fun getStateVolume() = (exoPlayer?.volume ?: 0f).toInt()
}
