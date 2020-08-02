package com.pritam.html;

import android.app.Activity;
import android.app.ProgressDialog;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyBrowser extends WebViewClient {

    Activity mainActivity;
    ProgressDialog progressDialog;

    public MyBrowser(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        progressDialog = new ProgressDialog(this.mainActivity);
        progressDialog.setMessage("Loading...");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        progressDialog.show();
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}