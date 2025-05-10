package com.delorenzo.Cinema.utils;

import com.delorenzo.Cinema.entity.Screening;

import java.util.Comparator;

public class ScreeningComparator implements Comparator<Screening> {
    public int compare(Screening s1, Screening s2) {
        if(s1.getMovie().getValue() < s2.getMovie().getValue()) return -1;
        if(s1.getMovie().getValue() > s2.getMovie().getValue()) return 1;
        return 0;
    }
}
