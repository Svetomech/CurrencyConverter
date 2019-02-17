package ru.svetomech.currencyconverter.ui.screen.main.presenter;

import ru.svetomech.currencyconverter.data.repository.ICurrenciesRepository;
import ru.svetomech.currencyconverter.ui.screen.main.view.IMainView;
import ru.svetomech.currencyconverter.ui.shared.IocContainer;
import ru.svetomech.currencyconverter.ui.shared.MvpPresenter;

public class MainPresenter extends MvpPresenter<IMainView> {

    private ICurrenciesRepository mCurrencies;

    @Override
    protected void onViewAttached() {
        mCurrencies = (ICurrenciesRepository) IocContainer.resolve("ICurrenciesRepository");
    }

    public void startCurrencySelectScreen() {
        getView().startCurrencySelectScreenCommand(mCurrencies);
    }

    public void recalculateCurrencies() {
        getView().recalculateCurrenciesCommand(mCurrencies);
    }
}
