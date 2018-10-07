package com.chinjiaxiong.hongleongchan;

import android.support.annotation.NonNull;

public class AccuracyQueue implements Comparable<AccuracyQueue> {

    int index;
    double result;

    public AccuracyQueue(int index, double result){
        this.index = index;
        this.result = result;
    }

    @Override
    public int compareTo(@NonNull AccuracyQueue accuracyQueue) {
        if(this.result < accuracyQueue.result) return 1;
        else if(this.result == accuracyQueue.result) return 0;
        return -1;
    }
}
