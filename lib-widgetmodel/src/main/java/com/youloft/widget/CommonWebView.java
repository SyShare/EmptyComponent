package com.youloft.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.youlu.util.NetUtil;

/**
 * @author 18721
 */

public class CommonWebView extends WebView {

    private WebViewLoadingListener webViewLoadingListener;
    private WebSettings settings;

    public CommonWebView(Context context) {
        super(context);
        init();
    }

    public CommonWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommonWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public CommonWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void init() {

        WebSettings settings = getSettings();
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(false);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);

        setWebViewClient(new MyWebViewClient());
        setWebChromeClient(new MyWebChromeClient());
    }


    @Override
    public void destroy() {
        setWebViewClient(null);

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(false);

        removeJavascriptInterface("mWebViewImageListener");
        removeAllViewsInLayout();

        removeAllViews();
        //clearCache(true);

        super.destroy();
    }


    /**
     * 设置监听事件
     *
     * @param webViewLoadingListener
     */
    public void setWebViewLoadingListener(WebViewLoadingListener webViewLoadingListener) {
        this.webViewLoadingListener = webViewLoadingListener;
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (webViewLoadingListener != null) {
                webViewLoadingListener.getWebViewTitle(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) { // 进度
            super.onProgressChanged(view, newProgress);
            if (webViewLoadingListener != null) {
                webViewLoadingListener.onProgress(newProgress);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (webViewLoadingListener != null) {
                webViewLoadingListener.showUrlRedirect(view, url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (webViewLoadingListener != null) {
                webViewLoadingListener.onPageFinished(view, url);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (webViewLoadingListener != null) {
                webViewLoadingListener.onPageStart(view, url, favicon);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (webViewLoadingListener != null) {
                webViewLoadingListener.onReceiveError(view, request);
            }
        }


        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (webViewLoadingListener != null) {
                webViewLoadingListener.onReceiveError(view, request);
            }
        }
    }


    /**
     * xiang相对应的监听回调
     */
    public interface WebViewLoadingListener {
        /**
         * 开始
         *
         * @param view
         * @param url
         * @param favicon
         */
        void onPageStart(WebView view, String url, Bitmap favicon);

        /**
         * 回调进度
         *
         * @param newProgress
         */
        void onProgress(int newProgress);

        /**
         * 完成
         *
         * @param view
         * @param url
         */
        void onPageFinished(WebView view, String url);

        /**
         * 拦截处理url事件
         * @return
         */
        boolean showUrlRedirect(WebView view, String url);

        /**
         * 获取title
         *
         * @param title
         */
        void getWebViewTitle(String title);


        /**
         * 接收错误信息
         *
         * @param view
         * @param request
         */
        void onReceiveError(WebView view, WebResourceRequest request);

    }

    public static final class UrlArgument implements Parcelable {
        public final String argument;
        public final String value;

        public UrlArgument(String argument, String value) {
            this.argument = argument;
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeString(argument);
            out.writeString(value);
        }

        public static final Creator<UrlArgument> CREATOR
                = new Creator<UrlArgument>() {
            @Override
            public UrlArgument createFromParcel(Parcel in) {
                return new UrlArgument(in);
            }

            @Override
            public UrlArgument[] newArray(int size) {
                return new UrlArgument[size];
            }
        };

        private UrlArgument(Parcel in) {
            this.argument = in.readString();
            this.value = in.readString();
        }
    }

}
