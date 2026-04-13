package utils;

public class ShellSortPanel extends SortingPanel {

    public ShellSortPanel(int[] arr) {
        super(arr);
    }

    @Override
    protected void sort() {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                    pause();
                }
                arr[j] = temp;
                pause();
            }
        }
    }
}
