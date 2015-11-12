package com.github.windsekirun.big5personalitydiagnostic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
    @Bind(R.id.mailButton)
    Button mailButton;

    DiagnosticModel model;

    LineChart lineChart;
    NaraePreference np;
    MaterialDialog loadingDialog;
    EditText idBox, passwordBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        model = (DiagnosticModel) getIntent().getSerializableExtra(DiagModel);
        np = new NaraePreference(this);

        toolbarSetting();
        lineChartSetting();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    lineChart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                    Toast.makeText(ResultActivity.this, R.string.activity_result_saved_gallery, Toast.LENGTH_SHORT).show();
                } else {
                    int permissionCheck = ContextCompat.checkSelfPermission(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
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
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    shareBitmap(saveBitmap(lineChart.getChartBitmap()).first);
                } else {
                    int permissionCheck = ContextCompat.checkSelfPermission(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        shareBitmap(saveBitmap(lineChart.getChartBitmap()).first);
                    } else {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(ResultActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 74);
                        }
                    }
                }
            }
        });

        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMailDialog();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(Material.getMaterialCyanColor(700));
        }
    }

    public void showMailDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.dialog_mail_address, null);
        TextView saveButton = (TextView) v.findViewById(R.id.saveButton);
        TextView exitButton = (TextView) v.findViewById(R.id.exitButton);
        final CheckBox saveId = (CheckBox) v.findViewById(R.id.saveId);
        idBox = (EditText) v.findViewById(R.id.mail_email);
        passwordBox = (EditText) v.findViewById(R.id.mail_password);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isOnline()) {
                    if (saveId.isChecked()) np.put(savedId, idBox.getText().toString());

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        new sendMailing().execute();
                    } else {
                        int permissionCheck = ContextCompat.checkSelfPermission(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            new sendMailing().execute();
                        } else {
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ActivityCompat.requestPermissions(ResultActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 74);
                            }
                        }
                    }
                } else {
                    Toast.makeText(ResultActivity.this, "Check Network State", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(v);
        dialog.show();
    }

    public void shareBitmap(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, getString(R.string.activity_result_share_intent)));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Pair<File, String> saveBitmap(Bitmap bitmap) {
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

        return new Pair<>(file, file.getPath());
    }

    public ArrayList<Entry> getY1() {
        ArrayList<Entry> yVals1 = new ArrayList<>();
        yVals1.add(new Entry(model.getC(), 0));
        yVals1.add(new Entry(model.getA(), 1));
        yVals1.add(new Entry(model.getN(), 2));
        yVals1.add(new Entry(model.getO(), 3));
        yVals1.add(new Entry(model.getE(), 4));
        return yVals1;
    }

    public ArrayList<Entry> getY2() {
        ArrayList<Entry> yVals2 = new ArrayList<>();
        yVals2.add(new Entry(14, 0));
        yVals2.add(new Entry(16, 1));
        yVals2.add(new Entry(10, 2));
        yVals2.add(new Entry(15, 3));
        yVals2.add(new Entry(13, 4));
        return yVals2;
    }

    public ArrayList<Entry> getY3() {
        ArrayList<Entry> yVals3 = new ArrayList<>();
        yVals3.add(new Entry(20, 0));
        yVals3.add(new Entry(20, 1));
        yVals3.add(new Entry(20, 2));
        yVals3.add(new Entry(20, 3));
        yVals3.add(new Entry(20, 4));
        return yVals3;
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

        ArrayList<Entry> yVals1 = getY1();
        ArrayList<Entry> yVals2 = getY2();
        ArrayList<Entry> yVals3 = getY3();

        LineDataSet set1 = new LineDataSet(yVals1, "Big5");
        set1.setColor(Material.getMaterialCyanColor(500));
        set1.setCircleColor(Material.getMaterialCyanAccentColor(700));
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Material.getMaterialCyanColor(500));

        LineDataSet set2 = new LineDataSet(yVals2, "Norm");
        set2.setColor(Material.getMaterialOrangeColor(500));
        set2.setCircleColor(Material.getMaterialOrangeAccentColor(700));
        set2.setLineWidth(2f);
        set2.setCircleSize(4f);
        set2.setValueTextSize(9f);
        set2.setFillAlpha(65);
        set2.setFillColor(Material.getMaterialCyanColor(500));

        LineDataSet set3 = new LineDataSet(yVals3, "");
        set3.setLineWidth(0f);
        set3.setCircleSize(0f);
        set3.setValueTextSize(0f);
        set3.setFillAlpha(0);
        set3.setColor(0x00000000);
        set3.setCircleColor(0x00000000);
        set3.setFillColor(0x00000000);

        ArrayList<LineDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);
        sets.add(set3);

        LineData data = new LineData(xVals, sets);

        lineChart.setData(data);

        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lineChart.setLayoutParams(params);

        if (chartView.getChildCount() != 0) chartView.removeAllViews();
        chartView.addView(lineChart);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 72: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Date currentDate = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    lineChart.saveToGallery("Big5 - " + dateFormat.format(currentDate), 100);
                    Toast.makeText(ResultActivity.this, R.string.activity_result_saved_gallery, Toast.LENGTH_SHORT).show();
                }
            }
            case 74: {
                shareBitmap(saveBitmap(lineChart.getChartBitmap()).first);
            }
            case 765: {
                new sendMailing().execute();
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

    public class sendMailing extends AsyncTask<Void, Void, Integer> {
        String idText;
        String passText;

        Bitmap line = lineChart.getChartBitmap();

        @Override
        protected Integer doInBackground(Void... params) {
            if (!idText.isEmpty() && !passText.isEmpty()) {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(idText, passText);
                            }
                        });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(idText));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
                    message.setSubject("[Big5] Sending Data from " + idText);
                    message.setText("Sent by Big5 Application");

                    MimeBodyPart messageBodyPart = new MimeBodyPart();

                    Multipart multipart = new MimeMultipart();

                    Pair<File, String> pairs = saveBitmap(line);

                    File file = pairs.first;
                    String fileName = pairs.second;
                    DataSource source = new FileDataSource(file);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(fileName);
                    multipart.addBodyPart(messageBodyPart);

                    message.setContent(multipart);

                    Transport.send(message);
                    return 0;
                } catch (MessagingException e) {
                    return 2;
                }
            } else {
                return 1;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = new MaterialDialog.Builder(ResultActivity.this)
                    .content(R.string.dialog_mail_sending)
                    .progress(false, 0)
                    .build();

            loadingDialog.show();

            idText = idBox.getText().toString();
            passText = passwordBox.getText().toString();
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
            switch (aVoid) {
                case 1:
                    Toast.makeText(ResultActivity.this, R.string.activity_result_message_1, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(ResultActivity.this, R.string.activity_result_message_2, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(ResultActivity.this, R.string.activity_result_message_0, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
