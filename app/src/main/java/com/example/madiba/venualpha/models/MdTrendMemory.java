package com.example.madiba.venualpha.models;

import com.example.madiba.venualpha.adapter.trends.trendv2.TrendMemoryCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madiba on 3/16/2017.
 */

public class MdTrendMemory {

    public List<TrendMemoryCell> trendEventCells= new ArrayList<>();

    public MdTrendMemory() {
    }

    public MdTrendMemory(List<TrendMemoryCell> trendEventCells) {
        this.trendEventCells = trendEventCells;
    }

    public List<TrendMemoryCell> getTrendEventCells() {
        return trendEventCells;
    }

    public void setTrendEventCells(List<TrendMemoryCell> trendEventCells) {
        this.trendEventCells = trendEventCells;
    }
}
