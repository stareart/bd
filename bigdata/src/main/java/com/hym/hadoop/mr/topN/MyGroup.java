package com.hym.hadoop.mr.topN;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class MyGroup extends WritableComparator {

    DateUtils dateUtils = new DateUtils();

    public MyGroup() {
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        //userid相同，且同一月的分成一组
        OrderBean aOrderBean = (OrderBean)a;
        OrderBean bOrderBean = (OrderBean)b;

        String aUserId = aOrderBean.getUserid();
        String bUserId = bOrderBean.getUserid();

        //userid、年、月相同的，作为一组
        int ret1 = aUserId.compareTo(bUserId);
        if(ret1 == 0) {
            return aOrderBean.getDatetime().compareTo(bOrderBean.getDatetime());
        } else {
            return ret1;
        }
    }
}