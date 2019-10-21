/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher;

/**
 *
 * @author Simon
 */
public class Time {

    private final int hour;
    private final int minute;
    private final int second;
    private final int millisecond;

    public Time(int h, int m, int s, int ms) {
        hour = h;
        minute = m;
        second = s;
        millisecond = ms;
    }

    @Override
    public String toString() {
        return "Time: " + hour + ":" + minute + ":" + second + ":" + millisecond;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
        // TODO
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getMillisecond() {
        return millisecond;
    }

    public int[] getTime() {
        int time[] = {hour,minute,second,millisecond};
        return time;
    }
}
