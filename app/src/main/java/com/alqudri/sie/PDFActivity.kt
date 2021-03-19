package com.alqudri.sie

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import java.net.URLEncoder


class PDFActivity : AppCompatActivity() {
    lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
        webView = findViewById(R.id.WV)
        progressBar = findViewById(R.id.pb)
        progressBar.setVisibility(View.VISIBLE)
        webView.getSettings().javaScriptEnabled = true
        webView.getSettings().builtInZoomControls = true
        webView.getSettings().displayZoomControls = false
        webView.setWebChromeClient(WebChromeClient())
        val intent = intent
        val position = intent.getStringExtra("url")
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl(
                    "javascript:(function() { " +
                            "document.querySelector('[role=\"toolbar\"]').remove();})()"
                )
                progressBar.setVisibility(View.GONE)
            }
        })
        var  g: String = URLEncoder.encode(position, "UTF-8")

       if(position != null){
           webView.loadUrl(
               "https://docs.google.com/gview?embedded=true&url="+ g
           )
       }else{
           Log.d("Firee", "Gagaaaaal")
       }
    }
}