package com.example.fitmybody;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // Button importu zaten vardı
import android.widget.CheckBox;
import android.widget.CompoundButton; // CompoundButton için import eklendi
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Gerekli olmayan importları kaldırdım veya yorum satırı yaptım
// import com.google.firebase.StartupTime; // Genellikle bu dosyada kullanılmaz
// import java.util.PrimitiveIterator; // Genellikle bu dosyada kullanılmaz

public class AppLoginPage extends AppCompatActivity {

    private EditText userName, userPassword;
    private FirebaseAuth mAuth;
    private CheckBox rememberMeCheckBox; // CheckBox değişkeni


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_login_page);

        // UI elemanlarını initial ettik
        userName = findViewById(R.id.edit_text_username);
        userPassword = findViewById(R.id.edit_text_password);
        rememberMeCheckBox = findViewById(R.id.checkbox_remember_me); // CheckBox'ı initial ettik


        // Firebase Authentication instance'ını al
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "Hoş Geldiniz, " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AppLoginPage.this, MainActivity.class));
            finish(); // Giriş sayfasını kapat
            return; // Metodun geri kalanını çalıştırma
        }

        rememberMeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(AppLoginPage.this, "Beni Hatırla: Oturum açık kalacak.", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(AppLoginPage.this, "Beni Hatırla: Oturum kapatıldığında tekrar giriş gerekecek.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Kayıt sayfasına geçiş kodu
        findViewById(R.id.CreateAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppLoginPage.this, RegisterPage.class));
            }
        });

        // Giriş butonuna tıklama dinleyicisi
        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMail = userName.getText().toString().trim(); // Boşlukları temizle
                String userpass = userPassword.getText().toString().trim(); // Boşlukları temizle

                if (userMail.isEmpty() || userpass.isEmpty()) {
                    Toast.makeText(AppLoginPage.this, "Alanlar boş bırakılamaz", Toast.LENGTH_SHORT).show();
                } else {
                    LoginToApp(userMail, userpass);
                }
            }

            private void LoginToApp(String userMail, String userpass) {
                mAuth.signInWithEmailAndPassword(userMail, userpass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser loggedInUser = mAuth.getCurrentUser(); // Başarılı girişten sonra güncel kullanıcıyı al
                                    if (loggedInUser != null) {
                                        Toast.makeText(AppLoginPage.this, "Giriş başarılı", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(AppLoginPage.this, MainActivity.class));
                                        finish(); // Giriş sayfasını kapat
                                    } else {
                                        Toast.makeText(AppLoginPage.this, "Giriş başarılı ancak kullanıcı bilgisi alınamadı.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Giriş başarısız olduğunda Firebase'den gelen hata mesajını göster
                                    String errorMessage = "Giriş başarısız.";
                                    if (task.getException() != null) {
                                        errorMessage += ": " + task.getException().getMessage();
                                    }
                                    Toast.makeText(AppLoginPage.this, errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Ek bir hata dinleyicisi, genellikle onComplete içinde ele alınır
                                // Bu kısım da hata mesajı gösterebilir, ancak genellikle yukarıdaki onComplete yeterlidir.
                                Toast.makeText(AppLoginPage.this, "Giriş işlemi başarısız oldu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}