package org.bitcoin.excel;


import java.util.Date;

public class Line {

    private Date date;
    private double current;

    public Line(Date date, double current) {
        this.date = date;
        this.current = current;
    }

    public Date getDate() {
        return date;
    }

    public double getCurrent() {
        return current;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "Line{" +
                "date=" + date +
                ", current=" + current +
                '}';
    }
}
