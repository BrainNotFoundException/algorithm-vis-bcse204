package utils;

public class QuickSortPanel extends SortingPanel {

    public QuickSortPanel(int[] arr) {
        super(arr);
    }

    private void quickSort(int l, int h) {
        if (l < h) {
            int p = partition(l, h);
            quickSort(l, p - 1);
            quickSort(p + 1, h);
        }
    }

    private int partition(int l, int h) {

        int pivot = arr[h];
        int i = l - 1;

        for (int j = l; j < h; j++) {

            if (arr[j] < pivot) {
                i++;

                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;

                pause();
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[h];
        arr[h] = temp;

        pause();

        return i + 1;
    }

    @Override
    protected void sort() {
        quickSort(0, arr.length - 1);
    }

}
