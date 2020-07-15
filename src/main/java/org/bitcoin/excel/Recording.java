package org.bitcoin.excel;

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
        return "Recording{" +
                "start=" + start +
                ", end=" + end +
                ", gap=" + gap +
                ", date_diff=" + date_diff +
                '}';
    }
}
