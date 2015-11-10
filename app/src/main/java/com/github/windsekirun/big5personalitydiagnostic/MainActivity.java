package com.github.windsekirun.big5personalitydiagnostic;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.windsekirun.big5personalitydiagnostic.util.Consts;
import com.github.windsekirun.big5personalitydiagnostic.util.DiagnosticModel;
import com.github.windsekirun.big5personalitydiagnostic.util.Material;
import com.github.windsekirun.big5personalitydiagnostic.util.QuestionStorage;
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

    long backPressedTime;
    Drawer drawer;
    QuestionStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        storage = new QuestionStorage(this);

        toolbarSetting();
        drawerSetting();

        fragmentCommit(1);
    }

    public void fragmentCommit(int num) {
        QuestionFragment questions = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(QuestNum, num);
        bundle.putSerializable(QuestPair, storage.getPair(num));
        bundle.putFloat(QuestProgress, ((num - 1) / Questions));
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
                            case 1:
                                //TODO: intent to My Github
                                drawer.setSelection(0);
                                break;
                            case 2:
                                //TODO: intent to LicenseActivity
                                drawer.setSelection(0);
                                break;
                            case 3:
                                //TODO: intent to MakerActivity
                                drawer.setSelection(0);
                                break;
                            case 4:
                                //TODO: intent to HelpActivity
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
        super.onBackPressed();
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && intervalTime <= 2000) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, R.string.activity_main_again_exit, Toast.LENGTH_SHORT).show();
        }
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
            fragmentCommit(nowNum + 1);
        } else {
            DiagnosticModel model = storage.analyze();
            //TODO: Intent to ResultActivity
        }

    }
}
