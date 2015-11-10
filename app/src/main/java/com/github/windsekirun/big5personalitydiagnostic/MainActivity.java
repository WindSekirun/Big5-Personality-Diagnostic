package com.github.windsekirun.big5personalitydiagnostic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.windsekirun.big5personalitydiagnostic.util.Material;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    long backPressedTime;
    Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbarSetting();
        drawerSetting();
    }

    public void drawerSetting() {
        PrimaryDrawerItem question = new PrimaryDrawerItem().withName(R.string.activity_main_questions);
        PrimaryDrawerItem github = new PrimaryDrawerItem().withName(R.string.activity_main_github);
        PrimaryDrawerItem license = new PrimaryDrawerItem().withName(R.string.activity_main_license);
        PrimaryDrawerItem maker = new PrimaryDrawerItem().withName(R.string.activity_main_maker);
        PrimaryDrawerItem help = new PrimaryDrawerItem().withName(R.string.activity_main_help);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .withToolbar(toolbar)
                .addDrawerItems(question, new DividerDrawerItem(), github, license, maker, help)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        return false;
                    }
                }).build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
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
}
