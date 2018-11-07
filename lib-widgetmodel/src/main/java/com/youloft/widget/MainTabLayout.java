package com.youloft.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.youloft.widget.indicators.AnimatedIndicatorInterface;
import com.youloft.widget.indicators.MainTabIndicator;


/**
 * @author Administrator
 */

public class MainTabLayout extends TabLayout implements ViewPager.OnPageChangeListener {

    private static final String TAG = "DachshundTabLayout";

    private static final int DEFAULT_WIDTH_DP = 50;
    private static final int DEFAULT_HEIGHT_DP = 6;
    int startXLeft, endXLeft, startXCenter, endXCenter, startXRight, endXRight;
    private int indicatorColor;
    private int indicatorHeight;
    private int indicatorWidth;
    private int indicatorStartColor;
    private int indicatorEndColor;
    private boolean isGradient = false;
    private int currentPosition;
    private ViewPager viewPager;
    private LinearLayout tabStrip;
    private AnimatedIndicatorInterface animatedIndicator;

    public MainTabLayout(Context context) {
        this(context, null);
    }

    public MainTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        super.setSelectedTabIndicatorHeight(0);

        tabStrip = (LinearLayout) super.getChildAt(0);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MainTabLayout);

        this.indicatorWidth = a.getDimensionPixelSize(R.styleable.MainTabLayout_indicatorWidth, dp2Px(DEFAULT_WIDTH_DP));
        this.indicatorHeight = a.getDimensionPixelSize(R.styleable.MainTabLayout_indicatorHeight, dp2Px(DEFAULT_HEIGHT_DP));
        this.indicatorColor = a.getColor(R.styleable.MainTabLayout_indicatorColor, Color.WHITE);
        this.isGradient = a.getBoolean(R.styleable.MainTabLayout_gradient, false);
        this.indicatorStartColor = a.getColor(R.styleable.MainTabLayout_indicatorStartColor, Color.WHITE);
        this.indicatorEndColor = a.getColor(R.styleable.MainTabLayout_indicatorEndColor, Color.WHITE);

        a.recycle();
        tabStrip.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (animatedIndicator == null) {
                setAnimatedIndicator(new MainTabIndicator(MainTabLayout.this));
            } else {
                refreshInvalidate();
            }
        });
    }

    private void refreshInvalidate() {
        if (viewPager != null && viewPager.getAdapter() != null) {
            int stripWidth = 0;
            for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
                if (stripWidth != 0 && stripWidth > getStripChildWidth(i)) {
                    stripWidth = getStripChildWidth(i);
                }
            }
            if (stripWidth != 0) {
                indicatorWidth = stripWidth;
            }
            animatedIndicator.setSelectedTabIndicatorWidth(indicatorWidth);
            invalidate();
        }
        animatedIndicator.mRedraw();
    }

    @Override
    public void setSelectedTabIndicatorColor(@ColorInt int color) {
        this.indicatorColor = color;
        if (animatedIndicator != null && !isGradient) {
            animatedIndicator.setSelectedTabIndicatorColor(color);

            invalidate();
        }
    }

    @Override
    public void setSelectedTabIndicatorHeight(int height) {
        this.indicatorHeight = height;
        if (animatedIndicator != null) {
            animatedIndicator.setSelectedTabIndicatorHeight(height);

            invalidate();
        }

    }

    @Override
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        this.setupWithViewPager(viewPager, true);
    }

    @Override
    public void setupWithViewPager(@Nullable final ViewPager viewPager, boolean autoRefresh) {
        super.setupWithViewPager(viewPager, autoRefresh);

        //TODO
        if (viewPager != null) {
            if (viewPager != this.viewPager) {
                viewPager.removeOnPageChangeListener(this);
                viewPager.addOnPageChangeListener(this);
                this.viewPager = viewPager;
            }

            if (animatedIndicator != null) {
                int stripWidth = 0;
                for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
                    if (stripWidth != 0 && stripWidth > getStripChildWidth(i)) {
                        stripWidth = getStripChildWidth(i);
                    }
                }
                if (stripWidth != 0) {
                    indicatorWidth = stripWidth;
                }
                animatedIndicator.setSelectedTabIndicatorWidth(indicatorWidth);
                invalidate();
            }

        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (animatedIndicator != null) {
            animatedIndicator.draw(canvas);
        }
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset,
                               final int positionOffsetPixels) {

        if ((position > currentPosition) || (position + 1 < currentPosition)) {
            currentPosition = position;
        }

        if (position != currentPosition) {

            startXLeft = (int) getChildXLeft(currentPosition);
            startXCenter = (int) getChildXCenter(currentPosition);
            startXRight = (int) getChildXRight(currentPosition);

            endXLeft = (int) getChildXLeft(position);
            endXRight = (int) getChildXRight(position);
            endXCenter = (int) getChildXCenter(position);

            if (animatedIndicator != null) {
                animatedIndicator.setIntValues(startXLeft, endXLeft,
                        startXCenter, endXCenter,
                        startXRight, endXRight);
                animatedIndicator.setCurrentPlayTime((long) ((1 - positionOffset) * (int) animatedIndicator.getDuration()));
            }

        } else {

            startXLeft = (int) getChildXLeft(currentPosition);
            startXCenter = (int) getChildXCenter(currentPosition);
            startXRight = (int) getChildXRight(currentPosition);

            if (tabStrip.getChildAt(position + 1) != null) {

                endXLeft = (int) getChildXLeft(position + 1);
                endXCenter = (int) getChildXCenter(position + 1);
                endXRight = (int) getChildXRight(position + 1);

            } else {
                endXLeft = (int) getChildXLeft(position);
                endXCenter = (int) getChildXCenter(position);
                endXRight = (int) getChildXRight(position);
            }
            if (animatedIndicator != null) {
                animatedIndicator.setIntValues(startXLeft, endXLeft,
                        startXCenter, endXCenter,
                        startXRight, endXRight);
                animatedIndicator.setCurrentPlayTime((long) (positionOffset * (int) animatedIndicator.getDuration()));
            }

        }

        if (positionOffset == 0) {
            currentPosition = position;
        }

    }

    @Override
    public void onPageSelected(final int position) {
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getStripChildWidth(int position) {
        if (tabStrip.getChildAt(position) != null) {
            return tabStrip.getChildAt(position).getWidth();
        }
        return 0;
    }

    public float getChildXLeft(int position) {
        if (tabStrip.getChildAt(position) != null)
            return (tabStrip.getChildAt(position).getX());
        else
            return 0;
    }

    public float getChildXCenter(int position) {
        if (tabStrip.getChildAt(position) != null)
            return (tabStrip.getChildAt(position).getX() + tabStrip.getChildAt(position).getWidth() / 2);
        else
            return 0;
    }

    public float getChildXRight(int position) {
        if (tabStrip.getChildAt(position) != null)
            return (tabStrip.getChildAt(position).getX() + tabStrip.getChildAt(position).getWidth());
        else
            return 0;
    }

    public AnimatedIndicatorInterface getAnimatedIndicator() {
        return animatedIndicator;
    }

    public void setAnimatedIndicator(AnimatedIndicatorInterface animatedIndicator) {
        this.animatedIndicator = animatedIndicator;

        animatedIndicator.setSelectedTabIndicatorColor(indicatorColor);
        animatedIndicator.setSelectedTabIndicatorHeight(indicatorHeight);
        animatedIndicator.setGradient(indicatorStartColor, indicatorEndColor, isGradient);

        if (viewPager != null && viewPager.getAdapter() != null) {
            int stripWidth = 0;
            for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
                if (stripWidth != 0 && stripWidth > getStripChildWidth(i)) {
                    stripWidth = getStripChildWidth(i);
                }
            }
            if (stripWidth != 0) {
                indicatorWidth = stripWidth;
            }
            animatedIndicator.setSelectedTabIndicatorWidth(indicatorWidth);
            invalidate();
        }
        invalidate();
    }

    private int dp2Px(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

}
