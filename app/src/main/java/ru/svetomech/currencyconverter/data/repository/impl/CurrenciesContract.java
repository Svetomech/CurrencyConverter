package ru.svetomech.currencyconverter.data.repository.impl;

import android.provider.BaseColumns;

public final class CurrenciesContract {
    private CurrenciesContract() {}

    public static class CurrenciesEntry implements BaseColumns {
        public static final String TABLE_NAME = "currency";
        public static final String COLUMN_NAME_NUM_CODE = "numCode";
        public static final String COLUMN_NAME_CHAR_CODE = "charCode";
        public static final String COLUMN_NAME_NOMINAL = "nominal";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_VALUE = "value";
    }
}
