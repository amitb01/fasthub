package com.mapprr.fasthub.shared.webView.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapprr.fasthub.R;
import com.mapprr.fasthub.core.view.MvpActivity;
import com.mapprr.fasthub.shared.webView.presenter.WebViewPresenter;

import butterknife.Bind;

public class WebViewActivity extends MvpActivity<WebViewPresenter> implements WebViewPresenter.View {

    private static final String KEY_URL = "url";

    private String url;

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_web_view_activity)
    TextView title;
    @Bind(R.id.subtitle_web_view_activity)
    TextView subTitle;
    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.progress_loader)
    RelativeLayout progressLoader;

    public static Intent getCallingIntent(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KEY_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        init();
        initWebView();
        backBtn.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public WebViewPresenter createPresenter(Bundle savedInstanceState) {
        return new WebViewPresenter();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_web_view;
    }

    private void init() {
        Intent intent = getIntent();
        url = intent.getStringExtra(KEY_URL);
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setDomStorageEnabled(true);

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.cancel();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressLoader.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                subTitle.setText(url);
                progressLoader.setVisibility(View.VISIBLE);
            }
        });

        if (!url.startsWith("http")) {
            url = String.format("http://%s", url);
        }
        webView.loadUrl(url);
    }
}
