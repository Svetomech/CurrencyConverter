package ru.svetomech.currencyconverter.data.models.response;

import android.os.Parcel;
import android.os.Parcelable;

public class XmlCurrenciesResponse implements Parcelable {
    public final int numCode; // 036
    public final String charCode; // "AUD"
    public final int nominal; // 1
    public final String name; // "Австралийский доллар"
    public final float value; // 46,8385

    public XmlCurrenciesResponse(int numCode, String charCode, int nominal, String name, float value) {
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(numCode);
        out.writeString(charCode);
        out.writeInt(nominal);
        out.writeString(name);
        out.writeFloat(value);
    }

    public static final Parcelable.Creator<XmlCurrenciesResponse> CREATOR = new Parcelable.Creator<XmlCurrenciesResponse>() {
        public XmlCurrenciesResponse createFromParcel(Parcel in) {
            return new XmlCurrenciesResponse(in);
        }

        public XmlCurrenciesResponse[] newArray(int size) {
            return new XmlCurrenciesResponse[size];
        }
    };

    private XmlCurrenciesResponse(Parcel in) {
        numCode = in.readInt();
        charCode = in.readString();
        nominal = in.readInt();
        name = in.readString();
        value = in.readFloat();
    }
}
