package ru.svetomech.currencyconverter.data.models.parser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.svetomech.currencyconverter.data.models.response.XmlCurrenciesResponse;

public class XmlCurrenciesParser {
    private static final String NS = null;
    private static final String VAL_CURS = "ValCurs";
    private static final String VALUTE = "Valute";
    private static final String NUM_CODE = "NumCode";
    private static final String CHAR_CODE = "CharCode";
    private static final String NOMINAL = "Nominal";
    private static final String NAME = "Name";
    private static final String VALUE = "Value";

    public List<XmlCurrenciesResponse> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readValCurs(parser);
        } finally {
            in.close();
        }
    }

    private List<XmlCurrenciesResponse> readValCurs(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<XmlCurrenciesResponse> currencies = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, NS, VAL_CURS);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(VALUTE)) {
                currencies.add(readValute(parser));
            } else {
                skip(parser);
            }
        }
        return currencies;
    }

    private XmlCurrenciesResponse readValute(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, VALUTE);
        int numCode = 0;
        String charCode = null;
        int nominal = 0;
        String name = null;
        float value = 0;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String parserName = parser.getName();
            switch (parserName) {
                case NUM_CODE:
                    numCode = readNumCode(parser);
                    break;
                case CHAR_CODE:
                    charCode = readCharCode(parser);
                    break;
                case NOMINAL:
                    nominal = readNominal(parser);
                    break;
                case NAME:
                    name = readName(parser);
                    break;
                case VALUE:
                    value = readValue(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new XmlCurrenciesResponse(numCode, charCode, nominal, name, value);
    }

    private int readNumCode(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NS, NUM_CODE);
        int numCode = readNumber(parser);
        parser.require(XmlPullParser.END_TAG, NS, NUM_CODE);
        return numCode;
    }

    private String readCharCode(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NS, CHAR_CODE);
        String charCode = readText(parser);
        parser.require(XmlPullParser.END_TAG, NS, CHAR_CODE);
        return charCode;
    }

    private int readNominal(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NS, NOMINAL);
        int nominal = readNumber(parser);
        parser.require(XmlPullParser.END_TAG, NS, NOMINAL);
        return nominal;
    }

    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NS, NAME);
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, NS, NAME);
        return name;
    }

    private float readValue(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NS, VALUE);
        float value = readNumberFloating(parser);
        parser.require(XmlPullParser.END_TAG, NS, VALUE);
        return value;
    }

    private int readNumber(XmlPullParser parser) throws IOException, XmlPullParserException {
        int number;
        try {
            number = Integer.parseInt(readText(parser));
        }
        catch (NumberFormatException e)
        {
            number = 0;
        }
        return number;
    }

    private float readNumberFloating(XmlPullParser parser) throws IOException, XmlPullParserException {
        float numberFloating;
        try {
            numberFloating = Float.parseFloat(readText(parser).replace(',','.'));
        }
        catch (NumberFormatException e)
        {
            numberFloating = 0;
        }
        return numberFloating;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
