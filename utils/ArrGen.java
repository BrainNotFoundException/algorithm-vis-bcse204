package utils;

import java.util.Random;

public interface ArrGen {

    public static int[] generateArray(int size, int min, int max) {
        int[] arr = new int[size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(max - min) + min + 1;
        }

        return arr;
    }
}
