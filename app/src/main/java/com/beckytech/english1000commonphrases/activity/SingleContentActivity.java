package com.beckytech.english1000commonphrases.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.beckytech.english1000commonphrases.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Locale;

public class SingleContentActivity extends AppCompatActivity {

    private WebView webView;
    private ImageView search;
    private String title;
    private String url;
    private ProgressBar progressBar;
    private TextToSpeech textToSpeech;
    private TextView textTitle;
    private final String loadTitle = "Loading...";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_content);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        title = intent.getStringExtra("passedValue");

        textTitle = findViewById(R.id.tv_title_single_content);
        textTitle.setText(title);

        ImageView add_fav = findViewById(R.id.add_fav_single_content_image);
        ImageView copy = findViewById(R.id.copy_image_single_content);

        copy.setOnClickListener(v -> copyTheText());

        ImageView play = findViewById(R.id.sound_play_single_image);

        play.setOnClickListener(v -> textToSpeech = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.getDefault());
                textToSpeech.speak(title, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }));

        search = findViewById(R.id.search_third_party_images);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        webView = findViewById(R.id.webView_google_search);
        webView.loadUrl("https://www.google.com/search?q=" + title);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                textTitle.setText(loadTitle);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                textTitle.setText(view.getTitle());
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);

        findViewById(R.id.back_button_single_content).setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                super.onBackPressed();
            }
        });

        search.setOnClickListener(v -> startSearching());
    }

    private void copyTheText() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("textToBeCopied", title);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
    }

    private void startSearching() {
        PopupMenu popupMenu = new PopupMenu(this, search);
        popupMenu.getMenuInflater().inflate(R.menu.search_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.bing_search) {
                url = "https://www.bing.com/search?q=" + title;
                webView.loadUrl(url);
            }

            if (id == R.id.collins_search) {
                url = "https://www.collinsdictionary.com/dictionary/english/" + title;
                webView.loadUrl(url);
            }

            if (id == R.id.wikibooks_search) {
                url = "https://en.wikibooks.org/w/index.php?search=" + title;
                webView.loadUrl(url);
            }

            if (id == R.id.pinterest) {
                url = "https://www.pinterest.com/search/pins/?q=" + title;
                webView.loadUrl(url);
            }

            if (id == R.id.word_reference_search) {
                url = "https://www.wordreference.com/es/translation.asp?tranword=" + title;
                webView.loadUrl(url);
            }

            if (id == R.id.youtube_search) {
                url = "https://www.youtube.com/results?search_query=" + title;
                webView.loadUrl(url);
            }
            if (id == R.id.google_translator_search) {
                url = "https://translate.google.com/?sl=en&tl=om&text=" + title + "&op=translate";
                webView.loadUrl(url);
            }

            return true;
        });
        popupMenu.show();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}