package com.example.fitmybody; // Paket adını kendi projenin paket adıyla değiştir

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.app.AlarmManager;
import android.app.PendingIntent;
import java.util.Calendar;

public class MidnightResetReceiver extends BroadcastReceiver {

    private static final String PREFS_NAME = "MyChronometerPrefs";
    private static final String KEY_TIME_SWAP_BUFF = "timeSwapBuff";
    private static final String KEY_IS_RUNNING = "isRunning";
    private static final String KEY_LAST_START_TIME = "lastStartTime";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Cihaz yeniden başlatıldığında tetiklenirse
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("MidnightReset", "Cihaz yeniden başlatıldı, gece yarısı sıfırlama yeniden planlanıyor.");
            scheduleMidnightReset(context); // Alarmı yeniden planla
        } else {
            // Gece yarısı alarmı tetiklendiğinde
            Log.d("MidnightReset", "Gece yarısı sıfırlama alarmı alındı!");

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Tüm kronometre ile ilgili tercihleri sıfırla
            editor.putLong(KEY_TIME_SWAP_BUFF, 0L);
            editor.putBoolean(KEY_IS_RUNNING, false);
            editor.remove(KEY_LAST_START_TIME); // En son başlama zamanını da temizle
            editor.apply();

            // Opsiyonel: Kullanıcıya sıfırlamanın yapıldığını bildiren bir bildirim gösterebilirsin.
        }
    }

    // Gece yarısı alarmını yeniden planlayan metot (cihaz yeniden başlatıldığında kullanılır)
    private void scheduleMidnightReset(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MidnightResetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Alarmı bir sonraki gece yarısı (00:00:00) için ayarla
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis()); // Şu anki zamanı al

        // Şu anki zamana bir gün ekle, böylece yarının tarihine geçmiş oluruz
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        // Saati, dakikayı, saniyeyi ve milisaniyeyi 00:00:00'a ayarla
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
        Log.d("MidnightReset", "Gece yarısı sıfırlama, " + calendar.getTime().toString() + " için yeniden planlandı.");
    }
}