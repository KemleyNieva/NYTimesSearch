package com.codepath.nytimessearch.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.nytimessearch.Article;
import com.codepath.nytimessearch.R;

import org.parceler.Parcel;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.wvArticle) WebView webView;
    //@BindView(R.id.wvArticle) WebView wvArticle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));

        //WebView webView = (WebView) findViewById(R.id.wvArticle);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(article.getWebUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // get reference to WebView
        //WebView wvArticle = (WebView) findViewById(R.id.wvArticle);
        // pass in the URL currently being used by the WebView
        if(webView != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
            miShare.setShareIntent(shareIntent);

        }
        return super.onCreateOptionsMenu(menu);
    }

}
