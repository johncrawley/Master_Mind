package com.jcrawley.codebreaker.view;

import android.view.View;
import android.view.ViewGroup;

import com.jcrawley.codebreaker.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridMap {

    private final Map<Integer, ViewGroup> gridRowMap = new HashMap<>();


    public void init(View parent){
        gridRowMap.clear();
        setupGridSection(parent, R.id.gameGridLayout, 9);
        setupGridSection(parent, R.id.gameGridLayout2, 4);
    }


    public int size(){
        return gridRowMap.size();
    }


    private void setupGridSection(View parentView, int parentLayoutId, int rowNumber){
        var rowIds = List.of(R.id.row_a, R.id.row_b, R.id.row_c, R.id.row_d, R.id.row_e);
        ViewGroup sectionLayout = parentView.findViewById(parentLayoutId);
        for(int i = 0; i < rowIds.size(); i++, rowNumber--){
            addRowToMap(rowNumber, sectionLayout, rowIds.get(i));
        }
    }


    private void addRowToMap(int number, ViewGroup parent, int rowId){
        ViewGroup row = parent.findViewById(rowId);
        gridRowMap.put(number, row);
    }

    public ViewGroup getRow(int index) {
        return gridRowMap.get(index);
    }




    public ViewGroup getPegRow(int index) {
        return (ViewGroup) getRow(index).getChildAt(0);
    }


    public ViewGroup getCluesRow(int index) {
        return (ViewGroup) getRow(index).getChildAt(1);
    }
}
