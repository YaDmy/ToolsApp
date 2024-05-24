package com.example.toolsapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//AppCompatActivity представляет отдельный экран (страницу) приложения или его визуальный интерфейс

import androidx.appcompat.app.AppCompatActivity;

import com.example.toolsapp.R;
import com.example.toolsapp.databinding.ActivityCalculatorBinding;

public class CalculatorActivity extends AppCompatActivity {

    private StringBuilder input = new StringBuilder();// объект StringBuilder нужен для сложения символов и строк в новую строку
    // View Binding - библиотека предоставляет решение для связывания xml и кода.
    private ActivityCalculatorBinding binding;//объект binding класса ViewBinding для обращения к элементам UI xml из Java класса

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Метод который умеет из содержимого layout-файла создать View-элемента называется inflate
        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());//инициализация binding объекта, привязка UI к логике
        setContentView(binding.getRoot());

        // устанавливаем слушители на кнопки цифр
        //при клике на кнопку прибавляем соответствующую цифру к строке, которую потом будем рассчитывать
        binding.btn0.setOnClickListener(v -> appendInput("0"));
        binding.btn1.setOnClickListener(v -> appendInput("1"));
        binding.btn2.setOnClickListener(v -> appendInput("2"));
        binding.btn3.setOnClickListener(v -> appendInput("3"));
        binding.btn4.setOnClickListener(v -> appendInput("4"));
        binding.btn5.setOnClickListener(v -> appendInput("5"));
        binding.btn6.setOnClickListener(v -> appendInput("6"));
        binding.btn7.setOnClickListener(v -> appendInput("7"));
        binding.btn8.setOnClickListener(v -> appendInput("8"));
        binding.btn9.setOnClickListener(v -> appendInput("9"));

        // устанавливаем слушатели на кнопки операций
        binding.btnAdd.setOnClickListener(v -> appendInput("+"));
        binding.btnSubtract.setOnClickListener(v -> appendInput("-"));
        binding.btnMultiply.setOnClickListener(v -> appendInput("*"));
        binding.btnDivide.setOnClickListener(v -> appendInput("/"));
        binding.btnDecimal.setOnClickListener(v -> appendInput("."));

        // кнопка очистки
        binding.clear.setOnClickListener(v -> {
            input.setLength(0);
            updateResult();
        });

        // кнопка =
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculateResult());
    }

    // Добавляет символ или число в строку ввода и обновляет отображаемый результат.
    private void appendInput(String value) {
        input.append(value);//к общей строке прибавляем новый символ
        // (это может быть цифра или действие, которое нужно выполнить)
        updateResult();// высвечиваем обновленную строку на экран, вызывая функцию updateResult()
    }

    // Обновляет текстовое поле калькулятора текущим значением ввода.
    private void updateResult() {
        binding.etCalc.setText(input.toString());
    }

    // Вычисляет результат выражения, обрабатывает исключения и обновляет интерфейс.
    private void calculateResult() {
        //try catch блок нужен для отлова возможных ошибок во время работы приложения
        try {
            double result = evaluate(input.toString());// Вычисляет результат выражения
            binding.etCalc.setText(String.valueOf(result));// Выводит результат на экран
            input.setLength(0);// Очищает строку ввода
            input.append(result);// Добавляет результат в строку ввода
        } catch (Exception e) {
            binding.etCalc.setText("Ошибка");// Выводит сообщение об ошибке на экран
            input.setLength(0);// Очищает строку ввода
        }
    }

    // Разбирает и вычисляет выражение, введенное пользователем.
    private double evaluate(String expression) {
        return new Object() {
            int pos = -1, ch;// Инициализируем переменные 'pos' и 'ch'

            // Перемещает указатель на следующий символ в выражении.
            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            // Проверяет и потребляет ожидаемый символ, если он совпадает с текущим.
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();// Пропускает пробелы
                if (ch == charToEat) {// Сравнивает текущий символ с ожидаемым
                    nextChar(); // Перемещает указатель
                    return true;
                }
                return false;
            }

            // Входная точка для разбора выражения.
            double parse() {
                nextChar();// Перемещает указатель к первому символу
                double x = parseExpression();// Разбирает выражение
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                // Проверяет наличие неразобранных символов
                return x;
            }

            // Обрабатывает сложение и вычитание.
            double parseExpression() {
                double x = parseTerm(); // Разбирает блок
                for (;;) {// Бесконечный цикл
                    if (eat('+')) x += parseTerm(); // Обрабатывает сложение.
                    else if (eat('-')) x -= parseTerm(); // Обрабатывает вычитание.
                    else return x;// Возвращает результат, если не найдено операторов
                }
            }

            // Обрабатывает умножение и деление.
            double parseTerm() {
                double x = parseFactor();
                for (;;) {// Бесконечный цикл
                    if (eat('*')) x *= parseFactor(); // Обрабатывает умножение.
                    else if (eat('/')) x /= parseFactor(); // Обрабатывает деление.
                    else return x;// Возвращает результат, если не найдено операторов
                }
            }

            // Разбирает факторы, числа, и обрабатывает унарные операции.
            // Парсит числа вместе со знаком - перед ними
            double parseFactor() {
                if (eat('+')) return parseFactor(); // Унарный плюс.
                if (eat('-')) return -parseFactor(); // Унарный минус.

                double x;
                int startPos = this.pos;// Сохраняет начальную позицию фактора
                if (eat('(')) { // Обработка скобок.
                    x = parseExpression(); // Разбирает вложенное выражение
                    eat(')'); // Ожидает закрывающую скобку
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // Чтение числа.
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();// Читает все цифры и запятые
                    x = Double.parseDouble(expression.substring(startPos, this.pos));// Преобразует строку числа в double
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);// Выдает исключение при встрече неожиданного символа
                }

                return x;
            }
        }.parse(); // Запускает процесс разбора выражения, вызывая parse().
    }
}
