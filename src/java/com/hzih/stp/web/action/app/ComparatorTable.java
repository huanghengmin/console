package com.hzih.stp.web.action.app;

import com.inetec.common.config.stp.nodes.Table;

import java.util.Comparator;

public class ComparatorTable implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        Table table1 = (Table)o1;
        Table table2 = (Table)o2;
        return table1.getSeqNumber()-table2.getSeqNumber();

    }
}