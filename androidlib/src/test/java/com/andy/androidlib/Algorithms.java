package com.andy.androidlib;

import com.andy.androidlib.algorithms.HeapSort;

import org.junit.Test;

/**
 * 算法测试
 * <p>
 * Created by andy on 18-1-22.
 */

public class Algorithms {

    @Test
    public void heap_sort() {
        HeapSort mHeapSort = new HeapSort();
        int[] a = {45, 85, 12, 44, 587, 542, 158, -45, 2, -65, 41, 75, 62, 84, 4895};
        mHeapSort.heapsort(a);
    }
}
