package com.example.toolsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
//AppCompatActivity представляет отдельный экран (страницу) приложения или его визуальный интерфейс
import android.os.Bundle;
import android.os.Handler;

import com.example.toolsapp.R;
import com.example.toolsapp.databinding.ActivityClockBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockActivity extends AppCompatActivity {

    //Handler - это механизм, который позволяет работать с очередью сообщений.
    // Он привязан к конкретному потоку (thread) и работает с его очередью.
    // обработчик потока - обновляет сведения о времени
    private Handler handler;// переменная
    private ActivityClockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handler = new Handler();

        updateClock();
    }

    private void updateClock() {
        //моментально начинаем отсчет времени с задержкой 1 секунда (чтобы часы шли правильно)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Получаем время
                long currentTimeMillis = System.currentTimeMillis();
                // конвертируем время сейчас в переменную Date
                Date currentTime = new Date(currentTimeMillis);

                // Объект для форматирования времени в строку
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String formattedTime = dateFormat.format(currentTime);

                // Обновляем время в TextView
                binding.clockTextView.setText(formattedTime);

                // каждую секунду обновляем время (через 1 сек отсчитываем еще раз)
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {// этот метод (коллбэк) срабатывает, если активность удалена
        super.onDestroy();
        // если активность удаляется, то также убираем всех последующие изменения и публикации
        handler.removeCallbacksAndMessages(null);
    }
}