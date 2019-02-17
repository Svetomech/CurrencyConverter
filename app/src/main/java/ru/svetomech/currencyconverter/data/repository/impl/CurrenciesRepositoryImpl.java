package ru.svetomech.currencyconverter.data.repository.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.svetomech.currencyconverter.data.models.parser.XmlCurrenciesParser;
import ru.svetomech.currencyconverter.data.models.response.XmlCurrenciesResponse;
import ru.svetomech.currencyconverter.data.repository.ICurrenciesRepository;

import static ru.svetomech.currencyconverter.ui.shared.Network.downloadUrl;
import static ru.svetomech.currencyconverter.ui.shared.Network.isNetworkAvailable;

public class CurrenciesRepositoryImpl implements ICurrenciesRepository {

    private CurrenciesDbHelper mCurrenciesDbHelper;
    private SQLiteDatabase mCurrenciesDb;
    private SQLiteDatabase mCurrenciesDbRead;
    private List<XmlCurrenciesResponse> mCurrencies;

    public CurrenciesRepositoryImpl(Context context) {
        mCurrenciesDbHelper = new CurrenciesDbHelper(context);
        mCurrencies = new ArrayList<>();
    }

    public List<XmlCurrenciesResponse> getCurrencies(String url) {
        if (!mCurrencies.isEmpty()) {
            return mCurrencies;
        } else if (isNetworkAvailable()) {
            try {
                mCurrencies = loadXmlFromNetwork(url);
                saveXmlToDatabase(mCurrencies);
                return mCurrencies;
            } catch (Exception e) {
                return new ArrayList<>();
            }
        } else {
            try {
                mCurrencies = loadXmlFromDatabase();
                return mCurrencies;
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
    }

    private void saveXmlToDatabase(List<XmlCurrenciesResponse> currencies) {
        mCurrenciesDb = mCurrenciesDb == null ? mCurrenciesDbHelper.getWritableDatabase() : mCurrenciesDb;
        for (XmlCurrenciesResponse currency : currencies) {
            ContentValues values = new ContentValues();
            values.put(CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NUM_CODE, currency.numCode);
            values.put(CurrenciesContract.CurrenciesEntry.COLUMN_NAME_CHAR_CODE, currency.charCode);
            values.put(CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NOMINAL, currency.nominal);
            values.put(CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NAME, currency.name);
            values.put(CurrenciesContract.CurrenciesEntry.COLUMN_NAME_VALUE, currency.value);
            mCurrenciesDb.insert(CurrenciesContract.CurrenciesEntry.TABLE_NAME, null, values);
        }
    }

    private List<XmlCurrenciesResponse> loadXmlFromDatabase() {
        mCurrenciesDbRead = mCurrenciesDbRead == null ? mCurrenciesDbHelper.getReadableDatabase() : mCurrenciesDbRead;
        String[] projection = {
                CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NUM_CODE,
                CurrenciesContract.CurrenciesEntry.COLUMN_NAME_CHAR_CODE,
                CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NOMINAL,
                CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NAME,
                CurrenciesContract.CurrenciesEntry.COLUMN_NAME_VALUE
        };
        // String sortOrder = CurrenciesContract.CurrenciesEntry.COLUMN_NAME_CHAR_CODE + " DESC";
        Cursor cursor = mCurrenciesDbRead.query(
                CurrenciesContract.CurrenciesEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        List<XmlCurrenciesResponse> currencies = new ArrayList<>();
        while (cursor.moveToNext()) {
            int numCode = cursor.getInt(cursor.getColumnIndexOrThrow(
                    CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NUM_CODE
            ));
            String charCode = cursor.getString(cursor.getColumnIndexOrThrow(
                    CurrenciesContract.CurrenciesEntry.COLUMN_NAME_CHAR_CODE
            ));
            int nominal = cursor.getInt(cursor.getColumnIndexOrThrow(
                    CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NOMINAL
            ));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(
                    CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NAME
            ));
            float value = cursor.getFloat(cursor.getColumnIndexOrThrow(
                    CurrenciesContract.CurrenciesEntry.COLUMN_NAME_VALUE
            ));
            currencies.add(new XmlCurrenciesResponse(numCode, charCode, nominal, name, value));
        }
        cursor.close();
        return currencies;
    }

    private List<XmlCurrenciesResponse> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {

        XmlCurrenciesParser xmlCurrenciesParser = new XmlCurrenciesParser();
        List<XmlCurrenciesResponse> currencies;

        try (InputStream stream = downloadUrl(urlString)) {
            currencies = xmlCurrenciesParser.parse(stream);
        }

        return currencies;
    }
}
