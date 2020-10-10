package com.grafana.plugin.pojo;
import java.util.Date;

public class Range {

    private Date from;
    private Date to;
    public void setFrom(Date from) {
         this.from = from;
     }
     public Date getFrom() {
         return from;
     }

    public void setTo(Date to) {
         this.to = to;
     }
     public Date getTo() {
         return to;
     }

}