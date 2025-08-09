package com.example.fitmybody;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView; // ImageView import'unu ekledik
import android.widget.Toast;
import de.hdodenhof.circleimageview.CircleImageView; // CircleImageView import'unu ekledik

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    private EditText userMail, userPassword, userName;
    private Button registerButton;
    private CircleImageView register_Photo;

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private FirebaseStorage storage;

    private Uri selectedImageUri; // Seçilen fotoğrafın URI'sını tutacak değişken

    // Galeriye erişmek için ActivityResultLauncher
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private final int UCROP_REQUEST_CODE = UCrop.REQUEST_CROP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);

        // UI elemanlarını initial ettik
        userMail = findViewById(R.id.create_email);
        userPassword = findViewById(R.id.create_password);
        userName = findViewById(R.id.create_name);
        registerButton = findViewById(R.id.button_register);
        register_Photo = findViewById(R.id.register_photo);

        // Firebase kodlarını initial ettik
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        // Resim seçimi ve kırpma sonrası için ActivityResultLauncher'ı başlatma
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            startCrop(imageUri);
                        }
                    }
                }
        );

        // register_Photo (ImageView) tıklandığında galeriye git
        register_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Kullanıcı hesabı varsa direkt giriş sayfasına geçiş
        findViewById(R.id.haveAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterPage.this, AppLoginPage.class));
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerMail = userMail.getText().toString().trim();
                String registerPassword = userPassword.getText().toString().trim();
                String registerName = userName.getText().toString().trim();

                if (registerMail.isEmpty() || registerPassword.isEmpty() || registerName.isEmpty()) {
                    Toast.makeText(RegisterPage.this, "Boş alan bırakmayınız", Toast.LENGTH_SHORT).show();
                } else if (registerPassword.length() < 6) {
                    Toast.makeText(RegisterPage.this, "Şifre 6 karakterden az olamaz", Toast.LENGTH_SHORT).show();
                } else {
                    CreateAccountToFireBase(registerMail, registerPassword, registerName);
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        pickImageLauncher.launch(galleryIntent);
    }

    private void startCrop(Uri uri) {
        String destinationFileName = "cropped_image.jpg";
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), destinationFileName));

        UCrop uCrop = UCrop.of(uri, destinationUri);

        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true); // Daire şeklinde kırpma katmanını etkinleştirir.
        options.setCropFrameColor(getResources().getColor(R.color.white)); // Kırpma çerçevesinin rengini ayarlar.
        options.setCropGridColor(getResources().getColor(R.color.white)); // Kırpma gridinin rengini ayarlar.
        options.setFreeStyleCropEnabled(false); // Serbest kırpmayı devre dışı bırakır.

        uCrop.withOptions(options);
        uCrop.withAspectRatio(1, 1); // En-boy oranını 1:1 olarak zorunlu kılar (daire için ideal).

        uCrop.start(this, UCROP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCROP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                selectedImageUri = resultUri;
                register_Photo.setImageURI(selectedImageUri); // Kırpılan fotoğrafı ImageView'a set et
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, "Fotoğraf kırpma hatası: " + cropError.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void CreateAccountToFireBase(String registerMail, String registerPassword, String registerName) {
        mAuth.createUserWithEmailAndPassword(registerMail, registerPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                String userEmail = currentUser.getEmail();

                                Toast.makeText(RegisterPage.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();

                                if (selectedImageUri != null) {
                                    uploadImageToFirebaseStorage(userId, userEmail, registerName, selectedImageUri);
                                } else {
                                    saveUserDataToRealtimeDatabase(userId, userEmail, registerName, null);
                                }
                            } else {
                                Toast.makeText(RegisterPage.this, "Kayıt başarılı ancak kullanıcı verisi alınamadı.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterPage.this, "Kayıt oluşturulamadı: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadImageToFirebaseStorage(String userId, String userEmail, String userName, Uri imageUri) {
        StorageReference imageRef = storage.getReference("profile_images/" + userId + "/" + "profile.jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                String photoUrl = downloadUri.toString();
                                saveUserDataToRealtimeDatabase(userId, userEmail, userName, photoUrl);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterPage.this, "Fotoğraf URL'si alınamadı: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                saveUserDataToRealtimeDatabase(userId, userEmail, userName, null);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterPage.this, "Fotoğraf yüklenemedi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        saveUserDataToRealtimeDatabase(userId, userEmail, userName, null);
                    }
                });
    }

    private void saveUserDataToRealtimeDatabase(String userId, String userEmail, String userName, String photoUrl) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", userEmail);
        userData.put("name", userName);

        // Hata giderildi: selectedImageUri yerine photoUrl kullanılıyor
        if (photoUrl != null) {
            userData.put("profileImageUrl", photoUrl);
        }

        mDataBase.child("User")
                .child(userId)
                .setValue(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegisterPage.this, "Realtime'a kullanıcı verileri ve fotoğraf bilgisi kayıt edildi", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterPage.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterPage.this, "Realtime'a kullanıcı verileri Kayıt edilemedi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
