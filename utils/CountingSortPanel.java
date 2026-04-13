package utils;

public class CountingSortPanel extends SortingPanel {

    public CountingSortPanel(int[] arr) {
        super(arr);
    }

    @Override
    protected void sort() {
        int max = arr[0];
        for (int v : arr) {
            if (v > max) {
                max = v;
            }
        }

        int[] count = new int[max + 1];
        for (int v : arr) {
            count[v]++;
        }

        int idx = 0;
        for (int i = 0; i <= max; i++) {
            while (count[i]-- > 0) {
                arr[idx++] = i;
                pause();
            }
        }
    }
}
