package com.mcsaatchi.gmfit.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.mcsaatchi.gmfit.R;
import com.mcsaatchi.gmfit.classes.Constants;
import com.mcsaatchi.gmfit.classes.SlidingTabLayout;
import com.mcsaatchi.gmfit.fragments.CustomizeCharts_Fragment;
import com.mcsaatchi.gmfit.fragments.CustomizeWidgets_Fragment;
import com.mcsaatchi.gmfit.models.DataChart;
import com.mcsaatchi.gmfit.models.FitnessWidget;
import com.mcsaatchi.gmfit.models.NutritionWidget;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CustomizeWidgetsAndCharts_Activity extends Base_Activity {
  @Bind(R.id.pager) ViewPager pager;
  @Bind(R.id.tabs) SlidingTabLayout tabs;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.mainLayoutTop) LinearLayout mainLayoutTop;

  private CustomizerViewPager_Adapter fragmentsPagerAdapter;

  private String typeOfFragmentToCustomiseFor;

  private ArrayList<FitnessWidget> fitnessWidgetsMapExtra;
  private ArrayList<NutritionWidget> nutritionWidgetsMapExtra;
  private ArrayList<DataChart> dataChartsMapExtra;

  private String[] tabTitles = new String[] {
      "Widgets", "Charts"
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_customize_widgets_and_charts);

    ButterKnife.bind(this);

    setupToolbar(toolbar, getResources().getString(R.string.customize_widgets_and_charts_activity_title), true);

    Bundle intentExtras = getIntent().getExtras();

    //Grab the Fragment type from one of the three Fragments (Fitness, Nutrition, Health)
    if (intentExtras != null) {
      typeOfFragmentToCustomiseFor = intentExtras.getString(Constants.EXTRAS_FRAGMENT_TYPE);

      if (typeOfFragmentToCustomiseFor != null) {
        switch (typeOfFragmentToCustomiseFor) {
          case Constants.EXTRAS_FITNESS_FRAGMENT:
            fitnessWidgetsMapExtra =
                intentExtras.getParcelableArrayList(Constants.BUNDLE_FITNESS_WIDGETS_MAP);
            dataChartsMapExtra =
                intentExtras.getParcelableArrayList(Constants.BUNDLE_FITNESS_CHARTS_MAP);
            mainLayoutTop.setBackground(getResources().getDrawable(R.drawable.fitness_background));
            break;
          case Constants.EXTRAS_NUTRITION_FRAGMENT:
            dataChartsMapExtra =
                intentExtras.getParcelableArrayList(Constants.BUNDLE_NUTRITION_CHARTS_MAP);
            nutritionWidgetsMapExtra =
                intentExtras.getParcelableArrayList(Constants.BUNDLE_NUTRITION_WIDGETS_MAP);
            mainLayoutTop.setBackground(
                getResources().getDrawable(R.drawable.nutrition_background));
            break;
        }
      }
    }

    fragmentsPagerAdapter = new CustomizerViewPager_Adapter(getSupportFragmentManager(), tabTitles);

    tabs.setDistributeEvenly(true);
    tabs.setSelectedIndicatorColors(getResources().getColor(android.R.color.white));

    pager.setAdapter(fragmentsPagerAdapter);
    pager.setCurrentItem(0);

    tabs.setViewPager(pager);
  }

  public class CustomizerViewPager_Adapter extends FragmentStatePagerAdapter {

    private String[] tabTitles;

    public CustomizerViewPager_Adapter(FragmentManager fm, String[] tabTitles) {
      super(fm);

      this.tabTitles = tabTitles;
    }

    @Override public Fragment getItem(int position) {
      Bundle fragmentArguments = new Bundle();

      fragmentArguments.putString(Constants.EXTRAS_FRAGMENT_TYPE, typeOfFragmentToCustomiseFor);

      if (typeOfFragmentToCustomiseFor != null) {
        switch (typeOfFragmentToCustomiseFor) {
          case Constants.EXTRAS_FITNESS_FRAGMENT:
            fragmentArguments.putParcelableArrayList(Constants.BUNDLE_FITNESS_WIDGETS_MAP,
                fitnessWidgetsMapExtra);
            fragmentArguments.putParcelableArrayList(Constants.BUNDLE_FITNESS_CHARTS_MAP,
                dataChartsMapExtra);
            break;
          case Constants.EXTRAS_NUTRITION_FRAGMENT:
            fragmentArguments.putParcelableArrayList(Constants.BUNDLE_NUTRITION_WIDGETS_MAP,
                nutritionWidgetsMapExtra);
            fragmentArguments.putParcelableArrayList(Constants.BUNDLE_NUTRITION_CHARTS_MAP,
                dataChartsMapExtra);
            break;
        }
      }

      switch (position) {
        case 0:
          Fragment customizeWidgetFragment = new CustomizeWidgets_Fragment();
          customizeWidgetFragment.setArguments(fragmentArguments);
          return customizeWidgetFragment;
        case 1:
          Fragment customizeChartsFragment = new CustomizeCharts_Fragment();
          customizeChartsFragment.setArguments(fragmentArguments);
          return customizeChartsFragment;
      }

      return null;
    }

    @Override public CharSequence getPageTitle(int position) {
      return tabTitles[position];
    }

    @Override public int getCount() {
      return tabTitles.length;
    }
  }
}
