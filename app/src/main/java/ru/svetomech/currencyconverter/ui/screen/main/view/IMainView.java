package ru.svetomech.currencyconverter.ui.screen.main.view;

import ru.svetomech.currencyconverter.data.repository.ICurrenciesRepository;
import ru.svetomech.currencyconverter.ui.shared.IMvpView;

public interface IMainView extends IMvpView {
    void startCurrencySelectScreenCommand(ICurrenciesRepository currencies);
    void recalculateCurrenciesCommand(ICurrenciesRepository currencies);
}
