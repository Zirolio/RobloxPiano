package com.zirolio.robloxpiano;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TabIterator implements Iterator<String> {
    private String str;

    public TabIterator(String str) {
        this.str = str;
    }

    @Override
    public boolean hasNext() {
        return !this.str.isEmpty();
    }

    @Override
    public String next() {
        if (!hasNext()) throw new NoSuchElementException();

        String nextItem;

        if (this.str.startsWith("--")) { // --
            nextItem = "--";
            this.str = this.str.substring(2);
        } else if (this.str.startsWith("-")) { // -
            nextItem = "-";
            this.str = this.str.substring(1);
        } else if (this.str.startsWith("[")) { // [asd]
            int endIndex = this.str.indexOf(']');
            nextItem = this.str.substring(0, endIndex + 1);
            this.str = this.str.substring(endIndex + 1);
        } else if (this.str.startsWith("{")) { // {asd}
            int endIndex = this.str.indexOf('}');
            nextItem = this.str.substring(0, endIndex + 1);
            this.str = this.str.substring(endIndex + 1);
        } else {  // a
            nextItem = this.str.substring(0, 1);
            this.str = this.str.substring(1);
        }

        str = this.str.strip(); // Rm spaces

        return nextItem;
    }
}
/*public class TabIterator implements Iterator<String> {
    private String str;

    public TabIterator(String str) {
        this.str = str;
    }

    @Override
    public boolean hasNext() {
        return !this.str.isEmpty();
    }

    @Override
    public String next() {
        if (!hasNext()) throw new NoSuchElementException();

        String nextItem;

        if (this.str.startsWith("[")) {
            int endIndex = this.str.indexOf(']');
            if (endIndex == -1) {
                nextItem = str;
                str = "";
            } else {
                nextItem = this.str.substring(0, endIndex + 1);
                str = this.str.substring(endIndex + 1);
            }
        } else {
            nextItem = this.str.substring(0, 1);
            str = this.str.substring(1);
        }

        if (!this.str.isEmpty() && this.str.charAt(0) == '|') {
            nextItem += "|";
            str = this.str.substring(1);
        }

        str = this.str.stripLeading();

        return nextItem;
    }
}*/