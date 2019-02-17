package ru.svetomech.currencyconverter.data.repository;

import android.graphics.Bitmap;

import java.util.Map;

public interface IFlagsRepository {
    public Map<String, Bitmap> getFlags(String url, String[] countryCodes);
}
