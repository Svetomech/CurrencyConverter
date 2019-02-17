package ru.svetomech.currencyconverter.data.repository.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ru.svetomech.currencyconverter.data.repository.IFlagsRepository;

import static ru.svetomech.currencyconverter.ui.shared.Network.downloadUrl;
import static ru.svetomech.currencyconverter.ui.shared.Network.isNetworkAvailable;

public class FlagsRepositoryImpl implements IFlagsRepository {

    private Context mContext;
    private Map<String, Bitmap> mFlags;

    public FlagsRepositoryImpl(Context context) {
        mContext = context;
        mFlags = new HashMap<>();
    }

    @Override
    public Map<String, Bitmap> getFlags(String url, String[] countryCodes) {
        if (!mFlags.isEmpty()) {
            return mFlags;
        } else if (isNetworkAvailable()) {
            try {
                mFlags = loadFlagsFromFilesystem();
                return mFlags;
            } catch (Exception e) {
                return new HashMap<>();
            }
        } else if (isNetworkAvailable()) {
            try {
                mFlags = loadFlagsFromNetwork(url, countryCodes);
                saveFlagsToFilesystem(mFlags);
                return mFlags;
            } catch (Exception e) {
                return new HashMap<>();
            }
        } else {
            return new HashMap<>();
        }
    }


    private Map<String, Bitmap> loadFlagsFromNetwork(String url, String[] countryCodes) {
        Map<String, Bitmap> flags = new HashMap<>();
        for (String countryCode : countryCodes) {
            Bitmap bmp = null;
            try {
                String filename = countryCode + ".png";
                InputStream is = downloadUrl(url + filename);
                bmp = BitmapFactory.decodeStream(is);
                flags.put(filename, bmp);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flags;
    }

    private Map<String, Bitmap> loadFlagsFromFilesystem() {
        File dir = mContext.getFilesDir();
        File[] subFiles = dir.listFiles();
        Map<String, Bitmap> flags = new HashMap<>();
        if (subFiles != null)
        {
            for (File file : subFiles)
            {
                Bitmap bmp = null;
                try {
                    String filename = file.getName();
                    FileInputStream is = mContext.openFileInput(filename);
                    bmp = BitmapFactory.decodeStream(is);
                    flags.put(filename, bmp);
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return flags;
    }

    private void saveFlagsToFilesystem(Map<String, Bitmap> flags) {
        for (Map.Entry<String, Bitmap> flag : flags.entrySet()) {
            try {
                FileOutputStream stream = mContext.openFileOutput(flag.getKey(), Context.MODE_PRIVATE);
                flag.getValue().compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
                flag.getValue().recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
