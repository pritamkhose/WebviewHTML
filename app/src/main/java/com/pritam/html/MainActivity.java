package com.pritam.html;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    WebView WebView;
    EditText edit_url;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_url = ((EditText) findViewById(R.id.edit_url));
        WebView = ((WebView) findViewById(R.id.webView));

        edit_url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkURL();
                    hideKeyboard();
                    loadPage();
                    handled = true;
                }
                return handled;
            }
        });

        ((Button) findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkURL();
                hideKeyboard();
                loadPage();
            }
        });

        edit_url.setText("https://pritam-react.web.app");
        loadPage();

    }

    private void checkURL() {
        String URL = edit_url.getText().toString();
        if (URL != null && URL.length() > 3) {
            if (URL.startsWith("https://") || URL.startsWith("http://")) {
            } else {
                URL = "https://" + URL;
            }
            edit_url.setText(URL);
        } else {
            snackBarMsg("Invalid URL", true);
        }
    }

    private void hideKeyboard() {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(edit_url, 0);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadPage() {
        String URL = edit_url.getText().toString();
        if (isConnected(MainActivity.this)) {
            WebView.setWebViewClient(new MyBrowser(MainActivity.this));
            WebView.getSettings().setLoadsImagesAutomatically(true);
            WebView.getSettings().setJavaScriptEnabled(true);
            WebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            WebView.loadUrl(URL);
            WebSettings settings = WebView.getSettings();
            settings.setDomStorageEnabled(true);
        } else {
            snackBarMsg("No Internet", false);
        }
    }

    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo.isConnected();
        } catch (Exception e) {
            System.out.println("isConnected:::ERROR::::" + e.getMessage());
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (WebView.copyBackForwardList().getCurrentIndex() > 0) {
            WebView.goBack();
        } else {
            // Your exit alert code, or alternatively line below to finish
            //super.onBackPressed(); // finishes activity
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            snackBarMsg("Please click BACK again to exit", true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    private void snackBarMsg(String s, boolean b) {
        if (b) {
            Snackbar.make(WebView.getRootView(), s, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(WebView.getRootView(), s, Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadPage();
                        }
                    }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_retry) {
            loadPage();
            return true;
        } else if (id == R.id.action_exit) {
            finishAffinity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

