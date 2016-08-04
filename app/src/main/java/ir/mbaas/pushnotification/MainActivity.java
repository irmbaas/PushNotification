package ir.mbaas.pushnotification;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ir.mbaas.pushnotification.adapter.ViewPagerAdapter;
import ir.mbaas.pushnotification.fragment.HelpFragment;
import ir.mbaas.pushnotification.fragment.IntroFragment;
import ir.mbaas.pushnotification.fragment.ReportFragment;
import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.models.User;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context ctx;

    private String FIRST_NAME    = "first_name";
    private String LAST_NAME     = "last_name";
    private String PHONE_NUMBER  = "phone_number";

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

        ctx = this;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_info:
                updateUserInfo();
                return true;
            case R.id.news_api:
                startActivity(new Intent(this, TestNewsAPIActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUserInfo() {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.dialog_prompts, null);
        final EditText firstName = (EditText) promptsView.findViewById(R.id.et_fname);
        final EditText lastName = (EditText) promptsView.findViewById(R.id.et_lname);
        final EditText phone = (EditText) promptsView.findViewById(R.id.et_phone);

        firstName.setText(PrefUtil.getString(ctx, FIRST_NAME));
        lastName.setText(PrefUtil.getString(ctx, LAST_NAME));
        phone.setText(PrefUtil.getString(ctx, PHONE_NUMBER));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String fnStr = firstName.getText().toString();
                                String lnStr = lastName.getText().toString();
                                String pnStr = phone.getText().toString();

                                PrefUtil.putString(ctx, FIRST_NAME, fnStr);
                                PrefUtil.putString(ctx, LAST_NAME, lnStr);
                                PrefUtil.putString(ctx, PHONE_NUMBER, pnStr);

                                User user = new User(fnStr, lnStr, pnStr);

                                MBaaS.updateInfo(user);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
