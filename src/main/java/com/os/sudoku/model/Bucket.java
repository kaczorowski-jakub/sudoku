package com.os.sudoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Bucket {

    private final List<Entry> list = new ArrayList<>();

    void addEntry(Entry e) {
        list.add(e);
    }

    void eliminateNumbers() {
        list.forEach(entry -> {
            if (entry.isEntered()) {
                list.forEach(entryT -> {
                    if (!entryT.isEntered()) {
                        entryT.eraseNumber(entry.getVal());
                    }
                });
            }
        });
    }

    void onlyPosibleChoice() {
        final int size = list.size();
        for (int i = 1; i <= size; i++) {
            final int val = i;
            final long cnt = list.stream().filter(entry -> !entry.isEntered() && entry.isNumberAllowed(val)).count();
            if (cnt == 1) {
                list.stream().filter(entry -> !entry.isEntered() && entry.isNumberAllowed(val)).findAny().get().setNumber(i);
            }

        }
    }

    void rejectMisfits() {
        final int size = list.size();
        final Map<Integer, List<Entry>> counters = new HashMap<>();
        for (int i = 1; i <= size; i++) {
            final int val = i;
            list.forEach(entry -> {
                if (!entry.isEntered() && entry.isNumberAllowed(val)) {
                    if (counters.containsKey(val)) {
                        counters.get(val).add(entry);
                    } else {
                        List<Entry> list = new ArrayList<>();
                        list.add(entry);
                        counters.put(val, list);
                    }
                }
            });
        }
        for (int i = 1; i <= size; i++) {

        }
    }

    boolean validate() {
        for (Entry e1 : list) {
            for (Entry e2 : list) {
                if (e1 != e2 && e1.getVal() == e2.getVal() && e1.getVal() != 0 && e2.getVal() != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    void printBucket() {
        final StringBuilder sb = new StringBuilder();
        list.forEach(entry -> {
            if (sb.length() == 0) {
                sb.append(entry.getVal());
            } else {
                sb.append(", ").append(entry.getVal());
            }
        });
        System.out.println(sb.toString());
    }
}
