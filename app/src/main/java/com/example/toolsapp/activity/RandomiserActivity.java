package com.example.toolsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
//AppCompatActivity представляет отдельный экран (страницу) приложения или его визуальный интерфейс
import android.os.Bundle;

import com.example.toolsapp.R;
import com.example.toolsapp.databinding.ActivityRandomiserBinding;

import java.util.Random;

public class RandomiserActivity extends AppCompatActivity {

    private ActivityRandomiserBinding binding;// объект binding для обращения к UI

    @Override
    //savedInstanceState сохранения состояния активити при изменении конфигурации.
    //onSaveInstanceState() сохраняет данные в объекте Bundle, который реализует
    // ассоциативный массив (хранилище вида ключ-значение).
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRandomiserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//?
        // обработчик нажатия на кнопку генерации рандомного числа
        binding.btnGenerate.setOnClickListener(v -> {
            // получаем границы
            String fromStr = binding.etFrom.getText().toString();
            String toStr = binding.etTo.getText().toString();
            //конвертируем границы, если они не пустые
            int from = ((fromStr.isEmpty()) ? 0 : Integer.parseInt(fromStr));
            int to = (((toStr.isEmpty()) ? 0 : Integer.parseInt(toStr)));

            // Генерация рандомного числа в введенном диапазоне и его отображение в UI
            binding.tvRandomNumber.setText(String.valueOf(new Random().nextInt(to-from) + from));
        });
    }
}