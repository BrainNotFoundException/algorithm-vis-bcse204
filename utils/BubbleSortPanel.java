package utils;

public class BubbleSortPanel extends SortingPanel {

    public BubbleSortPanel(int[] arr) {
        super(arr);
    }

    @Override
    protected void sort() {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    pause();
                }

            }
        }
    }
}
