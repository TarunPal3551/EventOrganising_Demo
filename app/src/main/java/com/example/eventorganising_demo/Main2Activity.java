package com.example.eventorganising_demo;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    TextInputLayout textInputLayoutName, textInputLayoutDate, textInputLayoutTime;
    Switch switchReminder;
    MaterialCardView cardBack;
    ImageView imageViewAdd, imageViewBackground;
    MaterialDatePicker<Long> picker;
    MaterialDatePicker.Builder<Long> builder;
    TimePickerDialog mTimePicker;
    String name, date, time, selectedPath;
    MaterialButton materialButtonSave;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutDate = (TextInputLayout) findViewById(R.id.textInputLayoutDate);
        textInputLayoutTime = (TextInputLayout) findViewById(R.id.textInputLayoutTime);
        materialButtonSave = (MaterialButton) findViewById(R.id.saveButton);
        switchReminder = (Switch) findViewById(R.id.remindersSwitch);
        cardBack = (MaterialCardView) findViewById(R.id.cardImage);
        imageViewAdd = (ImageView) findViewById(R.id.imageViewAdd);
        imageViewBackground = (ImageView) findViewById(R.id.imageViewBackground);
        imageViewBackground.setVisibility(View.GONE);
        builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Event Date");
        builder.setSelection(new Date().getTime());
        picker = builder.build();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        textInputLayoutDate.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getSupportFragmentManager(), picker.toString());

            }
        });

//        textInputLayoutDate.setOnHoverListener(new View.OnHoverListener() {
//            @Override
//            public boolean onHover(View v, MotionEvent event) {
//                picker.show(getSupportFragmentManager(), picker.toString());
//                return false;
//            }
//        });
        textInputLayoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getSupportFragmentManager(), picker.toString());
            }
        });
        textInputLayoutDate.setClickable(true);
        textInputLayoutTime.setClickable(true);
        textInputLayoutDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getSupportFragmentManager(), picker.toString());
            }
        });
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                try {
                    String _24HourTime = selectedHour + ":" + selectedMinute;
                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                    Date _24HourDt = _24HourSDF.parse(_24HourTime);
                    System.out.println(_24HourDt);
                    System.out.println(_12HourSDF.format(_24HourDt));
                    textInputLayoutTime.getEditText().setText("" + _12HourSDF.format(_24HourDt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        textInputLayoutTime.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker.show();
            }
        });
        textInputLayoutTime.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTimePicker.show();

            }
        });
        textInputLayoutDate.setFocusable(false);
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                if (selection != null) {
                    Date date = new Date(selection);
                    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                    String dateText = df2.format(date);
                    System.out.println(dateText);
                    textInputLayoutDate.getEditText().setText("" + dateText);
                }
            }
        });
        materialButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    progressDialog.show();
                    String eventId = String.valueOf(System.currentTimeMillis());
                    Event event = new Event(name, date, eventId, time, switchReminder.isChecked(), "");
                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("events").child(eventId).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {

                                Toast.makeText(Main2Activity.this, "Event Created Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {

                                Toast.makeText(Main2Activity.this, "Event Created Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }
        });


    }

    public boolean validate() {
        name = textInputLayoutName.getEditText().getText().toString();
        date = textInputLayoutDate.getEditText().getText().toString();
        time = textInputLayoutTime.getEditText().getText().toString();
        if (name.isEmpty()) {
            textInputLayoutName.setError("Required");
            return false;
        }
        if (date.isEmpty()) {
            textInputLayoutDate.setError("Required");
            return false;
        }
        if (time.isEmpty()) {
            textInputLayoutTime.setError("Required");
            return false;
        }
        return true;
    }
}