package com.github.windsekirun.big5personalitydiagnostic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.windsekirun.big5personalitydiagnostic.util.Consts;
import com.github.windsekirun.big5personalitydiagnostic.util.DiagnosticModel;
import com.github.windsekirun.big5personalitydiagnostic.util.Material;
import com.github.windsekirun.big5personalitydiagnostic.util.ModelMarkerView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Big5 Personality Diagnostic
 * class: ResultActivity
 * Created by WindSekirun on 2015. 11. 10..
 */
public class ResultActivity extends AppCompatActivity implements Consts {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.chart)
    RadarChart chart;
    @Bind(R.id.saveButton)
    Button saveButton;
    @Bind(R.id.shareButton)
    Button shareButton;

    DiagnosticModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        model = (DiagnosticModel) getIntent().getSerializableExtra(DiagModel);

        toolbarSetting();
        chartSetting();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    chart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                    Toast.makeText(ResultActivity.this, R.string.activity_result_saved_gallery, Toast.LENGTH_SHORT).show();
                    scanMediaProvider();
                } else {
                    int permissionCheck = ContextCompat.checkSelfPermission(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        chart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                        Toast.makeText(ResultActivity.this, R.string.activity_result_saved_gallery, Toast.LENGTH_SHORT).show();
                        scanMediaProvider();
                    } else {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(ResultActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 72);
                        }
                    }
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmap(chart.getChartBitmap());
            }
        });
    }

    public void saveBitmap(Bitmap bitmap) {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/big5-diagnostic";
        File dir = new File(file_path);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, "Big5 - " + dateFormat.format(currentDate));
        FileOutputStream fOut;

        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, getString(R.string.activity_result_share_intent)));
    }

    public void chartSetting() {
        chart.setDescription("");
        chart.setWebLineWidth(1.5f);
        chart.setWebLineWidthInner(0.75f);
        chart.setWebAlpha(200);

        ModelMarkerView modelMarkerView = new ModelMarkerView(this, R.layout.custom_marker_view);
        chart.setMarkerView(modelMarkerView);

        String[] models = new String[]{"C", "A", "N", "O", "E"};
        int cnt = 5;

        ArrayList<Entry> yVals1 = new ArrayList<>();

        yVals1.add(new Entry(model.getC(), 0));
        yVals1.add(new Entry(model.getA(), 1));
        yVals1.add(new Entry(model.getN(), 2));
        yVals1.add(new Entry(model.getO(), 3));
        yVals1.add(new Entry(model.getE(), 4));

        ArrayList<String> xVals = new ArrayList<>();

        for (int i = 0; i < cnt; i++)
            xVals.add(models[i % models.length]);

        RadarDataSet set1 = new RadarDataSet(yVals1, "Big5");
        set1.setColor(Material.getMaterialCyanColor(300));
        set1.setDrawFilled(true);
        set1.setValueTextSize(0f);
        set1.setLineWidth(2f);

        RadarData data = new RadarData(xVals, set1);
        data.setValueTextSize(0f);
        chart.setRotationEnabled(false);
        chart.setData(data);
        chart.setSkipWebLineCount(4);
        chart.invalidate();
    }

    public void toolbarSetting() {
        toolbar.setTitle(R.string.activity_result_title);
        toolbar.setTitleTextColor(Material.getWhite());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void scanMediaProvider() {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        Toast.makeText(ResultActivity.this, R.string.activity_result_gallery_scanned, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 72: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Date currentDate = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    chart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                    Toast.makeText(ResultActivity.this, R.string.activity_result_saved_gallery, Toast.LENGTH_SHORT).show();
                    scanMediaProvider();
                }
            }
        }
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
}
