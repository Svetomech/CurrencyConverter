package ru.svetomech.currencyconverter.ui.screen.main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import java.lang.ref.WeakReference;

import androidx.appcompat.widget.AppCompatEditText;

import static ru.svetomech.currencyconverter.ui.shared.Numbers.FilterDecimal;

public class CurrencyEdit extends AppCompatEditText {

    @Override
    public void onEditorAction(int actionCode) {
        int orientation = getResources().getConfiguration().orientation;
        if (actionCode != EditorInfo.IME_ACTION_DONE || orientation == Configuration.ORIENTATION_LANDSCAPE) {
            super.onEditorAction(actionCode);
        }
    }

    public CurrencyEdit(Context context, AttributeSet attrs) {
        super(context, attrs);

        final WeakReference<CurrencyEdit> thisClosure = new WeakReference<>(this);
        this.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void afterTextChanged(Editable arg0) {
                String str = thisClosure.get().getText().toString();
                if (str.isEmpty()) return;
                String str2 = FilterDecimal(str, 7, 4, 1000000);

                if (!str2.equals(str)) {
                    thisClosure.get().setText(str2);
                    int pos = thisClosure.get().getText().length();
                    thisClosure.get().setSelection(pos);
                }
            }
        });
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        int orientation = getResources().getConfiguration().orientation;
        if (keyCode == KeyEvent.KEYCODE_BACK && orientation != Configuration.ORIENTATION_LANDSCAPE) {
            ((Activity) getContext()).finish();
            return true;
        } else {
            return super.onKeyPreIme(keyCode, event);
        }
    }

}
