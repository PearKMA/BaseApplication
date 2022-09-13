package com.baseandroid.baseapplication.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

public class GifView extends View {
    public static final int PLAY_ONCE = 100;
    public static final int PLAY_REPEAT = 101;
    private static final int DEFAULT_GIF_DURATION = 1000;
    private static final int STATE_STARTED = 0;
    private static final int STATE_STOPPED = 1;
    private static final int STATE_PAUSED = 2;
    private static final int STATE_RESUME = 3;
    private static final int DEFAULT_GIF_SPEED = 1;
    // reducing or increasing  the speed of the animation by using '_animationMultiplier'
    float _animationMultiplier = DEFAULT_GIF_SPEED;
    int _repeatMode = PLAY_REPEAT;
    int _gifID;
    private Movie _movie;
    private long _gifStartedTime = 0;
    private int _currentAnimationTime = 0;
    private long _resumedAt;
    private int _state;
    private boolean playWhenReady = false;

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setGIFResource(int gifID) {
        _gifID = gifID;
        initial();
    }

    public void setAnimationSpeed(float animationSpeed) {
        _animationMultiplier = animationSpeed;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // we can set gif width & height as wrap_content to gif, we cant set to
        // match_parent bcz programmatically we can't stretch the GIF to
        // match_parent
        if (_movie != null) {
            // to set gif height and width
            setMeasuredDimension(_movie.width(), _movie.height());

            // to set view height and width
            /* int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
            this.setMeasuredDimension(parentWidth, parentHeight);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());*/
        } else {
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (_movie != null) {
            updateGifPlayingTime();
        }
        drawGif(canvas);
    }

    private void updateGifPlayingTime() {

        long now = android.os.SystemClock.uptimeMillis();
        if (_gifStartedTime == 0) {
            _gifStartedTime = now;
        }
        int dur = _movie.duration();
        if (dur == 0) {
            dur = DEFAULT_GIF_DURATION;
        }

        if (_state == STATE_STOPPED) {
            _currentAnimationTime = _movie.duration() + 1000;
        } else if (_state == STATE_STARTED) {
            _currentAnimationTime = (int) (((android.os.SystemClock.uptimeMillis() - _gifStartedTime)
                    * _animationMultiplier));
        } else {
            _currentAnimationTime = (int) (((getGifPlayingTime(_resumedAt) - _gifStartedTime)
                    * _animationMultiplier));
        }
        if (_repeatMode == PLAY_REPEAT && _state != STATE_STOPPED) {
            _currentAnimationTime %= dur;
        }
    }

    private void drawGif(Canvas canvas) {
        if (_state == STATE_STOPPED) {
            _resumedAt = 0;
            _movie.setTime(_currentAnimationTime);
            _movie.draw(canvas, 0, 0);
        } else if (_state == STATE_PAUSED) {
            _movie.setTime(_currentAnimationTime);
            _movie.draw(canvas, 0, 0);
        } else {
            _movie.setTime(_currentAnimationTime);
            _movie.draw(canvas, 0, 0);
            if (_currentAnimationTime < _movie.duration()) {
                invalidate();
            }
        }
    }

    public void setPlayMode(int repeatMode) {
        this._repeatMode = repeatMode;
    }

    public void stop() {
        if (_state != STATE_STOPPED) {
            _state = STATE_STOPPED;
        }
        requestLayout();
        invalidate();
    }

    public Boolean isPlaying() {
        return _state == STATE_STARTED || _state == STATE_RESUME;
    }

    public void start() {
        _state = STATE_STARTED;
        _movie = Movie.decodeStream(getResources().openRawResource(_gifID));
        _currentAnimationTime = 0;
        _gifStartedTime = 0;
        requestLayout();
        invalidate();
    }

    public void setPlayWhenReady(Boolean playWhenReady) {
        this.playWhenReady = playWhenReady;
    }


    public void pause() {
        if (_state != STATE_PAUSED && _state != STATE_STOPPED) {
            // if resume we are updating _resumeAt and no need to take _resumeAt
            if (_state != STATE_RESUME) {
                _resumedAt = android.os.SystemClock.uptimeMillis();
            }
            _state = STATE_PAUSED;
            _currentAnimationTime = 0;
            requestLayout();
            invalidate();
        }
    }

    public void resume() {
        if (_state != STATE_STARTED && _state != STATE_RESUME && _state != STATE_STOPPED) {
            _state = STATE_RESUME;
            requestLayout();
            invalidate();
        }
    }

    public void changeStatePlay() {
        if (!isPlaying()) {
            resume();
        } else {
            pause();
        }
    }

    public void changeStatePlay(Boolean playing) {
        if (playing) {
            resume();
        } else {
            pause();
        }
    }

    private void initial() {
        _state = STATE_STARTED;
        _movie = Movie.decodeStream(getResources().openRawResource(_gifID));
        _currentAnimationTime = 0;
        _gifStartedTime = 0;
        requestLayout();
        invalidate();
        if (!playWhenReady) {
            _resumedAt = android.os.SystemClock.uptimeMillis();
            _state = STATE_PAUSED;
            _currentAnimationTime = 0;
            requestLayout();
            invalidate();
        }
    }

    private long getGifPlayingTime(long hello) {
        int i = 17;
        if (_repeatMode == PLAY_REPEAT) {
            i = (int) (i * _animationMultiplier);
        }
        _resumedAt = hello + i;
        return _resumedAt;
    }
}
