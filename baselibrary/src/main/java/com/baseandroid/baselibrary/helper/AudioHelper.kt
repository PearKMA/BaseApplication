@file:Suppress("DEPRECATION")

package com.baseandroid.baselibrary.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.baseandroid.baselibrary.utils.extension.buildVersion
import com.baseandroid.baselibrary.utils.extension.isBuildLargerThan

class AudioHelper(context: Context, onAudioLoss: (Boolean) -> Unit) {
    // region Const and Fields
    private lateinit var request: AudioFocusRequest
    private val mContext = context
    private val mAudioCallback = onAudioLoss

    private val mAudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val mTelephonyManager by lazy {
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    private var callback: CustomTelephonyCallback? = null

    /**
     * Xử lý khi video được khôi phục hoặc bị mất
     */
    private val afListener = AudioManager.OnAudioFocusChangeListener { i: Int ->
        when (i) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                //Âm thanh đã được khôi phục trở lại
                // -> có thể play media
                onAudioLoss.invoke(false)
            }
            AudioManager.AUDIOFOCUS_LOSS, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                //Tạm thời mất âm thanh, vẫn có thể giảm âm lượng và play hoặc tạm ngưng âm thanh
                //lowerVolume() ||  pause()
                onAudioLoss.invoke(true)
            }
        }
    }

    /**
     * Xử lý khi có cuộc gọi
     */
    private val phoneListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            if (state != TelephonyManager.CALL_STATE_IDLE) {
                onAudioLoss.invoke(true)
            } else {
                onAudioLoss.invoke(false)
            }
            super.onCallStateChanged(state, phoneNumber)
        }
    }
    // endregion

    // region interactive
    /**
     * Ngừng lắng nghe tín hiệu media
     */
    fun stopRequestAudio() {
        try {
            abandonMediaFocus()
        } catch (e: Exception) {
        }
    }

    /**
     * Yêu cầu ngừng media các app khác
     */
    fun requestAudio(): Boolean {
        return try {
            requestAudioFocus() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } catch (e: Exception) {
            false
        }
    }
    // endregion

    // region private method
    /**
     * Tắt âm thanh từ app khác & bắt sự kiện âm thanh
     */
    private fun requestAudioFocus(): Int {
        abandonMediaFocus()

        if (isBuildLargerThan(buildVersion.S)) {
            if (mContext.isReadPhoneStateGranted()) {
                mTelephonyManager.registerTelephonyCallback(
                    mContext.mainExecutor,
                    CustomTelephonyCallback {
                        if (it != TelephonyManager.CALL_STATE_IDLE) {
                            mAudioCallback.invoke(true)
                        } else {
                            mAudioCallback.invoke(false)
                        }
                    }.also {
                        callback = it
                    }
                )
            }
        } else {
            mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE)
        }
        return if (isBuildLargerThan(Build.VERSION_CODES.O)) {
            //
            request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(afListener)
                .build()
            //
            mAudioManager.requestAudioFocus(
                request
            )
        } else {
            @Suppress("DEPRECATION")
            mAudioManager.requestAudioFocus(
                afListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    /**
     * Ngừng bắt sự kiện âm thanh
     */
    private fun abandonMediaFocus() {
        if (isBuildLargerThan(Build.VERSION_CODES.O) && ::request.isInitialized) {
            mAudioManager.abandonAudioFocusRequest(request)
        } else {
            @Suppress("DEPRECATION")
            mAudioManager.abandonAudioFocus(afListener)
        }

        if (isBuildLargerThan(buildVersion.S)) {
            callback?.let {
                mTelephonyManager.unregisterTelephonyCallback(it)
            }
        } else {
            mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE)
        }
    }
    // endregion


    //region android 12
    @RequiresApi(Build.VERSION_CODES.S)
    inner class CustomTelephonyCallback(private val func: (p0: Int) -> Unit) : TelephonyCallback(),
        TelephonyCallback.CallStateListener {
        override fun onCallStateChanged(p0: Int) {
            func(p0)
        }
    }

    private fun Context.isReadPhoneStateGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED


    // endregion
}