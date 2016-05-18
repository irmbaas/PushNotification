package ir.mbaas.pushnotification;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import ir.mbaas.pushnotification.adapter.ViewPagerAdapter;
import ir.mbaas.pushnotification.fragment.HelpFragment;
import ir.mbaas.pushnotification.fragment.IntroFragment;
import ir.mbaas.pushnotification.fragment.ReportFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_info,
            R.drawable.ic_action_book,
            R.drawable.ic_view_list,
            R.drawable.ic_contact_phone
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new IntroFragment(), getResources().getString(R.string.intro_tab_title));
        adapter.addFragment(new HelpFragment(), getResources().getString(R.string.help_tab_title));
        adapter.addFragment(new ReportFragment(), getResources().getString(R.string.report_tab_title));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String customData = intent.getStringExtra("CustomData");

        if(customData != null && !customData.isEmpty())
            Toast.makeText(MainActivity.this, customData, Toast.LENGTH_LONG).show();
    }
}
