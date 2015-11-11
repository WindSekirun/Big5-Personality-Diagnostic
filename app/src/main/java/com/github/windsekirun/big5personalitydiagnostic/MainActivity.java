package com.github.windsekirun.big5personalitydiagnostic;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.windsekirun.big5personalitydiagnostic.util.Consts;
import com.github.windsekirun.big5personalitydiagnostic.util.DiagnosticModel;
import com.github.windsekirun.big5personalitydiagnostic.util.Material;
import com.github.windsekirun.big5personalitydiagnostic.util.QuestionStorage;
import com.github.windsekirun.big5personalitydiagnostic.util.narae.NaraePreference;
import com.github.windsekirun.big5personalitydiagnostic.util.onFragmentChangeRequest;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity implements Consts, onFragmentChangeRequest {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    Drawer drawer;
    QuestionStorage storage;

    boolean isTempRequired = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        storage = new QuestionStorage(this);

        toolbarSetting();
        drawerSetting();

        fragmentCommit(1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(Material.getMaterialCyanColor(700));
        }
    }

    public void fragmentCommit(int num) {
        QuestionFragment questions = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(QuestNum, num);
        bundle.putSerializable(QuestPair, storage.getPair(num));
        bundle.putString(QuestProgress, " " + num + " / " + Questions);
        questions.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, questions).commit();
    }

    public void drawerSetting() {
        PrimaryDrawerItem question = new PrimaryDrawerItem().withName(R.string.activity_main_questions)
                .withIcon(new IconDrawable(this, FontAwesomeIcons.fa_question_circle).color(0xff727272));
        PrimaryDrawerItem github = new PrimaryDrawerItem().withName(R.string.activity_main_github)
                .withIcon(new IconDrawable(this, FontAwesomeIcons.fa_github).color(0xff727272));
        PrimaryDrawerItem license = new PrimaryDrawerItem().withName(R.string.activity_main_license)
                .withIcon(new IconDrawable(this, FontAwesomeIcons.fa_file_text).color(0xff727272));
        PrimaryDrawerItem maker = new PrimaryDrawerItem().withName(R.string.activity_main_maker)
                .withIcon(new IconDrawable(this, FontAwesomeIcons.fa_user).color(0xff727272));
        PrimaryDrawerItem help = new PrimaryDrawerItem().withName(R.string.activity_main_help)
                .withIcon(new IconDrawable(this, FontAwesomeIcons.fa_info_circle).color(0xff727272));

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withToolbar(toolbar)
                .addDrawerItems(question, new DividerDrawerItem(), github, license, maker, help)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        switch (i) {
                            case 0:
                                drawer.setSelection(0);
                                break;
                            case 2:
                                String url = "https://github.com/WindSekirun/Big5-Personality-Diagnostic";
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                drawer.setSelection(0);
                                break;
                            case 3:
                                Intent license = new Intent(MainActivity.this, LicenseActivity.class);
                                startActivity(license);
                                overridePendingTransition(0, 0);
                                drawer.setSelection(0);
                                break;
                            case 4:
                                Intent maker = new Intent(MainActivity.this, MakerActivity.class);
                                startActivity(maker);
                                overridePendingTransition(0, 0);
                                drawer.setSelection(0);
                                break;
                            case 5:
                                Intent help = new Intent(MainActivity.this, HelpActivity.class);
                                startActivity(help);
                                overridePendingTransition(0, 0);
                                drawer.setSelection(0);
                                break;
                        }
                        return false;
                    }
                }).build();
    }

    public void toolbarSetting() {
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(Material.getWhite());
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this)
                .content(R.string.activity_main_exit_question)
                .positiveText(R.string.activity_main_ok)
                .negativeText(R.string.activity_main_no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        if (isTempRequired) {
                            storage.savePairs();
                            Toast.makeText(MainActivity.this, R.string.storage_question_load_temped, Toast.LENGTH_SHORT).show();
                            NaraePreference np = new NaraePreference(MainActivity.this);
                            np.getValue(tempSave, true);
                        }
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPrev(int nowNum, int checked) {
        fragmentCommit(nowNum - 1);
    }

    @Override
    public void onNext(int nowNum, int checked) {
        storage.writePair(nowNum, checked);
        if (nowNum != 20) {
            isTempRequired = true;
            fragmentCommit(nowNum + 1);
        } else {
            DiagnosticModel model = storage.analyze();
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra(DiagModel, model);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }

    }
}
