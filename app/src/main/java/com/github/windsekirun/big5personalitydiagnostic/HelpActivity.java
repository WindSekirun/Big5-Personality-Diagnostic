package com.github.windsekirun.big5personalitydiagnostic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.github.windsekirun.big5personalitydiagnostic.util.Material;

import butterknife.ButterKnife;

/**
 * Big5 Personality Diagnostic
 * class: LicenseActivity
 * Created by WindSekirun on 2015. 11. 10..
 */
@SuppressWarnings("ConstantConditions")
public class HelpActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        toolbarSetting();

        webView.loadUrl("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toolbarSetting() {
        toolbar.setTitle(R.string.activity_main_help);
        toolbar.setTitleTextColor(Material.getWhite());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
