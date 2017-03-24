package com.example.madiba.venualpha.models;

import com.example.madiba.venualpha.adapter.trends.trendv2.TrendEventCell;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendMemoryCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madiba on 3/16/2017.
 */

public class MdTrendEvents {

    List<TrendEventCell> trendEventCells= new ArrayList<>();
    public List<TrendMemoryCell> trendMemoryCells= new ArrayList<>();


    public MdTrendEvents(List<TrendEventCell> trendEventCells) {
        this.trendEventCells = trendEventCells;
    }

    public List<TrendMemoryCell> getTrendMemoryCells() {
        return trendMemoryCells;
    }

    public void setTrendMemoryCells(List<TrendMemoryCell> trendMemoryCells) {
        this.trendMemoryCells = trendMemoryCells;
    }

    public List<TrendEventCell> getTrendEventCells() {
        return trendEventCells;
    }

    public void setTrendEventCells(List<TrendEventCell> trendEventCells) {
        this.trendEventCells = trendEventCells;
    }
}
