package ru.svetomech.currencyconverter.ui.screen.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ru.svetomech.currencyconverter.R;
import ru.svetomech.currencyconverter.data.models.response.XmlCurrenciesResponse;
import ru.svetomech.currencyconverter.data.repository.ICurrenciesRepository;
import ru.svetomech.currencyconverter.data.repository.impl.CurrenciesRepositoryImpl;
import ru.svetomech.currencyconverter.data.repository.impl.FlagsRepositoryImpl;
import ru.svetomech.currencyconverter.ui.screen.currency_select.activity.CurrencySelectActivity;
import ru.svetomech.currencyconverter.ui.screen.main.presenter.MainPresenter;
import ru.svetomech.currencyconverter.ui.screen.main.view.IMainView;
import ru.svetomech.currencyconverter.ui.shared.IocContainer;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements IMainView {

    public static final String CURRENCIES_KEY = "ru.svetomech.currencyconverter.CURRENCIES";
    public static final String CURRENCY_KEY = "ru.svetomech.currencyconverter.CURRENCY";

    private MainPresenter mPresenter = new MainPresenter();
    private List<XmlCurrenciesResponse> mCurrenciesSelected;

    private void focusRubText() {
        final EditText rub = findViewById(R.id.currency_rub_text);
        rub.setFocusableInTouchMode(true);
        rub.requestFocus();
        showKeyBoard();
    }

    private void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onResume() {
        super.onResume();

        View fab = findViewById(R.id.fab);
        fab.setEnabled(true);

        focusRubText();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && mCurrenciesSelected.size() < 3) {
            XmlCurrenciesResponse currency = data.getParcelableExtra(CURRENCY_KEY);
            mCurrenciesSelected.add(currency);
            switch (mCurrenciesSelected.size()) {
                case 1:
                    showFirstCurrency();
                    break;
                case 2:
                    showSecondCurrency();
                    break;
                case 3:
                    showThirdCurrency();
                    break;
            }
        }
        mPresenter.recalculateCurrencies();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                mPresenter.startCurrencySelectScreen();
            }
        });

        mCurrenciesSelected = new ArrayList<>();
        focusRubText();

        // TODO: Find a way to pass app context NOT in main activity
        if (IocContainer.resolve("ICurrenciesRepository") == null) {
            IocContainer.addSingleton("ICurrenciesRepository", new CurrenciesRepositoryImpl(getApplicationContext()));
        }
        if (IocContainer.resolve("IFlagsRepository") == null) {
            IocContainer.addSingleton("IFlagsRepository", new FlagsRepositoryImpl(getApplicationContext()));
        }

        mPresenter.attachView(this);
        // TODO: Don't call in MainActivity
        // mPresenter.getFlags();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFirstCurrency() {
        CurrencyEdit currency = findViewById(R.id.currency_first_text);
        currency.setVisibility(VISIBLE);
        TextView currencyLabel = findViewById(R.id.currency_first_label);
        currencyLabel.setText(mCurrenciesSelected.get(0).charCode);
        currencyLabel.setVisibility(VISIBLE);
    }

    private void showSecondCurrency() {
        CurrencyEdit currency = findViewById(R.id.currency_second_text);
        currency.setVisibility(VISIBLE);
        TextView currencyLabel = findViewById(R.id.currency_second_label);
        currencyLabel.setText(mCurrenciesSelected.get(1).charCode);
        currencyLabel.setVisibility(VISIBLE);
    }

    private void showThirdCurrency() {
        CurrencyEdit currency = findViewById(R.id.currency_third_text);
        currency.setVisibility(VISIBLE);
        TextView currencyLabel = findViewById(R.id.currency_third_label);
        currencyLabel.setText(mCurrenciesSelected.get(2).charCode);
        currencyLabel.setVisibility(VISIBLE);
        View fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void startCurrencySelectScreenCommand(ICurrenciesRepository currencies) {
        new GetCurrenciesTask(this, currencies).execute(getString(R.string.currencies_url));
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        mPresenter.recalculateCurrencies();
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void recalculateCurrenciesCommand(ICurrenciesRepository currencies) {
        CurrencyEdit rub = findViewById(R.id.currency_rub_text);
        CurrencyEdit currency1 = findViewById(R.id.currency_first_text);
        CurrencyEdit currency2 = findViewById(R.id.currency_second_text);
        CurrencyEdit currency3 = findViewById(R.id.currency_third_text);
        CurrencyEdit currentCurrency = (CurrencyEdit) getCurrentFocus();
        if (currentCurrency == rub) {
            Editable rubText = rub.getText();
            if (rubText != null && rubText.length() > 0) {
                float ruble = Float.parseFloat(rubText.toString());
                switch (mCurrenciesSelected.size()) {
                    case 3:
                        currency3.setText(String.valueOf(ruble / (mCurrenciesSelected.get(2).value / mCurrenciesSelected.get(2).nominal)));
                    case 2:
                        currency2.setText(String.valueOf(ruble / (mCurrenciesSelected.get(1).value) / mCurrenciesSelected.get(1).nominal));
                    case 1:
                        currency1.setText(String.valueOf(ruble / (mCurrenciesSelected.get(0).value / mCurrenciesSelected.get(0).nominal)));
                        break;
                }
            }
        } else if (currentCurrency == currency1) {
            Editable currency1Text = currency1.getText();
            if (currency1Text != null && currency1Text.length() > 0) {
                float curr1 = Float.parseFloat(currency1Text.toString());
                float ruble = (mCurrenciesSelected.get(0).value / mCurrenciesSelected.get(0).nominal) * curr1;
                rub.setText(String.valueOf(ruble));
                switch (mCurrenciesSelected.size()) {
                    case 3:
                        currency3.setText(String.valueOf(ruble / (mCurrenciesSelected.get(2).value / mCurrenciesSelected.get(2).nominal)));
                    case 2:
                        currency2.setText(String.valueOf(ruble / (mCurrenciesSelected.get(1).value / mCurrenciesSelected.get(1).nominal)));
                        break;
                }
            }
        } else if (currentCurrency == currency2) {
            Editable currency2Text = currency2.getText();
            if (currency2Text != null && currency2Text.length() > 0) {
                float curr2 = Float.parseFloat(currency2Text.toString());
                float ruble = (mCurrenciesSelected.get(1).value / mCurrenciesSelected.get(1).nominal) * curr2;
                rub.setText(String.valueOf(ruble));
                currency1.setText(String.valueOf(ruble / (mCurrenciesSelected.get(0).value / mCurrenciesSelected.get(0).nominal)));
                if (mCurrenciesSelected.size() == 3) {
                    currency3.setText(String.valueOf(ruble / (mCurrenciesSelected.get(2).value / mCurrenciesSelected.get(2).nominal)));
                }
            }
        } else if (currentCurrency == currency3) {
            Editable currency3Text = currency3.getText();
            if (currency3Text != null && currency3Text.length() > 0) {
                float curr3 = Float.parseFloat(currency3Text.toString());
                float ruble = (mCurrenciesSelected.get(2).value / mCurrenciesSelected.get(2).nominal) * curr3;
                rub.setText(String.valueOf(ruble));
                currency1.setText(String.valueOf(ruble / (mCurrenciesSelected.get(0).value / mCurrenciesSelected.get(0).nominal)));
                currency2.setText(String.valueOf(ruble / (mCurrenciesSelected.get(1).value / mCurrenciesSelected.get(1).nominal)));
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class GetCurrenciesTask extends AsyncTask<String, Void, List<XmlCurrenciesResponse>> {

        private WeakReference<Context> mContext;
        private ICurrenciesRepository mCurrencies;

        GetCurrenciesTask(Context context, ICurrenciesRepository currencies) {
            this.mContext = new WeakReference<>(context);
            this.mCurrencies = currencies;
        }

        @Override
        protected List<XmlCurrenciesResponse> doInBackground(String... urls) {
            return mCurrencies.getCurrencies(urls[0]);
        }

        @Override
        protected void onPostExecute(List<XmlCurrenciesResponse> result) {
            Intent intent = new Intent(mContext.get(), CurrencySelectActivity.class);
            intent.putParcelableArrayListExtra(CURRENCIES_KEY, (ArrayList<XmlCurrenciesResponse>) result);
            startActivityForResult(intent, 0);
        }
    }
}
