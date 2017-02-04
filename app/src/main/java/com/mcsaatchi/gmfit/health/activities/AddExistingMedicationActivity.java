package com.mcsaatchi.gmfit.health.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.mcsaatchi.gmfit.R;
import com.mcsaatchi.gmfit.architecture.otto.EventBusSingleton;
import com.mcsaatchi.gmfit.architecture.otto.MedicationItemCreatedEvent;
import com.mcsaatchi.gmfit.common.Constants;
import com.mcsaatchi.gmfit.common.activities.BaseActivity;
import com.mcsaatchi.gmfit.common.classes.AlarmReceiver;
import com.mcsaatchi.gmfit.common.classes.SimpleDividerItemDecoration;
import com.mcsaatchi.gmfit.health.adapters.MedicationRemindersRecyclerAdapter;
import com.mcsaatchi.gmfit.health.models.DayChoice;
import com.mcsaatchi.gmfit.health.models.Medication;
import com.mcsaatchi.gmfit.health.models.MedicationReminder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import timber.log.Timber;

public class AddExistingMedicationActivity extends BaseActivity {

  public static final int REMINDER_DAYS_CHOSEN = 231;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.medicineNameET) EditText medicineNameET;
  @Bind(R.id.unitMeasurementTV) TextView unitMeasurementTV;
  @Bind(R.id.daysOfWeekLayout) LinearLayout daysOfWeekLayout;
  @Bind(R.id.daysOfWeekTV) TextView daysOfWeekTV;
  @Bind(R.id.frequencyET) EditText frequencyET;
  @Bind(R.id.treatmentDurationET) EditText treatmentDurationET;
  @Bind(R.id.remindersRecyclerView) RecyclerView remindersRecyclerView;
  @Bind(R.id.yourNotesET) EditText yourNotesET;
  @Bind(R.id.unitsET) EditText unitsET;
  @Bind(R.id.addMedicationBTN) Button addMedicationBTN;
  @Bind(R.id.enableRemindersSwitch) Switch enableRemindersSwitch;
  @Bind(R.id.timesPerDayMeasurementTV) TextView timesPerDayMeasurementTV;

  private RuntimeExceptionDao<Medication, Integer> medicationDAO;
  private ArrayList<MedicationReminder> medicationReminders;
  private Medication medicationItem;
  private ArrayList<DayChoice> daysSelected = null;
  private boolean editPurpose = false;
  private int[] daysOfWeekArray;

  private Map<String, Integer> daysOfWeekMap = new HashMap<String, Integer>() {{
    put("Monday", 1);
    put("Tuesday", 2);
    put("Wednesday", 3);
    put("Thursday", 4);
    put("Friday", 5);
    put("Saturday", 6);
    put("Sunday", 7);
  }};

  private Map<String, Integer> calendarDays = new HashMap<String, Integer>() {{
    put("Monday", 2);
    put("Tuesday", 3);
    put("Wednesday", 4);
    put("Thursday", 5);
    put("Friday", 6);
    put("Saturday", 7);
    put("Sunday", 1);
  }};

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_add_existing_medication);

    ButterKnife.bind(this);

    setupToolbar(toolbar, getString(R.string.add_medication), true);

    medicationDAO = dbHelper.getMedicationDAO();

    if (getIntent().getExtras() != null) {
      medicationItem =
          (Medication) getIntent().getExtras().get(Constants.EXTRAS_MEDICATION_REMINDER_ITEM);

      editPurpose =
          getIntent().getExtras().getBoolean(Constants.EXTRAS_PURPOSE_EDIT_MEDICATION_REMINDER);

      if (editPurpose) {
        addMedicationBTN.setText(getString(R.string.edit_medication_button));
      }

      if (medicationItem.getWhen() != null) {
        daysSelected = medicationItem.getWhen();

        daysOfWeekArray = new int[daysSelected.size()];

        for (int i = 0; i < daysSelected.size(); i++) {
          if (daysSelected.get(i).isDaySelected()) {
            daysOfWeekArray[i] = daysOfWeekMap.get(daysSelected.get(i).getDayName());
          }
        }
      }

      medicineNameET.setText(medicationItem.getName());
      medicineNameET.setSelection(medicationItem.getName().length());

      if (medicationItem.isRemindersEnabled()) {
        enableRemindersSwitch.setChecked(true);
        medicationDAO.refresh(medicationItem);
        medicationReminders = new ArrayList<>(medicationItem.getMedicationReminders());
        remindersRecyclerView.setVisibility(View.VISIBLE);
        setupRemindersRecyclerView(medicationReminders);
      }

      addMedicationBTN.setText(getString(R.string.edit_medication_button));
      unitsET.setText(String.valueOf(medicationItem.getUnits()));
      treatmentDurationET.setText(String.valueOf(medicationItem.getTreatmentDuration()));
      unitMeasurementTV.setText(medicationItem.getUnitForm());
      frequencyET.setText(String.valueOf(medicationItem.getFrequency()));
      daysOfWeekTV.setText(medicationItem.getWhenString());
      treatmentDurationET.setText(String.valueOf(medicationItem.getTreatmentDuration()));
      yourNotesET.setText(medicationItem.getRemarks());
    }

    enableRemindersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        int frequencyNumber = Integer.parseInt(frequencyET.getText().toString());

        if (checked) {
          if (medicationItem != null
              && medicationItem.getMedicationReminders() != null
              && frequencyNumber == 0) {
            medicationDAO.refresh(medicationItem);
            setupRemindersRecyclerView(new ArrayList<>(medicationItem.getMedicationReminders()));
          } else {
            prepareRemindersRecyclerView(Integer.parseInt(frequencyET.getText().toString()));
          }

          remindersRecyclerView.setVisibility(View.VISIBLE);
        } else {
          remindersRecyclerView.setVisibility(View.GONE);
        }
      }
    });

    frequencyET.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override public void afterTextChanged(Editable editable) {
        if (!editable.toString().isEmpty() && enableRemindersSwitch.isChecked()) {
          prepareRemindersRecyclerView(Integer.parseInt(editable.toString()));
        }
      }
    });
  }

  @OnClick(R.id.timesPerDayMeasurementTV) void showFrequencyTypeDialog() {
    AlertDialog.Builder builderSingle = new AlertDialog.Builder(AddExistingMedicationActivity.this);
    builderSingle.setTitle("Type of frequency\n");

    final ArrayAdapter<String> arrayAdapter =
        new ArrayAdapter<>(AddExistingMedicationActivity.this, android.R.layout.simple_list_item_1);
    arrayAdapter.add(getString(R.string.frequency_type_weekly));
    arrayAdapter.add(getString(R.string.frequency_type_monthly));
    arrayAdapter.add(getString(R.string.frequency_type_when_needed));

    builderSingle.setNegativeButton(R.string.decline_cancel, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        String strName = arrayAdapter.getItem(which);
        timesPerDayMeasurementTV.setText(strName);
      }
    });

    builderSingle.show();
  }

  private void prepareRemindersRecyclerView(int frequencyNumber) {
    medicationReminders = new ArrayList<>(frequencyNumber);

    for (int ind = 0; ind < frequencyNumber; ind++) {
      medicationReminders.add(new MedicationReminder(Calendar.getInstance()));
    }

    setupRemindersRecyclerView(medicationReminders);
  }

  private void setupRemindersRecyclerView(ArrayList<MedicationReminder> medicationReminderTimes) {
    MedicationRemindersRecyclerAdapter medicationRemindersRecyclerAdapter =
        new MedicationRemindersRecyclerAdapter(AddExistingMedicationActivity.this,
            medicationReminderTimes);
    remindersRecyclerView.setLayoutManager(
        new LinearLayoutManager(AddExistingMedicationActivity.this));
    remindersRecyclerView.addItemDecoration(
        new SimpleDividerItemDecoration(AddExistingMedicationActivity.this));
    remindersRecyclerView.setAdapter(medicationRemindersRecyclerAdapter);
  }

  @OnClick(R.id.daysOfWeekLayout) void openDaysChooser() {
    Intent intent = new Intent(AddExistingMedicationActivity.this, DaysChoiceListActivity.class);
    if (daysSelected != null) intent.putParcelableArrayListExtra("REMINDER_DAYS", daysSelected);
    startActivityForResult(intent, REMINDER_DAYS_CHOSEN);
  }

  @OnClick(R.id.addMedicationBTN) void addMedication() {
    if (medicineNameET.getText().toString().isEmpty()
        || frequencyET.getText().toString().isEmpty()
        || daysOfWeekTV.getText().toString().isEmpty()
        || treatmentDurationET.getText().toString().isEmpty()
        || unitMeasurementTV.getText().toString().isEmpty()) {

      Toast.makeText(this, R.string.fill_in_below_fields_hint, Toast.LENGTH_LONG).show();
    } else {
      if (editPurpose) {
        medicationItem.setName(medicineNameET.getText().toString());
        medicationItem.setRemarks(yourNotesET.getText().toString());
        medicationItem.setUnits(Integer.parseInt(unitsET.getText().toString()));
        medicationItem.setFrequency(Integer.parseInt(frequencyET.getText().toString()));
        medicationItem.setUnitForm(unitMeasurementTV.getText().toString());
        medicationItem.setWhen(daysSelected);
        medicationItem.setWhenString(daysOfWeekTV.getText().toString());
        medicationItem.setDosage("0.5 " + medicationItem.getUnitForm());
        medicationItem.setRemindersEnabled(enableRemindersSwitch.isChecked());
        medicationItem.setTreatmentDuration(
            Integer.parseInt(treatmentDurationET.getText().toString()));
        medicationItem.setDescription(medicationItem.getUnits()
            + " "
            + medicationItem.getUnitForm()
            + " "
            + medicationItem.getUnitForm().toUpperCase()
            + " "
            + medicationItem.getUnits()
            + " "
            + medicationItem.getUnitForm());
        setFrequencyType(medicationItem);

        if (enableRemindersSwitch.isChecked() && medicationReminders != null) {
          medicationItem.setRemindersEnabled(true);
          assignForeignCollectionToParentObject(medicationItem);
        } else {
          medicationItem.setRemindersEnabled(false);
        }

        medicationDAO.update(medicationItem);

        setupMedicationReminders(medicationItem.getMedicationReminders().iterator());
      } else {
        Medication medication = new Medication();
        medication.setName(medicineNameET.getText().toString());
        medication.setRemarks(yourNotesET.getText().toString());
        medication.setUnits(Integer.parseInt(unitsET.getText().toString()));
        medication.setFrequency(Integer.parseInt(frequencyET.getText().toString()));
        medication.setUnitForm(unitMeasurementTV.getText().toString());
        medication.setWhen(daysSelected);
        medication.setWhenString(daysOfWeekTV.getText().toString());
        medication.setDosage("0.5 " + medication.getUnitForm());
        medication.setTreatmentDuration(Integer.parseInt(treatmentDurationET.getText().toString()));
        medication.setRemindersEnabled(enableRemindersSwitch.isChecked());
        medication.setDescription(
            medication.getUnits() + " " + medication.getUnitForm() + " " + medication.getUnitForm()
                .toUpperCase() + " " + medication.getUnits() + " " + medication.getUnitForm());
        setFrequencyType(medication);

        medicationDAO.create(medication);

        if (enableRemindersSwitch.isChecked() && medicationReminders != null) {
          medication.setRemindersEnabled(true);
          assignForeignCollectionToParentObject(medication);
        } else {
          medication.setRemindersEnabled(false);
        }

        medicationDAO.update(medication);

        setupMedicationReminders(medication.getMedicationReminders().iterator());
      }

      EventBusSingleton.getInstance().post(new MedicationItemCreatedEvent());

      finish();
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REMINDER_DAYS_CHOSEN) {
      ArrayList<DayChoice> dayChoices = data.getExtras().getParcelableArrayList("REMINDER_DAYS");
      daysSelected = dayChoices;

      String daysOfWeekResult = "";

      if (dayChoices != null) {
        daysOfWeekArray = new int[dayChoices.size()];

        for (int i = 0; i < dayChoices.size(); i++) {
          if (dayChoices.get(i).isDaySelected()) {
            daysOfWeekArray[i] = daysOfWeekMap.get(dayChoices.get(i).getDayName());
            daysOfWeekResult += dayChoices.get(i).getDayName().substring(0, 3) + ", ";
          }
        }
      }

      daysOfWeekTV.setText(daysOfWeekResult.replaceAll(", $", ""));
    }
  }

  private void assignForeignCollectionToParentObject(Medication medicationObject) {
    medicationDAO.assignEmptyForeignCollection(medicationObject, "medicationReminders");

    MedicationRemindersRecyclerAdapter adapter =
        (MedicationRemindersRecyclerAdapter) remindersRecyclerView.getAdapter();

    //Clear previous reminders
    medicationObject.getMedicationReminders().clear();

    for (int i = 0; i < adapter.getItemCount(); i++) {
      MedicationReminder medReminder = adapter.getItem(i);
      medReminder.setMedication(medicationObject);
      medReminder.setDays_of_week(daysOfWeekArray);
      medReminder.setEnabled(true);
      medReminder.setHour(adapter.getItem(i).getHour());
      medReminder.setMinute(adapter.getItem(i).getMinute());

      medicationObject.getMedicationReminders().add(medReminder);
    }
  }

  public void setupMedicationReminders(Iterator<MedicationReminder> medicationRemindersIterator) {

    PendingIntent pendingIntent;
    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

    while (medicationRemindersIterator.hasNext()) {
      MedicationReminder medReminder = medicationRemindersIterator.next();

      for (int j = 0; j < Integer.parseInt(treatmentDurationET.getText().toString()); j++) {

        for (int i = 0; i < medReminder.getDays_of_week().length; i++) {
          int dayChosen = medReminder.getDays_of_week()[i];

          if (dayChosen != 0) {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, dayChosen + 1);
            cal.set(Calendar.HOUR_OF_DAY, medReminder.getHour());
            cal.set(Calendar.MINUTE, medReminder.getMinute());
            cal.set(Calendar.SECOND, 0);

            /**
             * Add 1 week to the calendar if its time is in the past
             */
            if (cal.before(Calendar.getInstance())) {
              cal.add(Calendar.DATE, 7 * (j + 1));
            } else if (j >= 1) {
              cal.add(Calendar.DATE, 7 * j);
            }

            Intent intent = new Intent(AddExistingMedicationActivity.this, AlarmReceiver.class);
            intent.putExtra(Constants.EXTRAS_ALARM_TYPE, "medications");
            intent.putExtra(Constants.EXTRAS_MEDICATION_REMINDER_ITEM, (Parcelable) medReminder);

            pendingIntent = PendingIntent.getBroadcast(this, medReminder.getId(), intent,
                PendingIntent.FLAG_ONE_SHOT);

            int ALARM_TYPE = AlarmManager.RTC_WAKEUP;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              am.setRepeating(ALARM_TYPE, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                  pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
              am.setExact(ALARM_TYPE, cal.getTimeInMillis(), pendingIntent);
            } else {
              am.set(ALARM_TYPE, cal.getTimeInMillis(), pendingIntent);
            }

            Timber.d("Final date is : " + new Date(cal.getTimeInMillis()));
          }
        }
      }
    }
  }

  private void setFrequencyType(Medication medication) {
    switch (timesPerDayMeasurementTV.getText().toString()) {
      case "times per week":
        medication.setFrequencyType(1);
        break;
      case "times per month":
        medication.setFrequencyType(2);
        break;
      case "when needed":
        medication.setFrequencyType(3);
        break;
    }
  }
}
