package com.birdicomputers.login;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static android.view.View.GONE;

public class AddTask extends AppCompatActivity {
    private int mDay, mMonth, mYear, hourofDay, minutes;
    private EditText selectDate, selectTime, taskText;
    private Switch showReminder;
    private ConstraintLayout remiderLayout;
    private Button save;
    private String selectedImageUrl_1;
    private TaskDBHelper taskDBHelper;
    private ReminderReciver reminderReciver;
    private int NOTIFICATION_ID = 1;
    private int minuteHour;
    private ImageView attachedImage;
    private static int SELECT_PHOTO = 1;
    private String currentString, hour, minute;
    private String[] separated;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        createNotificationChannel();
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        hourofDay = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
        selectDate = findViewById(R.id.selectDate);
        selectDate.setFocusable(false);
        attachedImage = findViewById(R.id.attachmentImage1);
        Picasso.get().load(R.drawable.attachimage).centerInside().fit().into(attachedImage);
        selectTime = findViewById(R.id.selectTime);
        selectTime.setFocusable(false);
        showReminder = findViewById(R.id.reminderSwitch);
        remiderLayout = findViewById(R.id.showReminderLayout);
        save = findViewById(R.id.AddTaskButton);
        taskText = findViewById(R.id.tastTextbox);
        taskDBHelper = new TaskDBHelper(this);
        reminderReciver = new ReminderReciver();
        AccentColors();
        IntentFilter intentFilter = new IntentFilter("com.example.Broadcast");
        registerReceiver(reminderReciver,intentFilter);
        showReminder.toggle();
        showReminder.setText("Toggle to add Note :");
        showReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(showReminder.isChecked()){
                showReminder.setText("Toggle to add Note :");
                remiderLayout.setVisibility(View.VISIBLE);
                attachedImage.setVisibility(View.VISIBLE);
            }
            else{
                remiderLayout.setVisibility(View.INVISIBLE);
                showReminder.setText("Toggle to add Reminder :");
                attachedImage.setVisibility(GONE);
            }
        });
        selectDate.setOnClickListener(v -> ShowDatePicker());
        selectTime.setOnClickListener(v -> ShowTimePicker());
        String getViwOrUpdateIntent = getIntent().getStringExtra("clickedPosition");

        try{
            if(getViwOrUpdateIntent != null){
                showReminder.setVisibility(View.INVISIBLE);
                if (showReminder.isChecked()) {
                    String[] getPreviousData = taskDBHelper.searchReminders(getViwOrUpdateIntent);
                    taskText.setText(getPreviousData[1], TextView.BufferType.EDITABLE);
                    selectDate.setText(getPreviousData[2], TextView.BufferType.EDITABLE);
                    selectTime.setText(getPreviousData[3], TextView.BufferType.EDITABLE);
                    selectedImageUrl_1 = getPreviousData[4];
                    showReminder.setVisibility(View.VISIBLE);
                    Calendar dateAndTimeCheck = Calendar.getInstance();
                    dateAndTimeCheck.setTimeInMillis(System.currentTimeMillis());
                    Picasso.get().load(getPreviousData[4]).placeholder(R.drawable.attachimage).centerInside().fit()
                            .into((ImageView) findViewById(R.id.attachmentImage1));
                    save.setOnClickListener(v -> {
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        if (taskText.getText().length()>0) {
                            if (selectDate.getText().length()>0 && selectTime.getText().length()>0) {
                                currentString = selectTime.getText().toString();
                                separated = currentString.split(":");
                                hour = separated[0].trim();
                                minute = separated[1].trim();
                                SimpleDateFormat sdfs = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                                Date strDate = null;
                                String dateTime = selectDate.getText().toString() +" "+ hour +":"+ minute;
                                try {
                                    strDate = sdfs.parse(dateTime);
                                } catch (ParseException e) {
                                    Log.d("Parse",e.getMessage());
                                }
                                if (System.currentTimeMillis()-10000 <= strDate.getTime()) {
                                    taskDBHelper.UpdateTasks(getViwOrUpdateIntent,
                                            taskText.getText().toString(),
                                            selectDate.getText().toString(),
                                            selectTime.getText().toString(),
                                            selectedImageUrl_1, String.valueOf(NOTIFICATION_ID));
                                    setPushNotification();
                                    finish();
                                }
                                else{
                                    Toast.makeText(this, "Reminder time cannot be in past!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddTask.this, "Please select date and time.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddTask.this, "Please enter some text.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    String[] getPreviousData = taskDBHelper.searchReminders(getViwOrUpdateIntent);
                    taskText.setText(getPreviousData[1], TextView.BufferType.EDITABLE);
                    selectDate.setText(getPreviousData[2], TextView.BufferType.EDITABLE);
                    selectTime.setText(getPreviousData[3], TextView.BufferType.EDITABLE);
                    selectedImageUrl_1 = getPreviousData[4];
                    NOTIFICATION_ID = Integer.parseInt(getPreviousData[6]);
                    Picasso.get().load(getPreviousData[4]).placeholder(R.drawable.attachimage).centerInside().fit()
                            .into((ImageView) findViewById(R.id.attachmentImage1));
                    showReminder.toggle();
                    showReminder.setVisibility(View.VISIBLE);
                    if (taskText.getText().length()>0) {
                        if (selectDate.getText().length()>0 && selectTime.getText().length()>0) {
                            save.setOnClickListener(v -> {
                                taskDBHelper.UpdateTasks(getViwOrUpdateIntent,
                                        taskText.getText().toString(),
                                        selectDate.getText().toString(),
                                        selectTime.getText().toString(),
                                        selectedImageUrl_1, String.valueOf(NOTIFICATION_ID));
                                finish();
                            });
                        }
                        else {
                            Toast.makeText(AddTask.this, "Please select date and time.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(AddTask.this, "Please enter some text.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else{
                NOTIFICATION_ID = Integer.parseInt(getIntent().getStringExtra("Itemcount"));
                taskDBHelper = new TaskDBHelper(AddTask.this);
                save.setOnClickListener(v -> {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    if(showReminder.isChecked()) {
                        if (taskText.getText().length()>0) {
                            if (selectDate.getText().length() > 0 && selectTime.getText().length() > 0) {
                                Date strDate = null;
                                try {
                                    currentString = selectTime.getText().toString();
                                    separated = currentString.split(":");
                                    hour = separated[0].trim();
                                    minute = separated[1].trim();
                                    SimpleDateFormat sdfs = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                                    String dateTime = selectDate.getText().toString() + " " + hour + ":" + minute;
                                    strDate = sdfs.parse(dateTime);
                                } catch (ParseException e) {
                                    Log.d("Parse", e.getMessage());
                                }
                                if (System.currentTimeMillis() - 10000 <= strDate.getTime()) {
                                    taskDBHelper.addTask(taskText.getText().toString(),
                                            selectDate.getText().toString(), selectTime.getText().toString(),
                                            selectedImageUrl_1, String.valueOf(NOTIFICATION_ID));
                                    setPushNotification();
                                    finish();
                                } else {
                                    Toast.makeText(this, "Reminder time cannot be in past!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddTask.this, "Please select date and time.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(AddTask.this, "Please enter some text.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if (taskText.getText().length()>0) {
                            taskDBHelper.addNote(taskText.getText().toString(),
                                    selectDate.getText().toString(), selectTime.getText().toString());
                            finish();
                        }
                        else {
                            Toast.makeText(AddTask.this, "Please enter some text.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (Exception e){
            Log.d("UpdateTask",e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.attachButton) {
            if (showReminder.isChecked()) {
                Intent selectImage = new Intent();
                selectImage.setType("image/*");
                selectImage.setAction(Intent.ACTION_OPEN_DOCUMENT);
                selectImage.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(selectImage, "Photo"), SELECT_PHOTO);
            }
            else{
                Toast.makeText(this, "You cannot attach image in a note.", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            Uri selectedPhoto = data.getData();
            selectedImageUrl_1 = selectedPhoto.toString();
            Picasso.get().setLoggingEnabled(true);
            getContentResolver().takePersistableUriPermission(selectedPhoto, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Picasso.get().load(selectedImageUrl_1).placeholder(R.drawable.attachimage).centerInside().fit()
                        .into((ImageView) findViewById(R.id.attachmentImage1));
        }
        catch (Exception e){
            Log.d("onActivityResult", e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(){
        CharSequence channelName = "Reminders";
        String description = "Channel for reminders";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("Reminders", channelName, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void ShowDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            selectDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            mDay = dayOfMonth;
            mMonth = monthOfYear;
            mYear = year;
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void ShowTimePicker(){
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog Timepicker =
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance((view, hourOfDay, minute, second) -> {
            selectTime.setText(hourOfDay+":"+minute);
            hourofDay = hourOfDay;
            minutes = minute;
            minuteHour = Integer.parseInt(hourOfDay+""+minute);
        }, hourofDay, minutes,true);
        Timepicker.show(getSupportFragmentManager(),"TimePickerDialog");
    }

    public void setPushNotification(){
        Calendar myAlarmDate = Calendar.getInstance();
        myAlarmDate.setTimeInMillis(System.currentTimeMillis());
        myAlarmDate.set(mYear, mMonth, mDay, hourofDay, minutes);

        Intent notif = new Intent(AddTask.this, ReminderReciver.class);
        notif.setAction("com.example.Broadcast");
        notif.putExtra("notificationText", taskText.getText().toString());
        notif.putExtra("notificationID", String.valueOf(1));
        try {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTask.this, NOTIFICATION_ID, notif, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), pendingIntent);
        }
        catch (NumberFormatException e){
            Log.d("Reminder :", e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reminderReciver);
    }

    public void AccentColors(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTheme)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorTheme, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorTheme));
        }
    }
}
