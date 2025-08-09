package com.example.fitmybody; // Kendi paket adınızla değiştirin

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private TextView kronometerText;
    private ArrayList<Spor>haraketler = new ArrayList<>();
    private static final String TAG = "AdimSayarApp";
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1001;
    private static final int ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE = 1002;
    private Handler handler;
    private long startTime;
    private long timeInmiliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private boolean isRunning = false;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyChronometerPrefs";
    private static final String KEY_TIME_SWAP_BUFF = "timeSwapBuff";
    private static final String KEY_IS_RUNNING = "isRunning";
    private static final String KEY_LAST_START_TIME = "lastStartTime";
    private TextView stepDisplay, textView;
    private CircleImageView userPhoto;
    private FirebaseAuth mAuth;
    String CurrentUserlogin;
    private TextView calorieDisplay;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private List<WorkoutDay> workoutDays;
    private TextView gunBilgisiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            CurrentUserlogin = mUser.getUid();
            showUserInfo(CurrentUserlogin);
        } else {
            Toast.makeText(this, "Kullanıcı oturum açmamış. Lütfen giriş yapın.", Toast.LENGTH_SHORT).show();
            // Kullanıcı giriş yapmamışsa, login ekranına yönlendirme yapılabilir.
            // Örneğin: startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        db = FirebaseFirestore.getInstance();

        kronometerText = findViewById(R.id.chronometer);
        ImageView playtimer = findViewById(R.id.playTimer);
        ImageView pausetimer = findViewById(R.id.pauseTimer);
        ImageView gogus = findViewById(R.id.gogus);
        ImageView onkol = findViewById(R.id.onkol);
        userPhoto = findViewById(R.id.profilResmi);
        textView = findViewById(R.id.merhabaText);
        stepDisplay = findViewById(R.id.steps_display);
        calorieDisplay = findViewById(R.id.calorieDisplay);
        gunBilgisiText = findViewById(R.id.gunBilgisiText);

        recyclerView = findViewById(R.id.bugunYapilacaklarRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutDays = new ArrayList<>();
        adapter = new WorkoutAdapter(this, workoutDays);
        recyclerView.setAdapter(adapter);

        fetchWorkoutProgram();

        playtimer.setOnClickListener(v -> {
            if (!isRunning) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(updateTimeRunnable, 0);
                isRunning = true;
                saveState();
            }
        });

        pausetimer.setOnClickListener(v -> {
            if (isRunning) {
                timeSwapBuff += timeInmiliseconds;
                handler.removeCallbacks(updateTimeRunnable);
                isRunning = false;
                saveState();
            }
        });

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Profile_page.class));
            }
        });

        gogus.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, bench_press.class)));
        onkol.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, onKol.class)));
        findViewById(R.id.arkakol).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ArkaKol.class)));
        findViewById(R.id.bacak).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, bacak.class)));
        findViewById(R.id.Sirt).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Sirt.class)));
        findViewById(R.id.omuz).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Omuz.class)));
        findViewById(R.id.karin).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Karin.class)));


        handler = new Handler();
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        timeSwapBuff = sharedPreferences.getLong(KEY_TIME_SWAP_BUFF, 0L);
        isRunning = sharedPreferences.getBoolean(KEY_IS_RUNNING, false);
        long lastStartTime = sharedPreferences.getLong(KEY_LAST_START_TIME, 0L);

        if (isRunning && lastStartTime != 0L) {
            startTime = SystemClock.uptimeMillis() - (System.currentTimeMillis() - lastStartTime);
            handler.postDelayed(updateTimeRunnable, 0);
        } else {
            updateChronometerDisplay(timeSwapBuff);
        }

        scheduleMidnightReset();
        requestActivityRecognitionPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_GRANTED && GoogleSignIn.getLastSignedInAccount(this) != null) {
            connectToGoogleFit();
        }
        fetchWorkoutProgram();
    }


    private void showUserInfo(String currentUserlogin) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User")
                .child(currentUserlogin)
                .child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String takeUserName = snapshot.getValue(String.class);
                            textView.setText(takeUserName+"  Hoşgeldiniz");
                        }else {
                            textView.setText("Hoşgelidiniz");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show();
                    }
                });

        mDatabase.child("User")
                .child(currentUserlogin)
                .child("profileImageUrl")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String imageurl = snapshot.getValue(String.class);
                            if (imageurl != null && !imageurl.isEmpty()){
                                Glide.with(MainActivity.this)
                                        .load(imageurl)
                                        .placeholder(R.drawable.ic_kullanici_profil)
                                        .error(R.drawable.error_image)
                                        .into(userPhoto);
                            }else {
                                userPhoto.setImageResource(R.drawable.ic_kullanici_profil);
                                Toast.makeText(MainActivity.this, "Kullanıcı fotosu çekilemedi", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            userPhoto.setImageResource(R.drawable.ic_kullanici_profil);
                            Toast.makeText(MainActivity.this, "Kullanıcı resmi bulunamadı", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Resim URL'si alınamadı", Toast.LENGTH_SHORT).show();
                        userPhoto.setImageResource(R.drawable.error_image);
                    }
                });
    }

    // --- Google Fit ve Adım Sayar Metotları ---
    private void requestActivityRecognitionPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                        ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE);
            } else {
                connectToGoogleFit();
            }
        } else {
            connectToGoogleFit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectToGoogleFit();
            } else {
                Toast.makeText(this, "Adım ve kalori verilerine erişim için fiziksel aktivite izni gereklidir.", Toast.LENGTH_LONG).show();
                Log.w(TAG, "ACTIVITY_RECOGNITION izni reddedildi.");
                stepDisplay.setText("İzin Verilmedi");
                calorieDisplay.setText("İzin Verilmedi");
            }
        }
    }

    private void connectToGoogleFit() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .build();
        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        } else {
            readDailyFitnessData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                readDailyFitnessData();
            } else {
                Toast.makeText(this, "Google Fit bağlantısı başarısız oldu. Adım ve kalori verileri çekilemiyor.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Google Fit izinleri reddedildi.");
                stepDisplay.setText("Bağlantı Hatası");
                calorieDisplay.setText("Bağlantı Hatası");
            }
        }
    }

    private void readDailyFitnessData() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTime = cal.getTimeInMillis();
        Log.d(TAG, "Bugünkü fitness verilerini okuma aralığı: " + new Date(startTime) + " - " + new Date(endTime));

        FitnessOptions fitnessOptionsForRead = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .build();

        Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptionsForRead))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        dataSet -> {
                            long totalSteps = 0;
                            if (dataSet != null && !dataSet.isEmpty()) {
                                for (com.google.android.gms.fitness.data.DataPoint dp : dataSet.getDataPoints()) {
                                    for (Field field : dp.getDataType().getFields()) {
                                        if (field.equals(Field.FIELD_STEPS)) {
                                            totalSteps += dp.getValue(field).asInt();
                                        }
                                    }
                                }
                            }
                            Log.d(TAG, "Bugün atılan toplam adım: " + totalSteps);
                            stepDisplay.setText(String.valueOf(totalSteps) + " Adım");
                        })
                .addOnFailureListener(
                        e -> {
                            Log.e(TAG, "Günlük adım sayısını okuma başarısız oldu", e);
                            Toast.makeText(this, "Adım sayısını çekerken hata oluştu.", Toast.LENGTH_SHORT).show();
                            stepDisplay.setText("Hata!");
                        });

        Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptionsForRead))
                .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(
                        dataSet -> {
                            float totalCalories = 0.0f;
                            if (dataSet != null && !dataSet.isEmpty()) {
                                for (com.google.android.gms.fitness.data.DataPoint dp : dataSet.getDataPoints()) {
                                    for (Field field : dp.getDataType().getFields()) {
                                        if (field.equals(Field.FIELD_CALORIES)) {
                                            totalCalories += dp.getValue(field).asFloat();
                                        }
                                    }
                                }
                            }
                            String formattedCalories = String.format("%.2f", totalCalories);
                            Log.d(TAG, "Bugün yakılan toplam kalori: " + formattedCalories);
                            calorieDisplay.setText(formattedCalories + " kcal");
                        })
                .addOnFailureListener(
                        e -> {
                            Log.e(TAG, "Günlük kalori verisini okuma başarısız oldu", e);
                            Toast.makeText(this, "Kalori çekerken hata oluştu.", Toast.LENGTH_SHORT).show();
                            calorieDisplay.setText("Hata!");
                        });
    }


    // --- Kronometre Metotları ---
    private Runnable updateTimeRunnable = new Runnable() {
        public void run() {
            timeInmiliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInmiliseconds;
            updateChronometerDisplay(updatedTime);
            handler.postDelayed(this, 10);
        }
    };

    private void updateChronometerDisplay(long milliseconds) {
        int seconds = (int) (milliseconds / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int hours = minutes / 60;
        minutes = minutes % 60;
        kronometerText.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    private void saveState() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_TIME_SWAP_BUFF, timeSwapBuff);
        editor.putBoolean(KEY_IS_RUNNING, isRunning);
        if (isRunning) {
            editor.putLong(KEY_LAST_START_TIME, System.currentTimeMillis());
        } else {
            editor.remove(KEY_LAST_START_TIME);
        }
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable);
    }

    private void scheduleMidnightReset() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MidnightResetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
        Log.d("Kronometre", "Gece yarısı sıfırlama, " + calendar.getTime().toString() + " için planlandı.");
    }


    // --- DİNAMİK VERİ ÇEKME VE GÜNCELLEME METOTLARI ---
    public void fetchWorkoutProgram() {
        if (CurrentUserlogin == null) {
            Log.e("Firestore", "Kullanıcı ID'si bulunamadığı için veriler çekilemedi.");
            updateUIWithData(new ArrayList<>());
            return;
        }

        gunBilgisiText.setText("Program Yükleniyor...");

        // Adım 1: Kullanıcının program başlangıç tarihini çek
        db.collection("users").document(CurrentUserlogin)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("programBaslangicTarihi")) {
                        Date programStartDate = documentSnapshot.getDate("programBaslangicTarihi");
                        if (programStartDate != null) {
                            long diffInMillies = Math.abs(new Date().getTime() - programStartDate.getTime());
                            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            int currentWorkoutDay = (int) diff + 1;
                            fetchWorkoutForDay(currentWorkoutDay);
                        } else {
                            Log.e("Firestore", "Program başlangıç tarihi null. Varsayılan olarak 1. gün çekiliyor.");
                            fetchWorkoutForDay(1); // Varsayılan olarak 1. gün
                        }
                    } else {
                        Log.d("Firestore", "Program başlangıç tarihi bulunamadı, varsayılan olarak 1. gün çekiliyor.");
                        fetchWorkoutForDay(1);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Kullanıcı verileri çekilirken hata: ", e);
                    updateUIWithData(new ArrayList<>());
                });
    }

    private void fetchWorkoutForDay(int dayNumber) {
        CollectionReference workoutRef = db.collection("users").document(CurrentUserlogin)
                .collection("workouts");

        workoutRef.whereEqualTo("dayNumber", dayNumber).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                List<WorkoutDay> fetchedWorkoutDays = new ArrayList<>();
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                WorkoutDay workoutDay = document.toObject(WorkoutDay.class);

                document.getReference().collection("exercises").get()
                        .addOnCompleteListener(exerciseTask -> {
                            if (exerciseTask.isSuccessful()) {
                                List<Exercise> exercises = new ArrayList<>();
                                for (QueryDocumentSnapshot exerciseDoc : exerciseTask.getResult()) {
                                    exercises.add(exerciseDoc.toObject(Exercise.class));
                                }
                                workoutDay.setExercises(exercises);
                                fetchedWorkoutDays.add(workoutDay);
                                updateUIWithData(fetchedWorkoutDays);
                            } else {
                                Log.e("Firestore", "Hareketler çekilirken hata: ", exerciseTask.getException());
                                updateUIWithData(new ArrayList<>());
                            }
                        });
            } else {
                Log.e("Firestore", "Belirli bir gün için program bulunamadı: " + dayNumber);
                updateUIWithData(new ArrayList<>());
            }
        });
    }

    private void updateUIWithData(List<WorkoutDay> data) {
        if (adapter == null) {
            adapter = new WorkoutAdapter(this, data);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setWorkoutDays(data);
            adapter.notifyDataSetChanged();
        }

        if (!data.isEmpty()) {
            int dayNumber = data.get(0).getDayNumber();
            gunBilgisiText.setText(dayNumber + ". Gün Programı");
        } else {
            gunBilgisiText.setText("Bugüne ait program bulunamadı.");
        }
    }
}