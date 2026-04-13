package utils;

public class RadixSortPanel extends SortingPanel {

    public RadixSortPanel(int[] arr) {
        super(arr);
    }

    private void countingSort(int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];

        for (int v : arr) {
            count[(v / exp) % 10]++;
        }
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        for (int i = n - 1; i >= 0; i--) {
            output[--count[(arr[i] / exp) % 10]] = arr[i];
        }
        for (int i = 0; i < n; i++) {
            arr[i] = output[i];
            pause();
        }
    }

    @Override
    protected void sort() {
        int max = arr[0];
        for (int v : arr) {
            if (v > max) {
                max = v;
            }
        }
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(exp);
        }
    }
}
