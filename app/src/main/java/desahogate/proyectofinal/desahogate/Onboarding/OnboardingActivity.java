package desahogate.proyectofinal.desahogate.Onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import desahogate.proyectofinal.desahogate.R;
import desahogate.proyectofinal.desahogate.LoginActivity;

import com.ogaclejapan.smarttablayout.SmartTabLayout;


public class OnboardingActivity extends FragmentActivity {

    private ViewPager pager;
    private SmartTabLayout indicator;
    private Button skip;
    private Button next;
    FragmentStatePagerAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);


        inicializar();


        adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 : return new OnboardingFragment1();
                    case 1 : return new OnboardingFragment2();
                    case 2 : return new OnboardingFragment3();
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

        };

        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnboarding();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pager.getCurrentItem() == 2) { // The last screen
                    finishOnboarding();
                } else {
                    pager.setCurrentItem(
                            pager.getCurrentItem() + 1,
                            true
                    );
                }
            }
        });

        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    skip.setVisibility(View.GONE);
                    next.setText("Terminar");
                } else {
                    skip.setVisibility(View.VISIBLE);
                    next.setText("Siguiente");
                }
            }
        });


    }

    private void inicializar() {
        pager = (ViewPager)findViewById(R.id.pager);
        indicator = (SmartTabLayout)findViewById(R.id.indicator);
        skip = (Button)findViewById(R.id.skip);
        next = (Button)findViewById(R.id.next);


    }


    private void finishOnboarding() {

        // Get the shared preferences
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);

        // Set onboarding_complete to true
        preferences.edit().putBoolean("onboarding_complete",true).apply();

        // Launch the main Activity, called MainActivity
        Intent main = new Intent(this, LoginActivity.class);
        startActivity(main);

        // Close the OnboardingActivity
        finish();
    }




}
