package ru.svetomech.currencyconverter.ui.screen.currency_select.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.svetomech.currencyconverter.R;
import ru.svetomech.currencyconverter.data.models.response.XmlCurrenciesResponse;
import ru.svetomech.currencyconverter.ui.screen.currency_select.adapter.CurrencyListAdapter;
import ru.svetomech.currencyconverter.ui.screen.currency_select.presenter.CurrencySelectPresenter;
import ru.svetomech.currencyconverter.ui.screen.currency_select.view.ICurrencySelectView;
import ru.svetomech.currencyconverter.ui.screen.main.activity.MainActivity;

import static ru.svetomech.currencyconverter.ui.screen.main.activity.MainActivity.CURRENCY_KEY;

public class CurrencySelectActivity extends AppCompatActivity implements ICurrencySelectView, CurrencyListAdapter.ItemClickListener {

    private CurrencyListAdapter mAdapter;
    private CurrencySelectPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        List<XmlCurrenciesResponse> currencies = intent.getParcelableArrayListExtra(MainActivity.CURRENCIES_KEY);
        if (currencies.isEmpty()) {
            currencies.add(new XmlCurrenciesResponse(0, getString(R.string.error), 0, getString(R.string.no_internet_connection), 0f));
        }

        RecyclerView recyclerView = findViewById(R.id.currency_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new CurrencyListAdapter(this, currencies);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onItemClick(View view, int position) {
        XmlCurrenciesResponse currency = mAdapter.getItem(position);
        if (currency.charCode == getString(R.string.error)) {
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(CURRENCY_KEY, new XmlCurrenciesResponse(currency.numCode, currency.charCode, currency.nominal, currency.name, currency.value));
        setResult(RESULT_OK, intent);
        finish();
    }
}
