package com.example.fitmybody;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profile_page extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView menuIcon;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String ciniyet;
    private FirebaseUser mUser;
    private DatabaseReference mDataBase;
    private TextView profileWeight, profileHeight, profileAge, VkiTextView;
    private String currenUserID;
    private String vkiResult;
    private List<Spor> tumHaraketler;
    private OkHttpClient client;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    // Navigation Drawer Header elemanları
    private TextView weightTextView, heightTextView, ageTextView;
    private Button programCreate, btnSave;
    private ImageView btnMale, btnFemale;

    // RecyclerView ve Adapter
    private RecyclerView recyclerViewWeeklyProgram;
    private WeeklyProgramAdapter weeklyProgramAdapter;
    private List<DailyProgramSummary> weeklyProgramList;

    // Günlük Aktivite Raporu için
    private TextView totalKcalTextView;
    private LinearLayout dailyExercisesLayout;
    private CircleImageView profileImage;
    private TextView NameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);

        // Status bar'ın rengini değiştirmek için kod
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // Renk kodunu doğrudan atayabilirsiniz
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.google_light_gray));

            // Veya direkt hex kodunu da kullanabilirsiniz:
            // window.setStatusBarColor(Color.parseColor("#F5F5F5"));
        }


        mAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menuIcon);
        drawerLayout = findViewById(R.id.drawer_layout);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        profileImage = findViewById(R.id.profilePicture);
        NameTextView = findViewById(R.id.profileName);

        
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Program oluşturuluyor...");
        progressDialog.setCancelable(false);

        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            startActivity(new Intent(Profile_page.this, AppLoginPage.class));
            finish();
            return;
        }
        currenUserID = mUser.getUid();

        VkiTextView = findViewById(R.id.vkiDegeri);
        profileAge = findViewById(R.id.profilePageAge);
        profileHeight = findViewById(R.id.profilePageHeight);
        profileWeight = findViewById(R.id.profilePageWeight);

        totalKcalTextView = findViewById(R.id.totalKcalTextView);
        dailyExercisesLayout = findViewById(R.id.dailyExercisesLayout);

        View headerView = navigationView.getHeaderView(0);
        weightTextView = headerView.findViewById(R.id.weight);
        heightTextView = headerView.findViewById(R.id.height_text);
        ageTextView = headerView.findViewById(R.id.age_text);
        btnMale = headerView.findViewById(R.id.btn_male);
        btnFemale = headerView.findViewById(R.id.btn_female);
        btnSave = headerView.findViewById(R.id.onayla);
        programCreate = headerView.findViewById(R.id.programOlustur);

        recyclerViewWeeklyProgram = findViewById(R.id.recyclerViewWeeklyProgram);
        recyclerViewWeeklyProgram.setLayoutManager(new LinearLayoutManager(this));
        weeklyProgramList = new ArrayList<>();
        weeklyProgramAdapter = new WeeklyProgramAdapter(weeklyProgramList);
        recyclerViewWeeklyProgram.setAdapter(weeklyProgramAdapter);

        tumHaraketler = new ArrayList<>();
        initTumHaraketler();

        findViewById(R.id.logouticon).setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(Profile_page.this, AppLoginPage.class));
            finish();
        });

        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        btnMale.setOnClickListener(v -> {
            ciniyet = "erkek";
            Toast.makeText(Profile_page.this, "Cinsiyet: Erkek", Toast.LENGTH_SHORT).show();
        });
        btnFemale.setOnClickListener(v -> {
            ciniyet = "kadın";
            Toast.makeText(Profile_page.this, "Cinsiyet: Kadın", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> {
            String weightStr = weightTextView.getText().toString();
            String heightStr = heightTextView.getText().toString();
            String ageStr = ageTextView.getText().toString();

            if (!weightStr.isEmpty() && !heightStr.isEmpty() && !ageStr.isEmpty() && ciniyet != null) {
                try {
                    double newHeight = Double.parseDouble(heightStr);
                    double newWeight = Double.parseDouble(weightStr);
                    double heightInmeters = newHeight / 100.0;

                    double vkiDegeribulundu = newWeight / (heightInmeters * heightInmeters);
                    DecimalFormat df = new DecimalFormat("##.##");
                    vkiResult = df.format(vkiDegeribulundu);

                    saveToFirebase(weightStr, heightStr, ageStr, ciniyet, currenUserID, vkiResult);
                } catch (NumberFormatException e) {
                    Toast.makeText(Profile_page.this, "Lütfen geçerli sayılar girin.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Profile_page.this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            }
        });

        programCreate.setOnClickListener(v -> {
            String weight = profileWeight.getText().toString().replace(" kg", "").trim();
            String height = profileHeight.getText().toString().replace(" cm", "").trim();
            String age = profileAge.getText().toString().trim();
            String vki = VkiTextView.getText().toString().replace(" VKI", "").trim();

            if (weight.isEmpty() || height.isEmpty() || age.isEmpty() || vki.isEmpty()) {
                Toast.makeText(this, "Lütfen önce profil bilgilerinizi kaydedin.", Toast.LENGTH_LONG).show();
            } else {
                askAI(weight, height, age, vki);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        mDataBase.child("Users").child(currenUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String kilo = snapshot.child("Weight").getValue(String.class);
                            String boy = snapshot.child("Height").getValue(String.class);
                            String yas = snapshot.child("Age").getValue(String.class);
                            String vki = snapshot.child("VKI").getValue(String.class);
                            if (kilo != null) profileWeight.setText(kilo + " kg");
                            if (boy != null) profileHeight.setText(boy + " cm");
                            if (yas != null) profileAge.setText(yas);
                            if (vki != null) VkiTextView.setText(vki + " VKI");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Profile_page.this, "Veriler çekilemedi", Toast.LENGTH_SHORT).show();
                    }
                });

        mDataBase.child("User")
                .child(currenUserID)
                .child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String takeUserName = snapshot.getValue(String.class);
                            NameTextView.setText(takeUserName);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Profile_page.this, "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show();
                    }
                });

        mDataBase.child("User")
                .child(currenUserID)
                .child("profileImageUrl")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String imageurl = snapshot.getValue(String.class);
                            if (imageurl != null && !imageurl.isEmpty()){
                                Glide.with(Profile_page.this)
                                        .load(imageurl)
                                        .placeholder(R.drawable.ic_kullanici_profil)
                                        .error(R.drawable.error_image)
                                        .into(profileImage);
                            }else {
                                profileImage.setImageResource(R.drawable.ic_kullanici_profil);
                                Toast.makeText(Profile_page.this, "Kullanıcı fotosu çekilemedi", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            profileImage.setImageResource(R.drawable.ic_kullanici_profil);
                            Toast.makeText(Profile_page.this, "Kullanıcı resmi bulunamadı", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Profile_page.this, "Resim URL'si alınamadı", Toast.LENGTH_SHORT).show();
                        profileImage.setImageResource(R.drawable.error_image);
                    }
                });

        fetchWeeklyProgramFromFirestore();
        fetchTodayWorkoutFromFirestore();

    }


    private void fetchTodayWorkoutFromFirestore() {
        if (currenUserID == null) return;

        // Bugünün programını çekmek için bugünün sayısını kullan
        // Basit bir yaklaşım için ilk günü alalım (day_1)
        String todayDocumentPath = "day_1";

        db.collection("users").document(currenUserID)
                .collection("workouts").document(todayDocumentPath)
                .collection("exercises")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        long totalKcal = 0;
                        dailyExercisesLayout.removeAllViews(); // Önceki görünümleri temizle

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String exerciseName = document.getString("name");
                            // Firestore'dan doğrudan kalori değerini al
                            long kcalLong = document.getLong("calories") != null ? document.getLong("calories") : 0;
                            int kcal = (int) kcalLong;
                            totalKcal += kcal;

                            // Yeni bir TextView oluştur ve ekle
                            LinearLayout exerciseItem = createExerciseItemView(exerciseName, kcal);
                            dailyExercisesLayout.addView(exerciseItem);
                        }
                        totalKcalTextView.setText(totalKcal + " kcal");
                    } else {
                        Log.w("Firestore", "Bugünün programı çekilemedi.", task.getException());
                    }
                });
    }

    private LinearLayout createExerciseItemView(String exerciseName, int kcal) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setPadding(0, 8, 0, 8);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayout.setLayoutParams(params);

        // Egzersiz adı
        TextView nameTextView = new TextView(this);
        nameTextView.setText(exerciseName);
        nameTextView.setTextColor(getResources().getColor(android.R.color.white));
        nameTextView.setTextSize(14);
        nameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
        ));

        // Kalori değeri
        TextView kcalTextView = new TextView(this);
        kcalTextView.setText(String.valueOf(kcal) + " kcal");
        kcalTextView.setTextColor(getResources().getColor(R.color.grey_500));
        kcalTextView.setTextSize(12);

        linearLayout.addView(nameTextView);
        linearLayout.addView(kcalTextView);

        return linearLayout;
    }


    private void fetchWeeklyProgramFromFirestore() {
        if (currenUserID == null) return;

        db.collection("users").document(currenUserID)
                .collection("workouts")
                .whereLessThanOrEqualTo("dayNumber", 7)
                .orderBy("dayNumber", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DailyProgramSummary> tempProgramList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            int dayNumber = Objects.requireNonNull(document.getLong("dayNumber")).intValue();
                            String bodyPart = document.getString("bodyPart");
                            tempProgramList.add(new DailyProgramSummary(dayNumber, bodyPart));
                        }
                        weeklyProgramAdapter.updateData(tempProgramList);
                    } else {
                        Log.w("Firestore", "Veri çekme başarısız.", task.getException());
                        Toast.makeText(this, "Haftalık program yüklenemedi.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initTumHaraketler() {
        // Bu metod, kullanıcıya özel program oluşturma sırasında AI'a
        // doğru resim adlarını göndermek için bir referans listesi sağlar.
        tumHaraketler.add(new Spor("Kickback", R.drawable.kickbak_unscreen));
        tumHaraketler.add(new Spor("Triceps Pushdown", R.drawable.pushdown_unscreen));
        tumHaraketler.add(new Spor("Dips Triceps", R.drawable.dips_unscreen));
        tumHaraketler.add(new Spor("Dar Bench Press", R.drawable.close_grip_unscreen));
        tumHaraketler.add(new Spor("Skull Crusher", R.drawable.skull_unscreen));
        tumHaraketler.add(new Spor("Baş Üstü Triceps", R.drawable.basust_unscreen));

        tumHaraketler.add(new Spor("Squat", R.drawable.squats_unscreen));
        tumHaraketler.add(new Spor("Lunge (Adım Lunge)", R.drawable.lunge_unscreen));
        tumHaraketler.add(new Spor("Leg Press (Makinede İtiş)", R.drawable.leg_press_unscreen));
        tumHaraketler.add(new Spor("Leg Curl", R.drawable.leg_curl_unscreen));
        tumHaraketler.add(new Spor("Leg Extension", R.drawable.leg_extension_unscreen));
        tumHaraketler.add(new Spor("Hip Thrust", R.drawable.hip_thrust_unscreen));
        tumHaraketler.add(new Spor("Calf Raise", R.drawable.calf_raise_unscreen));
        tumHaraketler.add(new Spor("Glute Kickback", R.drawable.glute_kickback_unscreen));

        tumHaraketler.add(new Spor("Incline Bench Press", R.drawable.incline_dumbbell_press_unscreen));
        tumHaraketler.add(new Spor("Decline Bench Press", R.drawable.decline_bench_press));
        tumHaraketler.add(new Spor("Dumbbell Fly", R.drawable.dumbbell_fly_unscreen));
        tumHaraketler.add(new Spor("Flat Bench Press", R.drawable.flat_bench_press_unscreen));
        tumHaraketler.add(new Spor("Cable Crossover", R.drawable.kablo_ile_unscreen));
        tumHaraketler.add(new Spor("Push up", R.drawable.push_up_unscreen));

        tumHaraketler.add(new Spor("Crunch", R.drawable.crunch_unscreen));
        tumHaraketler.add(new Spor("Lying Leg Raise", R.drawable.lyingleg_unscreen));
        tumHaraketler.add(new Spor("Plank", R.drawable.plank_unscreen));
        tumHaraketler.add(new Spor("Russian Twist", R.drawable.russiantwist_unscreen));
        tumHaraketler.add(new Spor("Bicycle Crunch", R.drawable.bicycle_unscreen));
        tumHaraketler.add(new Spor("Mountain Climber", R.drawable.mountain_unscreen));
        tumHaraketler.add(new Spor("Reverse Crunch", R.drawable.reversecrunch_unscreen));
        tumHaraketler.add(new Spor("Decline Bench Sit-Up", R.drawable.decline_unscreen));

        tumHaraketler.add(new Spor("Overhead Press", R.drawable.overhead_unscreen));
        tumHaraketler.add(new Spor("Lateral Raise", R.drawable.lateral_raise_unscreen));
        tumHaraketler.add(new Spor("Front Raise", R.drawable.front_raise_unscreen));
        tumHaraketler.add(new Spor("Reverse Fly", R.drawable.reverse_unscreen));
        tumHaraketler.add(new Spor("Arnold Press", R.drawable.arnold_unscreen));
        tumHaraketler.add(new Spor("Face Pull", R.drawable.facep_omuz_unscreen));
        tumHaraketler.add(new Spor("Upright Row", R.drawable.upright_unscreen));
        tumHaraketler.add(new Spor("Leaning Lateral Raise", R.drawable.leaning_unscreen));

        tumHaraketler.add(new Spor("Barbell Curl", R.drawable.barbell_bench_press_unscreen));
        tumHaraketler.add(new Spor("Dumbbell Curl", R.drawable.dumball_curl_unscreen));
        tumHaraketler.add(new Spor("Hammer Curl", R.drawable.incline_dumbbell_press_unscreen));
        tumHaraketler.add(new Spor("Concentration Curl", R.drawable.concentrationcurls_unscreen));
        tumHaraketler.add(new Spor("Preacher Curl", R.drawable.preacher_curl_unscreen));
        tumHaraketler.add(new Spor("Cable Curl", R.drawable.cable_onkol));

        tumHaraketler.add(new Spor("Pull-Up", R.drawable.pull_up_unscreen));
        tumHaraketler.add(new Spor("Lat Pulldown", R.drawable.lat_pull_down_unscreen));
        tumHaraketler.add(new Spor("Barbell Row", R.drawable.barbell_row_unscreen));
        tumHaraketler.add(new Spor("Seated Cable Row", R.drawable.seated_cable_unscreen));
        tumHaraketler.add(new Spor("T-Bar Row", R.drawable.t_bar_row_unscreen));
        tumHaraketler.add(new Spor("Dumbbell Row", R.drawable.dumbell_row_unscreen));
        tumHaraketler.add(new Spor("Face Pull", R.drawable.face_pull_unscreen));
        tumHaraketler.add(new Spor("Deadlift", R.drawable.deadlift_unscreen));
    }


    public void askAI(String kilo, String boy, String yas, String vkiDegeri){
        String UserPrompt;
        if (tumHaraketler != null && !tumHaraketler.isEmpty()) {
            UserPrompt = "Bir fitness programı oluşturmanız gerekiyor. Lütfen yalnızca bir JSON nesnesi döndürün ve başka hiçbir metin, açıklama veya giriş/çıkış cümlesi eklemeyin. İşte kullanıcı verileri: kilom " + kilo + " boyum santimetre cinsinden" + boy + " yaşım" + yas + " VKİ değerim ise" + vkiDegeri + ". Bu verilere göre 7 günlük bir program döngüsü hazırlayın. Programın her günü için çalışılan ana kas grubunu ('bodyPart') ve o gün yapılacak egzersizleri ('exercises') belirtin. 30 günlük program için bu 7 günlük döngü tekrar edecektir. Yanıt formatınız kesinlikle aşağıdaki gibi olmalı: " +
                    "```json\n" +
                    "{\n" +
                    "  \"weeklyProgram\": [\n" +
                    "    {\n" +
                    "      \"dayNumber\": 1,\n" +
                    "      \"bodyPart\": \"Omuz & Triceps\",\n" +
                    "      \"exercises\": [\n" +
                    "        {\"name\": \"Dumbbell Shoulder Press\", \"setsReps\": \"3x12\", \"imageName\": \"dumble_shoulder_press_unscreen\", \"calories\": 75}\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"dayNumber\": 2,\n" +
                    "      \"bodyPart\": \"Dinlenme/Kardiyo\",\n" +
                    "      \"exercises\": [\n" +
                    "        {\"name\": \"Bisiklet\", \"setsReps\": \"30 dakika\", \"imageName\": \"cardio_image_unscreen\", \"calories\": 150}\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}\n" +
                    "```" +
                    "Bu formata uygun 7 günlük bir program döngüsü hazırla. Hareket isimlerini İngilizce orijinal adıyla ver ve resim isimlerini de (örneğin push_up_unscreen) ekle, ama resim isimlerini benim sana verdiğim haraketlerin isimleriyle ver karmaşıklık çıkıyor."+
                    "Sana verilen verilerde zaten kullanıcının kilosu, boyu, yaşı ve VKI değerleri var. Yaptığın programı kullanacak olan bu kullanıcının, bu bilgilerine göre her bir hareketten tahmini olarak ne kadar kalori harcayacağını hesapla ve JSON çıktısına 'calories' adında bir alan ekleyerek bu değeri yaz."+
            "Sana vermiş olduğum haraketleri kullandır kullanıcıya mesela 1. gün programında omuz ve gögüs var benim sana vermiş olduğum haraketlerdeki gögüs ve omuz haraketlerinin hepsini kullandır."+tumHaraketler+
                    "bu haraketler üzerinden bana istediğim şeyi ver. Yani kısacası hangi haraket yapılacaksa benim verdiğim haraketlerin hepsini kullansın o bölge ile ilgili olan haraketleri buna dikkat et."+
            "ayrıca ben sana haraketleri isim ve resimId leri ile verdim lütfen kendin değiştirme idleri resimleri çekmekte sıkıntı oluyor sonra benim sana verdiğim fotoğraf id'si ne ise onu ver."+
                "unutma  bölgesel haraketteki bütün haraketleri yaptırmayı."+
                    "!!! dikkat et benim sana verdiğim haraketlerle ilgili hiç bir şeyi değiştirme benim sana verdiğim gibi bir sonuç ver kendin düzeltme ben ne verdiysem sende o şekilde ver düzeltme"+"benim haraketlerim bunlar bunların resim idlerini değiştirme";
        } else {
            Toast.makeText(this, "Hareket listesi boş, program oluşturulamıyor.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject json = new JSONObject();
        try {
            json.put("model", "deepseek/deepseek-chat-v3-0324:free");
            JSONArray messages = new JSONArray();
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", UserPrompt);
            messages.put(userMsg);
            json.put("messages", messages);

        } catch (Exception e) {
            Log.e("JSON_BUILD_ERROR", "JSON isteği oluşturulurken hata: " + e.getMessage());
            runOnUiThread(() -> {
                progressDialog.dismiss();
                Toast.makeText(Profile_page.this, "Program isteği oluşturulurken hata.", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        RequestBody body = RequestBody.create(mediaType, json.toString());
        Request request = new Request.Builder()
                .url("https://openrouter.ai/api/v1/chat/completions")
                .addHeader("Authorization", "Bearer sk-or-v1-eb************")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("AI_API_CALL", "API isteği başarısız oldu: " + e.getMessage());
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(Profile_page.this, "API isteği başarısız: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }

            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "No error body";
                    Log.e("AI_API_RESPONSE", "Başarısız yanıt kodu: " + response.code() + ", Mesaj: " + response.message());
                    Log.e("AI_API_RESPONSE", "Hata Gövdesi: " + errorBody);

                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(Profile_page.this, "AI yanıtı başarısız: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                String res = Objects.requireNonNull(response.body()).string();
                Log.d("AI_Response_Full", "API'dan gelen ham yanıt: " + res);

                runOnUiThread(() -> {
                    try {
                        JSONObject resJson = new JSONObject(res);
                        String content = resJson.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        // Yanıtın JSON kısmını bulmak için temizleme
                        String jsonReply = content.substring(content.indexOf('{'), content.lastIndexOf('}') + 1);
                        Log.d("AI_Response_Parsed", "Temizlenmiş JSON yanıtı: " + jsonReply);

                        JSONObject programJson = new JSONObject(jsonReply);
                        JSONArray weeklyProgramArray = programJson.getJSONArray("weeklyProgram");

                        saveProgramToFirestore(weeklyProgramArray);

                    } catch (Exception e) {
                        Log.e("AI_Parsing_Error", "AI yanıtı işlenirken hata: " + e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(Profile_page.this, "Hata oluştu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void saveProgramToFirestore(JSONArray weeklyProgramArray) {
        WriteBatch batch = db.batch();
        try {
            for (int i = 0; i < 30; i++) {
                JSONObject dayObject = weeklyProgramArray.getJSONObject(i % 7);

                int dayNumber = i + 1;
                String bodyPart = dayObject.getString("bodyPart");
                JSONArray exercisesArray = dayObject.getJSONArray("exercises");

                String dayDocumentPath = "day_" + dayNumber;

                Map<String, Object> dayData = new HashMap<>();
                dayData.put("dayNumber", dayNumber);
                dayData.put("bodyPart", bodyPart);
                dayData.put("createdAt", System.currentTimeMillis());

                DocumentReference dayRef = db.collection("users").document(currenUserID)
                        .collection("workouts").document(dayDocumentPath);
                batch.set(dayRef, dayData);

                for (int j = 0; j < exercisesArray.length(); j++) {
                    JSONObject exerciseObject = exercisesArray.getJSONObject(j);

                    Map<String, Object> exerciseData = new HashMap<>();
                    exerciseData.put("name", exerciseObject.getString("name"));
                    exerciseData.put("setsReps", exerciseObject.getString("setsReps"));
                    exerciseData.put("imageName", exerciseObject.getString("imageName"));

                    // JSON'dan kalori değerini al ve kaydet
                    if (exerciseObject.has("calories")) {
                        exerciseData.put("calories", exerciseObject.getInt("calories"));
                    } else {
                        // Eğer AI kalori değeri döndürmezse, varsayılan bir değer atayabiliriz
                        exerciseData.put("calories", 0);
                    }

                    DocumentReference exerciseRef = dayRef.collection("exercises").document();
                    batch.set(exerciseRef, exerciseData);
                }
            }

            batch.commit()
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(Profile_page.this, "Program başarıyla kaydedildi.", Toast.LENGTH_LONG).show();
                        fetchWeeklyProgramFromFirestore();
                        fetchTodayWorkoutFromFirestore();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Toplu yazma işleminde hata: " + e.getMessage(), e);
                        progressDialog.dismiss();
                        Toast.makeText(Profile_page.this, "Kayıt hatası: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

        } catch (JSONException e) {
            Log.e("Firestore", "JSON ayrıştırma hatası: " + e.getMessage(), e);
            progressDialog.dismiss();
            Toast.makeText(Profile_page.this, "AI yanıtı hatalı formatta.", Toast.LENGTH_LONG).show();
        }
    }

    private void saveToFirebase(String weightSave, String heightSave, String ageSave, String ciniyetSave, String currentUser, String vkiResult1) {
        Map<String, Object> data = new HashMap<>();
        data.put("Weight", weightSave);
        data.put("Height", heightSave);
        data.put("Age", ageSave);
        data.put("Cinsiyet", ciniyetSave);
        data.put("VKI", vkiResult1);

        mDataBase.child("Users")
                .child(currentUser)
                .setValue(data)
                .addOnSuccessListener(aVoid -> Toast.makeText(Profile_page.this, "Veriler kayıt edildi", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(Profile_page.this, "Kayıt işlemi başarısız", Toast.LENGTH_SHORT).show());
    }




    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
