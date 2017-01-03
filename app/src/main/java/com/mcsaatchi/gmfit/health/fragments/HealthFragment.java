package com.mcsaatchi.gmfit.health.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.mcsaatchi.gmfit.R;
import com.mcsaatchi.gmfit.architecture.GMFitApplication;
import com.mcsaatchi.gmfit.architecture.data_access.DataAccessHandler;
import com.mcsaatchi.gmfit.architecture.otto.EventBusSingleton;
import com.mcsaatchi.gmfit.architecture.otto.HealthWidgetsOrderChangedEvent;
import com.mcsaatchi.gmfit.architecture.otto.MedicalTestEditCreateEvent;
import com.mcsaatchi.gmfit.architecture.rest.TakenMedicalTestsResponse;
import com.mcsaatchi.gmfit.architecture.rest.TakenMedicalTestsResponseBody;
import com.mcsaatchi.gmfit.architecture.rest.UserProfileResponse;
import com.mcsaatchi.gmfit.architecture.rest.UserProfileResponseDatum;
import com.mcsaatchi.gmfit.architecture.rest.WidgetsResponse;
import com.mcsaatchi.gmfit.architecture.rest.WidgetsResponseDatum;
import com.mcsaatchi.gmfit.architecture.touch_helpers.SimpleSwipeItemTouchHelperCallback;
import com.mcsaatchi.gmfit.common.Constants;
import com.mcsaatchi.gmfit.common.activities.BaseActivity;
import com.mcsaatchi.gmfit.common.activities.CustomizeWidgetsAndChartsActivity;
import com.mcsaatchi.gmfit.common.classes.SimpleDividerItemDecoration;
import com.mcsaatchi.gmfit.health.activities.AddMedicationActivity;
import com.mcsaatchi.gmfit.health.activities.AddNewHealthTestActivity;
import com.mcsaatchi.gmfit.health.adapters.HealthWidgetsRecyclerAdapter;
import com.mcsaatchi.gmfit.health.adapters.MedicationsRecyclerAdapter;
import com.mcsaatchi.gmfit.health.adapters.UserTestsRecyclerAdapter;
import com.mcsaatchi.gmfit.health.models.HealthWidget;
import com.mcsaatchi.gmfit.health.models.Medication;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class HealthFragment extends Fragment {

  @Inject DataAccessHandler dataAccessHandler;
  @Inject SharedPreferences prefs;

  @Bind(R.id.metricCounterTV) TextView metricCounterTV;
  @Bind(R.id.widgetsGridView) RecyclerView widgetsGridView;
  @Bind(R.id.addEntryBTN_MEDICAL_TESTS) TextView addEntryBTN_MEDICAL_TESTS;
  @Bind(R.id.addEntryBTN_MEDICATIONS) TextView addEntryBTN_MEDICATIONS;
  @Bind(R.id.userTestsListView) RecyclerView userTestsListView;
  @Bind(R.id.loadingWidgetsProgressBar) ProgressBar loadingWidgetsProgressBar;
  @Bind(R.id.loadingTestsProgressBar) ProgressBar loadingTestsProgressBar;
  @Bind(R.id.medicationsRecyclerView) RecyclerView medicationsRecyclerView;

  private RuntimeExceptionDao<Medication, Integer> medicationDAO;

  private ArrayList<HealthWidget> healthWidgetsMap = new ArrayList<>();

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View fragmentView = inflater.inflate(R.layout.fragment_health, container, false);

    ButterKnife.bind(this, fragmentView);

    EventBusSingleton.getInstance().register(this);

    setHasOptionsMenu(true);

    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.health_tab_title);
    ((GMFitApplication) getActivity().getApplication()).getAppComponent().inject(this);

    medicationDAO = ((BaseActivity) getActivity()).dbHelper.getMedicationDAO();

    List<Medication> medicationsList = medicationDAO.queryForAll();

    setupMedicationsList(medicationsList);

    getWidgets();

    getTakenMedicalTests();

    getUserProfile();

    addEntryBTN_MEDICATIONS.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(getActivity(), AddMedicationActivity.class);
        startActivity(intent);
      }
    });

    addEntryBTN_MEDICAL_TESTS.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(getActivity(), AddNewHealthTestActivity.class);
        startActivity(intent);
      }
    });

    return fragmentView;
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.main, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.settings:
        Intent intent = new Intent(getActivity(), CustomizeWidgetsAndChartsActivity.class);
        intent.putExtra(Constants.EXTRAS_FRAGMENT_TYPE, Constants.EXTRAS_HEALTH_FRAGMENT);
        intent.putExtra(Constants.BUNDLE_HEALTH_WIDGETS_MAP, healthWidgetsMap);
        startActivity(intent);
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  private void setupMedicationsList(List<Medication> medicationList) {
    medicationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    MedicationsRecyclerAdapter medicationsRecyclerAdapter =
        new MedicationsRecyclerAdapter(getActivity(), medicationList, medicationDAO);
    medicationsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
    medicationsRecyclerView.setAdapter(medicationsRecyclerAdapter);
  }

  private void getUserProfile() {
    dataAccessHandler.getUserProfile(new Callback<UserProfileResponse>() {
      @Override public void onResponse(Call<UserProfileResponse> call,
          Response<UserProfileResponse> response) {
        switch (response.code()) {
          case 200:
            UserProfileResponseDatum userProfileData =
                response.body().getData().getBody().getData();

            SharedPreferences.Editor prefsEditor = prefs.edit();

            if (userProfileData != null) {

              /**
               * Set the weight
               */
              if (userProfileData.getWeight() != null && !userProfileData.getWeight().isEmpty()) {
                prefsEditor.putFloat(Constants.EXTRAS_USER_PROFILE_WEIGHT,
                    Float.parseFloat(userProfileData.getWeight()));
                metricCounterTV.setText(String.valueOf(String.format(Locale.getDefault(), "%.1f",
                    Float.parseFloat(userProfileData.getWeight()))));
              }

              prefsEditor.apply();

              break;
            }
        }
      }

      @Override public void onFailure(Call<UserProfileResponse> call, Throwable t) {
        Timber.d("Call failed with error : %s", t.getMessage());
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(getString(R.string.error_response_from_server_incorrect));
        alertDialog.show();
      }
    });
  }

  private void getWidgets() {
    dataAccessHandler.getWidgets("medical", new Callback<WidgetsResponse>() {
      @Override
      public void onResponse(Call<WidgetsResponse> call, Response<WidgetsResponse> response) {
        switch (response.code()) {
          case 200:
            List<WidgetsResponseDatum> widgetsFromResponse =
                response.body().getData().getBody().getData();

            for (int i = 0; i < widgetsFromResponse.size(); i++) {
              HealthWidget widget = new HealthWidget();

              widget.setId(widgetsFromResponse.get(i).getWidgetId());
              widget.setMeasurementUnit(widgetsFromResponse.get(i).getUnit());
              widget.setPosition(i);
              widget.setValue(Float.parseFloat(widgetsFromResponse.get(i).getTotal()));
              widget.setTitle(widgetsFromResponse.get(i).getName());
              widget.setSlug(widgetsFromResponse.get(i).getSlug());

              healthWidgetsMap.add(widget);
            }

            setupWidgetViews(healthWidgetsMap);

            break;
        }
      }

      @Override public void onFailure(Call<WidgetsResponse> call, Throwable t) {
        Timber.d("Call failed with error : %s", t.getMessage());
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(getString(R.string.error_response_from_server_incorrect));
        alertDialog.show();
      }
    });
  }

  private void getTakenMedicalTests() {
    dataAccessHandler.getTakenMedicalTests(new Callback<TakenMedicalTestsResponse>() {
      @Override public void onResponse(Call<TakenMedicalTestsResponse> call,
          Response<TakenMedicalTestsResponse> response) {
        switch (response.code()) {
          case 200:
            List<TakenMedicalTestsResponseBody> takenMedicalTests =
                response.body().getData().getBody();

            loadingTestsProgressBar.setVisibility(View.GONE);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            UserTestsRecyclerAdapter userTestsRecyclerAdapter =
                new UserTestsRecyclerAdapter(getActivity().getApplication(), takenMedicalTests);
            ItemTouchHelper.Callback callback =
                new SimpleSwipeItemTouchHelperCallback(userTestsRecyclerAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);

            userTestsListView.setLayoutManager(mLayoutManager);
            userTestsListView.setNestedScrollingEnabled(false);
            userTestsListView.setAdapter(userTestsRecyclerAdapter);
            userTestsListView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            touchHelper.attachToRecyclerView(userTestsListView);

            break;
        }
      }

      @Override public void onFailure(Call<TakenMedicalTestsResponse> call, Throwable t) {
        Timber.d("Call failed with error : %s", t.getMessage());
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(getString(R.string.error_response_from_server_incorrect));
        alertDialog.show();
      }
    });
  }

  @Subscribe public void updateWidgetsOrder(HealthWidgetsOrderChangedEvent event) {
    healthWidgetsMap = event.getWidgetsMapHealth();
    setupWidgetViews(healthWidgetsMap);
  }

  @Subscribe public void reflectMedicalTestEditCreate(MedicalTestEditCreateEvent event) {
    getWidgets();
    getTakenMedicalTests();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    EventBusSingleton.getInstance().unregister(this);
  }

  private void setupWidgetViews(ArrayList<HealthWidget> healthWidgetsMap) {
    if (!healthWidgetsMap.isEmpty() && healthWidgetsMap.size() > 4) {
      healthWidgetsMap = new ArrayList<>(healthWidgetsMap.subList(0, 4));

      HealthWidgetsRecyclerAdapter healthWidgetsGridAdapter =
          new HealthWidgetsRecyclerAdapter(getActivity(), healthWidgetsMap,
              R.layout.grid_item_health_widgets);
      widgetsGridView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
      widgetsGridView.setAdapter(healthWidgetsGridAdapter);

      loadingWidgetsProgressBar.setVisibility(View.GONE);
    }
  }
}