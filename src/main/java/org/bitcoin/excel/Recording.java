package org.bitcoin.excel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Recording {

    Line start;
    Line end;
    double gap;
    long date_diff;

    public Recording(Line start, Line end, double gap, long date_diff) {
        this.start = start;
        this.end = end;
        this.gap = gap;
        this.date_diff = date_diff;
    }

    public Line getStart() {
        return start;
    }

    public void setStart(Line start) {
        this.start = start;
    }

    public Line getEnd() {
        return end;
    }

    public void setEnd(Line end) {
        this.end = end;
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
    }

    public long getDate_diff() {
        return date_diff;
    }

    public void setDate_diff(long date_diff) {
        this.date_diff = date_diff;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###.##");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy H:m");
        String dateStringStart = format.format(start.getDate());
        String dateStringEnd = format.format(end.getDate());
        String message = "Hausse de " + df.format(abs(gap)) + ", entre le " + dateStringStart + " et le " + dateStringEnd;
        return message;
    }

    public String toStringBaisse() {
        DecimalFormat df = new DecimalFormat("###.##");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy H:m");
        String dateStringStart = format.format(start.getDate());
        String dateStringEnd = format.format(end.getDate());
        String message = "Baisse de " + df.format(abs(gap)) + ", entre le " + dateStringStart + " et le " + dateStringEnd;
        return message;
    }

    private Double abs(double gap) {
        if(gap < 0)
            return gap * -1;
        return gap;
    }
}
