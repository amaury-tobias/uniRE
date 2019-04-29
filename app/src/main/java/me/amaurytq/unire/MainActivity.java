package me.amaurytq.unire;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import me.amaurytq.unire.fragments.NewsFragment;
import me.amaurytq.unire.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    //private AppListFragmentManager appListFragmentManager;

    private void changeFragment(Fragment newFragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentByTag(tag);

        if (currentFragment != null && currentFragment.isVisible()) return;

        /*
        if (newFragment instanceof NewsFragment)
            appListFragmentManager = (AppListFragmentManager) newFragment;
        */

        FragmentTransaction ft = fm.beginTransaction();
        //ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.replace(R.id.containerFrameMain, newFragment, tag);
        //ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(new ProfileFragment(), "news");
    }
}
