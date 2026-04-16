package ru.job4j.gc.prof;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;
import java.util.Scanner;

public class Menu {

    public static final int CREATE_ARRAY = 1;
    public static final int MERGE_SORT = 2;
    public static final int BUBBLE_SORT = 3;
    public static final int INSERT_SORT = 4;

    public static final String BEGIN = " Начало сортировки - ";

    public static final String END = " Конец сортировки - ";

    public static final String DURATION = " Продолжительность сортировки - ";

    public static final String SELECT = "Введите количество элементов";

    public static final String ARRAY_INFO = "Массив создан и заполнен случайными значениями от 1 до ";

    public static final String MENU = """
                Введите 1 для создание массива.
                Введите 2, чтобы начать сортировку слиянием.
                Введите 3, чтобы начать сортировку пузырьком.
                Введите 4,чтобы начать сортировку вставками.
                Введите любое другое число для выхода.
            """;

    public static void main(String[] args) {
        Data store = new RandomArray(new Random());
        Sort merge = new MergeSort();
        Sort bubble = new BubbleSort();
        Sort insert = new InsertSort();
        Scanner scanner = new Scanner(System.in);
        boolean run = true;
        while (run) {
            System.out.println(MENU);
            int userChoice = Integer.parseInt(scanner.nextLine());
            System.out.println(userChoice);
            if (CREATE_ARRAY == userChoice) {
                System.out.println(SELECT);
                int size = Integer.parseInt(scanner.nextLine());
                store.insert(size);
                System.out.println(ARRAY_INFO + size);
            } else if (MERGE_SORT == userChoice) {
                var start = LocalTime.now();
                System.out.printf("%s%s%s%n", merge.getClass().getSimpleName(), BEGIN, start);
                merge.sort(store);
                var end = LocalTime.now();
                System.out.printf("%s%s%s%n", merge.getClass().getSimpleName(), END, end);
                System.out.printf("%s%s%s секунд%n", merge.getClass().getSimpleName(), DURATION, Duration.between(start, end).toSeconds());
            } else if (BUBBLE_SORT == userChoice) {
                var start = LocalTime.now();
                System.out.printf("%s%s%s%n", bubble.getClass().getSimpleName(), BEGIN, LocalTime.now());
                bubble.sort(store);
                var end = LocalTime.now();
                System.out.printf("%s%s%s%n", bubble.getClass().getSimpleName(), END, LocalTime.now());
                System.out.printf("%s%s%s секунд%n", bubble.getClass().getSimpleName(), DURATION, Duration.between(start, end).toSeconds());
            } else if (INSERT_SORT == userChoice) {
                var start = LocalTime.now();
                System.out.printf("%s%s%s%n", insert.getClass().getSimpleName(), BEGIN, LocalTime.now());
                insert.sort(store);
                var end = LocalTime.now();
                System.out.printf("%s%s%s%n", insert.getClass().getSimpleName(), END, LocalTime.now());
                System.out.printf("%s%s%s секунд%n", insert.getClass().getSimpleName(), DURATION, Duration.between(start, end).toSeconds());
            } else {
                run = false;
                System.out.println("Конец работы");
            }
        }
    }
}