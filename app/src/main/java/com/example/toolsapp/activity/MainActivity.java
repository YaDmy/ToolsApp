package com.example.toolsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
//AppCompatActivity представляет отдельный экран (страницу) приложения или его визуальный интерфейс
import android.content.Intent;
import android.os.Bundle;

import com.example.toolsapp.databinding.ActivityMainBinding;

import java.time.Clock;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;// переменная binding для взаимодействия с UI из Java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());// привязываем layout к переменной binding
        setContentView(binding.getRoot());

        //устанавливаем слушатели на нажатия кнопок для перехода на соответствующийинструмент
        binding.stopwatch.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, StopwatchActivity.class)));
        binding.calculator.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CalculatorActivity.class)));
        binding.random.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RandomiserActivity.class)));
        binding.clock.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ClockActivity.class)));
    }
}