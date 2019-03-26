package com.example.mernashenda.uangsaya;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import SlidingTabs.SlidingTabLayout;
import SlidingTabs.SlidingTabAdapter;
import Fragment.OneFragment;
import tabs.TabSurat;
import tabs.ViewPagerAdapter;

public class HomeActivity extends ActionBarActivity {
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    SlidingTabLayout mSlidingTabLayout;
    ViewPager mViewPager;
    private String namaku;
    private View navHeader;
    private NavigationView navigationView;
    private TextView txtName;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.initLayout();
        this.bindNavDrawerEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(AppVar.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(AppVar.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();

                        //Starting login activity
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        //try {
        //    int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        //    TextView tv = (TextView) alertDialog.findViewById(textViewId);
        //    tv.setTextColor(getResources().getColor(R.color.red));
        //}
        //catch (Exception e) {
        //    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        //}


        alertDialog.show();

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        //nbutton.setBackgroundColor(Color.YELLOW);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //pbutton.setBackgroundColor(Color.YELLOW);
        pbutton.setTextColor(Color.BLACK);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //--------------------------------------------------------------------------
        // triggers if the user selects a menu item
        //--------------------------------------------------------------------------
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        //--------------------------------------------------------------------------
        // make sure the drawer toggle is in the right state, nothing to do here
        //--------------------------------------------------------------------------
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //--------------------------------------------------------------------------
        // close the nav drawer if user pressed the back button
        // nothing to do here
        //--------------------------------------------------------------------------
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabSurat(), "Pemasukan");
        adapter.addFragment(new TabSurat(), "Pengeluaran");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void initLayout() {
        //--------------------------------------------------------------------------
        // create the material toolbar
        //--------------------------------------------------------------------------
        //Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        //setSupportActionBar(mToolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //--------------------------------------------------------------------------
        // create the material navdrawer
        //--------------------------------------------------------------------------
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.colorMainDark));

        //--------------------------------------------------------------------------
        // create the material navdrawer toggle and bind it to the navigation_drawer
        //--------------------------------------------------------------------------
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //navigationView = (NavigationView) findViewById(R.id.navdrawer);
        //navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) findViewById(R.id.imgDrawerHeader);
        SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        namaku = sharedPreferences.getString(AppVar.USERNAME_SHARED_PREF, "");
        txtName.setText(namaku);

        //--------------------------------------------------------------------------
        // create the viewpager which holds the tab contents
        // tell the viewpager which tabs he have to listen to
        //--------------------------------------------------------------------------
        /*mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(5); // tabcachesize (=tabcount for better performance)
        mViewPager.setAdapter(new SlidingTabAdapter());*/

        //--------------------------------------------------------------------------
        // create sliding tabs and bind them to the viewpager
        //--------------------------------------------------------------------------
        //mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        // use own style rules for tab layout
        /*mSlidingTabLayout.setCustomTabView(R.layout.toolbar_tab, R.id.toolbar_tab_txtCaption);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.tab_indicator_color));

        mSlidingTabLayout.setDistributeEvenly(true); // each tab has the same size
        mSlidingTabLayout.setViewPager(mViewPager);*/

    }

    public void bindTabEvents() {
        // Tab events
        if (mSlidingTabLayout != null) {
            mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                    // TODO add code tabbar is scrolled
                }

                @Override
                public void onPageSelected(int position) {
                    // TODO add code tab page select
                    Toast.makeText(HomeActivity.this, "Error:" + position, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // TODO add code tab scrollstate changed
                }
            });
        }
    }

    public void bindNavDrawerEvents() {
        // Click event for one Navigation element
        LinearLayout navButton = (LinearLayout) findViewById(R.id.txtNavButton);
        navButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                mDrawerLayout.closeDrawers();
                //Toast.makeText(v.getContext(), "navitem Home clicked", Toast.LENGTH_SHORT).show();

                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });

        LinearLayout navButton1 = (LinearLayout) findViewById(R.id.txtNavButton1);
        navButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(HomeActivity.this,KasActivity.class));
                //Intent intent = new Intent(HomeActivity.this, ArsipSuratActivity.class);
                //startActivity(intent);
                //Toast.makeText(v.getContext(), "navitem Home clicked", Toast.LENGTH_SHORT).show();

                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });

        LinearLayout navButton2 = (LinearLayout) findViewById(R.id.txtNavButton2);
        navButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(HomeActivity.this,KategoriActivity.class));
                //Intent intent = new Intent(HomeActivity.this, ArsipSuratActivity.class);
                //startActivity(intent);
                //Toast.makeText(v.getContext(), "navitem Home clicked", Toast.LENGTH_SHORT).show();

                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });

        LinearLayout navButton3 = (LinearLayout) findViewById(R.id.txtNavButton3);
        navButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating a shared preference
                //SharedPreferences sharedPreferences = HomeActivity.this.getSharedPreferences(AppVar.SHARED_PREF_KONDISI_TRANSAKSI, Context.MODE_PRIVATE);

                //Creating editor to store values to shared preferences
                //SharedPreferences.Editor editor = sharedPreferences.edit();

                //Adding values to editor
                //editor.putString(AppVar.SHARED_TRANSAKSI, "1");
                //editor.commit();
                Intent intent = new Intent(HomeActivity.this, PengeluaranActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("kondisi","1");
                intent.putExtras(bundle);

                mDrawerLayout.closeDrawers();
                startActivity(intent);
                //startActivity(new Intent(HomeActivity.this,PengeluaranActivity.class));
            }
        });

        LinearLayout navButton4 = (LinearLayout) findViewById(R.id.txtNavButton4);
        navButton4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating a shared preference
                Intent intent = new Intent(HomeActivity.this, PengeluaranActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("kondisi","0");
                intent.putExtras(bundle);

                mDrawerLayout.closeDrawers();
                startActivity(intent);

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                //mDrawerLayout.closeDrawers();
                //Intent intent = new Intent(HomeActivity.this, ArsipDisposisiActivity.class);
                //startActivity(intent);
                //Toast.makeText(v.getContext(), "navitem Home clicked", Toast.LENGTH_SHORT).show();

                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });

        LinearLayout navButton9 = (LinearLayout) findViewById(R.id.txtNavButton9);
        navButton9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(HomeActivity.this,DeveloperActivity.class));
                //Intent intent = new Intent(HomeActivity.this, ArsipDisposisiActivity.class);
                //startActivity(intent);
                //Toast.makeText(v.getContext(), "navitem Home clicked", Toast.LENGTH_SHORT).show();

                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void addContent(){

        // Inflate your Layouts here
        addTab(R.layout.tabcontent_1,"Pemasukan");
        addTab(R.layout.tabcontent_2, "Pengeluaran");
        //addTab(R.layout.tabcontent_3, "Newsfeed");
        //addTab(R.layout.tabcontent_1, "Second Home");
    }

    public void addTab(int layout,String tabTitle)
    {
        this.addTab(layout,tabTitle,-1);
    }
    public void addTab(int layout,String tabTitle,int position)
    {
        SlidingTabAdapter mTabs = (SlidingTabAdapter)mViewPager.getAdapter();
        mTabs.addView(getLayoutInflater().inflate(layout,null),tabTitle,position);
        mTabs.notifyDataSetChanged();
        mSlidingTabLayout.populateTabStrip();
    }

    public void removeTab()
    {
        this.removeTab(-1);
    }
    public void removeTab(int position)
    {
        SlidingTabAdapter mTabs = (SlidingTabAdapter)mViewPager.getAdapter();
        mTabs.removeView(position);
        mTabs.notifyDataSetChanged();
        mSlidingTabLayout.populateTabStrip();
    }

}
