package com.example.toolsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
//AppCompatActivity представляет отдельный экран (страницу)
// приложения или его визуальный интерфейс
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.toolsapp.R;
import com.example.toolsapp.viewModel.StopwatchViewModel;
import com.example.toolsapp.databinding.ActivityStopwatchBinding;

public class StopwatchActivity extends AppCompatActivity {

    private ActivityStopwatchBinding binding;// объект binding для обращения к UI
    private StopwatchViewModel viewModel;// создание объекта класса ViewModel, который хранит данные для активности
    private boolean isStopped = true; // переменная, указывающая на то, остановлен ли секундомер

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStopwatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // инициализируем объект вью модели
        viewModel = new ViewModelProvider(this).get(StopwatchViewModel.class);
        //устанавливаем слушатель в реальном времени на изменение счетчика времени (изменения происходят в классе ViewModel)
        viewModel.getElapsedTime().observe(this, elapsedTime -> {
            long seconds = elapsedTime / 1000;// рассчитываем секунды
            //long milliseconds = elapsedTime % 1000;
            binding.tvLiveStopwatch.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
        });
        // обработчик нажатия на кнопку остановки/включения секундомера
        binding.resumeBtn.setOnClickListener(v -> {
            if (isStopped) {
                // если секундомер остановлен, то включаем его
                viewModel.startStopwatch();
                binding.resumeBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pause_icon));
                isStopped = false;// сохраняем статус секундомера
            }else {
                // останавливаем секундомер, если он включен
                viewModel.stopStopwatch();
                binding.resumeBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play_icon));
                isStopped = true;// сохраняем статус секундомера
            }
        });
    }


}