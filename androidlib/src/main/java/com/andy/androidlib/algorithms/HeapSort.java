package com.andy.androidlib.algorithms;

/**
 * 堆排序
 * 堆类型：最大堆
 * <p>
 * Created by andy on 18-1-22.
 */

public class HeapSort {

    /**
     * 复杂度：O(lg n)
     */
    private void max_heapify(int[] a, int i) {
        int l = 2 * i;
        int r = 2 * i + 1;

        int largest;
        if (l < a.length && a[l] > a[i]) {
            largest = l;
        } else {
            largest = i;
        }

        if (r < a.length && a[r] > a[largest]) {
            largest = r;
        }

        if (largest != i) {
            int v1 = a[i];
            a[i] = a[largest];
            a[largest] = v1;

            max_heapify(a, largest);
        }
    }

    private void build_max_heap(int[] a) {
        int size = a.length;
        for (int i = size / 2; i > 0; i--) {
            max_heapify(a, i);
        }
    }

    public void heapsort(int[] a) {
        build_max_heap(a);
        int size = a.length - 1;
        for (int i = size; i > 1; i--) {
            int v1 = a[1];
            a[1] = a[i];
            System.out.print(v1);
            System.out.print(" ");
            int[] b = new int[a.length - 1];
            for (int j = 0; j < b.length; j++) {
                b[j] = a[j];
            }
            a = b;
            max_heapify(a, 1);
        }
    }
}
