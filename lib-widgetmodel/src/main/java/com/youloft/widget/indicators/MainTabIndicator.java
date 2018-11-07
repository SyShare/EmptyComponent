package com.youloft.widget.indicators;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.youloft.widget.MainTabLayout;


/**
 * @author Administrator
 */

public class MainTabIndicator implements AnimatedIndicatorInterface, ValueAnimator.AnimatorUpdateListener {

    private Paint paint;
    private RectF rectF;
    private Rect rect;
    private int indicatorStartColor;
    private int indicatorEndColor;
    private boolean isGradient = false;

    private int height;
    private int width;

    private ValueAnimator valueAnimatorLeft, valueAnimatorRight;

    private MainTabLayout mainTabLayout;

    private AccelerateInterpolator accelerateInterpolator;
    private DecelerateInterpolator decelerateInterpolator;

    private int leftX, rightX;

    public MainTabIndicator(MainTabLayout mainTabLayout) {
        this.mainTabLayout = mainTabLayout;

        valueAnimatorLeft = new ValueAnimator();
        valueAnimatorLeft.setDuration(DEFAULT_DURATION);
        valueAnimatorLeft.addUpdateListener(this);

        valueAnimatorRight = new ValueAnimator();
        valueAnimatorRight.setDuration(DEFAULT_DURATION);
        valueAnimatorRight.addUpdateListener(this);

        accelerateInterpolator = new AccelerateInterpolator();
        decelerateInterpolator = new DecelerateInterpolator();

        rectF = new RectF();
        rect = new Rect();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);


        leftX = (int) mainTabLayout.getChildXCenter(mainTabLayout.getCurrentPosition());
        rightX = leftX;
    }

    @Override
    public void mRedraw() {
        if (leftX == 0) {
            leftX = (int) mainTabLayout.getChildXCenter(mainTabLayout.getCurrentPosition());
            rightX = leftX;
        }
    }

    /**
     * 更新渐变色
     */
    private void updateGradient() {
        if (!isGradient) {
            return;
        }
        LinearGradient linearGradient = new LinearGradient(0, height / 2, rect.width(), height / 2,
                indicatorStartColor, indicatorEndColor, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        leftX = (int) valueAnimatorLeft.getAnimatedValue();
        rightX = (int) valueAnimatorRight.getAnimatedValue();

        rect.top = mainTabLayout.getHeight() - height;
        rect.left = leftX - width / 2;
        rect.right = rightX + width / 2;
        rect.bottom = mainTabLayout.getHeight();
        updateGradient();

        mainTabLayout.invalidate(rect);
    }

    @Override
    public void setSelectedTabIndicatorColor(@ColorInt int color) {
        paint.setColor(color);
    }

    @Override
    public void setSelectedTabIndicatorHeight(int height) {
        this.height = height;
    }

    @Override
    public void setGradient(int startColor, int endColor, boolean isGradient) {
        this.indicatorStartColor = startColor;
        this.indicatorEndColor = endColor;
        this.isGradient = isGradient;
        updateGradient();
    }

    @Override
    public void setSelectedTabIndicatorWidth(int width) {
        this.width = width;
    }

    @Override
    public void setIntValues(int startXLeft, int endXLeft,
                             int startXCenter, int endXCenter,
                             int startXRight, int endXRight) {
        boolean toRight = endXCenter - startXCenter >= 0;

        if (toRight) {
            valueAnimatorLeft.setInterpolator(accelerateInterpolator);
            valueAnimatorRight.setInterpolator(decelerateInterpolator);
        } else {
            valueAnimatorLeft.setInterpolator(decelerateInterpolator);
            valueAnimatorRight.setInterpolator(accelerateInterpolator);
        }

        valueAnimatorLeft.setIntValues(startXCenter, endXCenter);
        valueAnimatorRight.setIntValues(startXCenter, endXCenter);
    }

    @Override
    public void setCurrentPlayTime(long currentPlayTime) {
        valueAnimatorLeft.setCurrentPlayTime(currentPlayTime);
        valueAnimatorRight.setCurrentPlayTime(currentPlayTime);
    }

    @Override
    public void draw(Canvas canvas) {

        rectF.top = mainTabLayout.getHeight() - height;
        rectF.left = leftX - width / 2;
        rectF.right = rightX + width / 2;
        rectF.bottom = mainTabLayout.getHeight();
        updateGradient();
        canvas.drawRoundRect(rectF, height, height, paint);
    }

    @Override
    public long getDuration() {
        return valueAnimatorLeft.getDuration();
    }
}
