package ru.svetomech.currencyconverter.data.repository;

import java.util.List;

import ru.svetomech.currencyconverter.data.models.response.XmlCurrenciesResponse;

public interface ICurrenciesRepository {
    List<XmlCurrenciesResponse> getCurrencies(String url);

}
