package com.boodhram.guideme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class WebViewActivity extends Activity {

    String url = "";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        url = getIntent().getExtras().getString("url",null);
        WebView webView = (WebView) findViewById(R.id.webView1);
        progressDialog =  new ProgressDialog(WebViewActivity.this); // progress dialogue details loaded on oncreate
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                progressDialog.dismiss();
            }
        });

        //to navigate using this particular webview
        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);
        if(url!=null){
            webView.loadUrl(String.valueOf(Uri.parse(url)));
        }

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(progressDialog!=null && !isFinishing() && !progressDialog.isShowing() ){
                    progressDialog.show();
                }

            }
        });


        ImageView back = (ImageView) findViewById(R.id.imgViewId);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        super.onPause();
    }
}
