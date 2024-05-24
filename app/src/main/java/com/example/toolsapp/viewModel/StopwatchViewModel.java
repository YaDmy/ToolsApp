package com.example.toolsapp.viewModel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// класс ViewModel для активности секундомера
public class StopwatchViewModel extends ViewModel {

    private MutableLiveData<Long> elapsedTime = new MutableLiveData<>();
    // текущее время секундомера, объявлено как
    // объект класса MutableLiveData для Realtime обновлений UI
    private long startTime = 0;// время старта секундомера
    private Handler handler;// объект класса Handler для работы с основным потоком приложения
    // Задача, выполняющаяся для обновления времени секундомера
    private Runnable stopwatchRunnable;// Задача, выполняющаяся для обновления времени секундомера

    public StopwatchViewModel() {
        // Инициализация Handler для работы с основным потоком приложения
        handler = new Handler(Looper.getMainLooper());
        stopwatchRunnable = new Runnable() {
            @Override
            public void run() {
                // Рассчитываем текущее время секундомера
                long currentTime = System.currentTimeMillis();
                // Обновляем LiveData с текущим временем
                elapsedTime.setValue(currentTime - startTime);
                handler.postDelayed(this, 1000);
            }
        };
    }

    // Метод для получения LiveData с текущим временем секундомера
    public LiveData<Long> getElapsedTime() {
        return elapsedTime;
    }

    public void startStopwatch() {

        // Установка времени старта секундомера, если он не запущен.
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        } else {
            // Возобновление с учетом уже измеренного времени, если секундомер был остановлен
            startTime = System.currentTimeMillis() - elapsedTime.getValue();
        }
        // Запуск задачи для обновления времени секундомера
        handler.post(stopwatchRunnable);
    }

    public void stopStopwatch() {
        // Остановка задачи для обновления времени секундомера
        handler.removeCallbacks(stopwatchRunnable);
    }

    // Переопределение метода onCleared для остановки задачи при уничтожении ViewModel
    @Override
    protected void onCleared() {
        super.onCleared();
        // Остановка задачи для обновления времени секундомера
        handler.removeCallbacks(stopwatchRunnable);
    }
}
