package com.youloft.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.youloft.widget.empty.Loading
import com.youlu.util.NetUtil

/**
 * Author by Administrator , Date on 2018/10/18.
 * PS: Not easy to write code, please indicate.
 */
open class EmptyLayout : LinearLayout, android.view.View.OnClickListener {


    companion object {
        const val HIDE_LAYOUT = 4
        const val NETWORK_ERROR = 1
        const val NETWORK_LOADING = 2
        const val NODATA = 3
        const val NODATA_ENABLE_CLICK = 5
    }

    private lateinit var img: ImageView
    private lateinit var mLoading: Loading
    private lateinit var tv: TextView

    private var clickEnable = true
    private var listener: android.view.View.OnClickListener? = null

    var defaultTip: CharSequence? = null
    var errorRes: Int = 0
    var networkRes: Int = 0
    private var mErrorState: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.widget_view_error_layout, this, true)
        img = view.findViewById<View>(R.id.widget_img_error_layout) as ImageView
        tv = view.findViewById<View>(R.id.widget_tv_error_layout) as TextView
        mLoading = view.findViewById<View>(R.id.widget_animProgress) as Loading
        setBackgroundColor(-1)
        setOnClickListener(this)
        img.setOnClickListener { v ->
            if (clickEnable) {
                listener?.onClick(v)
            }
        }
    }


    fun getErrorState(): Int {
        return mErrorState
    }

    fun isLoadError(): Boolean {
        return mErrorState == NETWORK_ERROR
    }

    fun isLoading(): Boolean {
        return mErrorState == NETWORK_LOADING
    }


    fun setErrorMessage(msg: String) {
        tv.text = msg
    }

    fun setErrorImag(imgResource: Int) {
        try {
            img.setImageResource(imgResource)
        } catch (e: Exception) {
        }

    }

    fun setLoadColor(color: Int) {
        mLoading.setForegroundColor(color)
    }

    fun setLoadColor(color: IntArray?) {
        mLoading.foregroundColor = color
    }


    fun setErrorType(i: Int) {
        visibility = View.VISIBLE
        when (i) {
            NETWORK_ERROR -> {
                mErrorState = NETWORK_ERROR
                if (NetUtil.isNetworkAvailable(context)) {
                    tv.setText(R.string.widget_error_view_load_error_click_to_refresh)
                    img.setBackgroundResource(errorRes)
                } else {
                    tv.setText(R.string.widget_network_disconnected_tips)
                    img.setBackgroundResource(networkRes)
                }
                img.visibility = View.VISIBLE
                mLoading.stop()
                mLoading.visibility = View.GONE
                clickEnable = true
            }
            NETWORK_LOADING -> {
                mErrorState = NETWORK_LOADING
                mLoading.visibility = View.VISIBLE
                mLoading.start()
                img.visibility = View.GONE
                tv.setText(R.string.widget_error_view_loading)
                clickEnable = false
            }
            NODATA -> {
                mErrorState = NODATA
                img.setBackgroundResource(errorRes)
                img.visibility = View.VISIBLE
                mLoading.stop()
                mLoading.visibility = View.GONE
                setTvNoDataContent()
                clickEnable = true
            }
            HIDE_LAYOUT -> {
                mLoading.stop()
                visibility = View.GONE
            }
            NODATA_ENABLE_CLICK -> {
                mErrorState = NODATA_ENABLE_CLICK
                img.setBackgroundResource(errorRes)
                img.visibility = View.VISIBLE
                mLoading.stop()
                mLoading.visibility = View.GONE
                setTvNoDataContent()
                clickEnable = true
            }
            else -> {
            }
        }
    }


    fun setOnLayoutClickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

    private fun setTvNoDataContent() {
        tv.text = defaultTip
    }

    override fun setVisibility(visibility: Int) {
        if (visibility == View.GONE)
            mErrorState = HIDE_LAYOUT
        super.setVisibility(visibility)
    }

    override fun onClick(v: View?) {
        if (clickEnable) {
            listener?.onClick(v)
        }
    }

}

