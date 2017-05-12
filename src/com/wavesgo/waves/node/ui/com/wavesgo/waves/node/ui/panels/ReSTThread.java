package com.wavesgo.waves.node.ui.com.wavesgo.waves.node.ui.panels;

import com.wavesgo.waves.node.ui.Callback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReSTThread extends Thread {

    private String host;
    private String endpoint;
    private Callback callback;

    public ReSTThread(String host, String endpoint, Callback callback) {
        this.host = host;
        this.endpoint = endpoint;
        this.callback = callback;
    }

    public void run() {
        URL url;
        HttpURLConnection urlCon;

        try {
            url = new URL(host + endpoint);
            urlCon = (HttpURLConnection)url.openConnection();

            urlCon.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            StringBuffer strBuf = new StringBuffer();
            String str;

            while ((str = in.readLine()) != null) {
                strBuf.append(str);
            }

            callback.requestFinished(strBuf.toString());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
