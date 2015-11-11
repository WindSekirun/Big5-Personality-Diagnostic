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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.windsekirun.big5personalitydiagnostic.util.Consts;
import com.github.windsekirun.big5personalitydiagnostic.util.DiagnosticModel;
import com.github.windsekirun.big5personalitydiagnostic.util.Material;
import com.github.windsekirun.big5personalitydiagnostic.util.ModelMarkerView;
import com.github.windsekirun.big5personalitydiagnostic.util.narae.NaraePreference;

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
    RelativeLayout chartView;
    @Bind(R.id.saveButton)
    Button saveButton;
    @Bind(R.id.shareButton)
    Button shareButton;
    @Bind(R.id.shapeButton)
    Button shapeButton;

    DiagnosticModel model;
    boolean isSpdier = true;

    RadarChart radarChart;
    LineChart lineChart;
    NaraePreference np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        model = (DiagnosticModel) getIntent().getSerializableExtra(DiagModel);
        np = new NaraePreference(this);
        isSpdier = np.getValue(WebShape, true);

        toolbarSetting();
        if (isSpdier) {
            radarChartSetting();
        } else {
            lineChartSetting();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    if (isSpdier)
                        radarChart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                    else
                        lineChart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                    Toast.makeText(ResultActivity.this, R.string.activity_result_saved_gallery, Toast.LENGTH_SHORT).show();
                } else {
                    int permissionCheck = ContextCompat.checkSelfPermission(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        if (isSpdier)
                            radarChart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                        else
                            lineChart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                        Toast.makeText(ResultActivity.this, R.string.activity_result_saved_gallery, Toast.LENGTH_SHORT).show();
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
                int permissionCheck = ContextCompat.checkSelfPermission(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    if (isSpdier)
                        saveBitmap(radarChart.getChartBitmap());
                    else
                        saveBitmap(lineChart.getChartBitmap());
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(ResultActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 72);
                    }
                }
            }
        });

        shapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                np.put(WebShape, !isSpdier);
                Intent i = new Intent(ResultActivity.this, ResultActivity.class);
                i.putExtra(DiagModel, model);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(Material.getMaterialCyanColor(700));
        }
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

    public void radarChartSetting() {
        radarChart = new RadarChart(this);
        radarChart.setDescription("");
        radarChart.setWebLineWidth(1.5f);
        radarChart.setWebLineWidthInner(0.75f);
        radarChart.setWebAlpha(200);

        ModelMarkerView modelMarkerView = new ModelMarkerView(this, R.layout.custom_marker_view);
        radarChart.setMarkerView(modelMarkerView);

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
        radarChart.setRotationEnabled(false);
        radarChart.setData(data);
        radarChart.setSkipWebLineCount(4);
        radarChart.invalidate();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        radarChart.setLayoutParams(params);

        if (chartView.getChildCount() != 0) chartView.removeAllViews();
        chartView.addView(radarChart);
    }

    public void lineChartSetting() {
        lineChart = new LineChart(this);
        lineChart.setDescription("");
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);

        ModelMarkerView mv = new ModelMarkerView(this, R.layout.custom_marker_view);

        lineChart.setMarkerView(mv);

        lineChart.getAxisRight().setEnabled(false);

        ArrayList<String> xVals = new ArrayList<>();
        String[] models = new String[]{"C", "A", "N", "O", "E"};

        for (int i = 0; i < 5; i++) {
            xVals.add(models[i % models.length]);
        }

        ArrayList<Entry> yVals = new ArrayList<>();

        yVals.add(new Entry(model.getC(), 0));
        yVals.add(new Entry(model.getA(), 1));
        yVals.add(new Entry(model.getN(), 2));
        yVals.add(new Entry(model.getO(), 3));
        yVals.add(new Entry(model.getE(), 4));

        LineDataSet set1 = new LineDataSet(yVals, "Big5");

        set1.setColor(Material.getMaterialCyanColor(500));
        set1.setCircleColor(Material.getMaterialCyanAccentColor(700));
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Material.getMaterialCyanColor(500));

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(xVals, dataSets);

        lineChart.setData(data);

        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lineChart.setLayoutParams(params);

        if (chartView.getChildCount() != 0) chartView.removeAllViews();
        chartView.addView(lineChart);
    }

    @SuppressWarnings("ConstantConditions")
    public void toolbarSetting() {
        toolbar.setTitle(R.string.activity_result_title);
        toolbar.setTitleTextColor(Material.getWhite());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    if (isSpdier)
                        radarChart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                    else
                        lineChart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                    Toast.makeText(ResultActivity.this, R.string.activity_result_saved_gallery, Toast.LENGTH_SHORT).show();
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
