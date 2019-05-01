package me.amaurytq.unire;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import me.amaurytq.unire.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    public void onBackPressed() {}

    private void changeFragment(Fragment newFragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentByTag(tag);
        if (currentFragment != null && currentFragment.isVisible()) return;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.containerFrameMain, newFragment, tag);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(ProfileFragment.newInstance("",""), "news");
    }
}
