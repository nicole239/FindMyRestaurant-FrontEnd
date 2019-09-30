package tec.findmyrestaurant.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import tec.findmyrestaurant.R;


public class TabbedActivity extends AppCompatActivity {

    public static final String KEY_EXTRA = "RESTAURANT_LIST_RESULT";
    private static final String TAG = "MainActivity";

    private SectionsPageAdapter sectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mViewPager = findViewById(R.id.view_pager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton btnSearch = findViewById(R.id.fabSeach);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchBtn_onClick();
            }
        });

        FloatingActionButton btnAdd = findViewById(R.id.fabAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addBtn_onClick();
            }
        });

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new MapFragment(),"MAP");
        adapter.addFragment(new ListFragment(),"LIST");
        viewPager.setAdapter(adapter);
    }

    private void searchBtn_onClick(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
    private void addBtn_onClick(){
        Intent intent = new Intent(this, AddRestaurantActivity.class);
        startActivity(intent);
    }

    public void logoutBtn_onClick(View view){
        Intent intent =  new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}