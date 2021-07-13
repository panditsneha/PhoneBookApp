package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class FirstActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    SearchView searchView;
   RecycleViewAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentFav(),"Favorite");
        adapter.AddFragment(new FragmentRecent(),"Recent");
        adapter.AddFragment(new FragmentContact(),"Contact");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.star);
        tabLayout.getTabAt(1).setIcon(R.drawable.clock);
        tabLayout.getTabAt(2).setIcon(R.drawable.man);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        MenuItem item = menu.findItem(R.id.search);
//        searchView = (SearchView) item.getActionView();
//        searchView.setQueryHint("Search here...");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                recyclerAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:break;
            case R.id.calllogs:break;
            case R.id.share:break;
        }
        return super.onOptionsItemSelected(item);
    }
}