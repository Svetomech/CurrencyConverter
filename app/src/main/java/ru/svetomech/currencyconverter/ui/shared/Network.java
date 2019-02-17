package ru.svetomech.currencyconverter.ui.shared;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Network {

    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final String GET = "GET";

    public static boolean isNetworkAvailable() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static InputStream downloadUrl(String urlString) throws IOException {
        // TODO: Request parameters
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setRequestMethod(GET);
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }
}
