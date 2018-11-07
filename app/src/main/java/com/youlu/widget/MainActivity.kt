package com.youlu.widget

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.youloft.widget.EmptyLayout
import com.youloft.widget.empty.EmptyBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    val empty by lazy {
        EmptyBuilder(this)
                .initPage(emptyll)
                .setDefaultTip("没有数据")
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        empty.setErrorType(EmptyLayout.HIDE_LAYOUT)
        showRetry.setOnClickListener {
            empty.setErrorType(EmptyLayout.NETWORK_LOADING)
        }


        showNetWork.setOnClickListener {
            empty.setErrorType(EmptyLayout.NETWORK_ERROR)
        }


        show.setOnClickListener {
            empty.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK)
        }

//        empty.setOnLayoutClickListener {
//            empty.setErrorType(EmptyLayout.NETWORK_LOADING)
//        }

    }
}
