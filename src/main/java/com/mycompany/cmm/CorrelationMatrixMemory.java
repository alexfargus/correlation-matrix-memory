/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cmm;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 *
 * @author alex
 */
public class CorrelationMatrixMemory {

    private final Integer nRows;
    private final Integer nColumns;

    private final Map<Integer, Row> rows;

    private CorrelationMatrixMemory(Integer nRows, Integer nColumns) {
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.rows = new HashMap<>(nRows);
    }

    private static class Row extends TreeSet<Integer> {

        static final Row EMPTY = new Row();

        public Row() {
        }

    }

    private class Index {

        final Integer row;
        final Integer column;

        public Index(Integer row, Integer column) {
            this.row = row;
            this.column = column;
        }

    }

    public CorrelationMatrixMemory store(Token input, Token output) {
        if (input.getLength() >= nRows) {
            final String message = String.format("Input token is longer than column length (%d)", nRows);
            throw new IndexOutOfBoundsException(message);
        } else if (output.getLength() >= nColumns) {
            final String message = String.format("Output token is longer than row width (%d)", nColumns);
            throw new IndexOutOfBoundsException(message);
        }

        input.keySet().stream()
                .flatMap(row -> output.keySet()
                .stream()
                .map(column -> new Index(row, column)))
                .forEach(this::set);

        return this;
    }

    private void set(Index index) {
        rows.computeIfAbsent(index.row, r -> new Row()).add(index.column);
    }

    private Stream<Entry<Integer, Integer>> getRowEntries(Integer rowIndex, Integer weight) {
        final Row row = rows.get(rowIndex);
        if (row == null) {
            return Stream.empty();
        } else {
            return row.stream()
                    .map(columnIndex -> new AbstractMap.SimpleImmutableEntry<>(columnIndex, weight));
        }
    }

    public Token recall(Token query) {
        if (query.getLength() >= nRows) {
            final String message = String.format("Query token is longer than column length (%d)", nRows);
            throw new IndexOutOfBoundsException(message);
        }
        return query.entrySet().stream()
                .flatMap(entry -> getRowEntries(entry.getKey(), entry.getValue()))
                .collect(new TokenCollector(nColumns));
    }

}
