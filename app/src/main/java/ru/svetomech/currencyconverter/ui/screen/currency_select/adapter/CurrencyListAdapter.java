package ru.svetomech.currencyconverter.ui.screen.currency_select.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import ru.svetomech.currencyconverter.R;
import ru.svetomech.currencyconverter.data.models.response.XmlCurrenciesResponse;

public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder> {

    private List<XmlCurrenciesResponse> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public CurrencyListAdapter(Context context, List<XmlCurrenciesResponse> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.currency_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XmlCurrenciesResponse currency = mData.get(position);
        holder.myTextView.setText(currency.charCode + " | " + currency.name);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.currency_list_item_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public XmlCurrenciesResponse getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
