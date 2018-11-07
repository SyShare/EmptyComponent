package com.youloft.widget.empty

import android.app.Activity
import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.youloft.widget.EmptyLayout
import com.youloft.widget.R

/**
 * Author by Administrator , Date on 2018/10/18.
 * PS: Not easy to write code, please indicate.
 */
class EmptyBuilder(private val context: Context) {
    private val emptyLayout: EmptyLayout
    /**
     * 默认数据提示
     */
    private var defaultDataTip: CharSequence? = null

    /**
     * 默认无数据图片资源
     */
    @DrawableRes
    private var errorRes = R.drawable.widget_pic_empty

    @DrawableRes
    private var networkRes = R.drawable.widget_pic_empty_network

    private var retryListener: View.OnClickListener? = null

    private var loadingColor: Int
    private var loadingColorArray: IntArray? = null

    init {

        this.loadingColor = 0xff3fa542.toInt()
        this.defaultDataTip = context.getString(R.string.widget_list_empty)
        this.emptyLayout = EmptyLayout(context)
    }

    fun setErrorRes(@DrawableRes errorRes: Int): EmptyBuilder {
        this.errorRes = errorRes
        return this
    }

    fun setNetworkRes(@DrawableRes networkRes: Int): EmptyBuilder {
        this.networkRes = networkRes
        return this
    }

    fun setDefaultTip(@StringRes defaultTip: Int): EmptyBuilder {
        this.defaultDataTip = context.getString(defaultTip)
        return this
    }

    fun setLoadingColorArray(@ColorInt loadingColorArray: IntArray): EmptyBuilder {
        this.loadingColorArray = loadingColorArray
        return this
    }

    fun setLoadingColor(@ColorInt loadingColor: Int): EmptyBuilder {
        this.loadingColor = loadingColor
        return this
    }

    fun setDefaultTip(defaultTip: CharSequence): EmptyBuilder {
        this.defaultDataTip = defaultTip
        return this
    }

    fun setRetryListener(retryListener: View.OnClickListener): EmptyBuilder {
        this.retryListener = retryListener
        return this
    }

    /**
     * set target view for root
     */
    fun initPage(targetView: Any): EmptyBuilder {
        var content: ViewGroup? = null
        when (targetView) {
            is Activity -> {    //如果是Activity，获取到android.R.content
                content = targetView.findViewById(android.R.id.content)
            }
            is Fragment -> {    //如果是Fragment获取到parent
                content = (targetView.view)?.parent as ViewGroup
            }
            is ViewGroup -> {        //如果是View，也取到parent
                content = targetView
            }
            else -> {
                throw IllegalArgumentException("targetView must mapping the ViewGroup")
            }
        }
        content?.removeView(emptyLayout)     //将本身content移除，并且把emptyLayout添加到DecorView中去
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        content?.addView(emptyLayout, lp)
        return this
    }

    fun build(): EmptyLayout {
        emptyLayout.defaultTip = defaultDataTip
        emptyLayout.errorRes = errorRes
        emptyLayout.networkRes = networkRes
        emptyLayout.setOnLayoutClickListener(retryListener)
        if (loadingColorArray != null) {
            emptyLayout.setLoadColor(loadingColorArray)
        } else {
            emptyLayout.setLoadColor(loadingColor)
        }
        return emptyLayout
    }
}