package com.wavesgo.waves.node.ui;

import com.wavesgo.waves.node.ui.panels.DebugPanel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamReader extends Thread {

    private InputStream stream;
    private DebugPanel debugPanel;

    public StreamReader(InputStream stream, DebugPanel debugPanel) {
        this.stream = stream;
        this.debugPanel = debugPanel;
    }

    public void run() {
        BufferedReader in;
        String str;

        try {
            in = new BufferedReader(new InputStreamReader(stream));

            while ((str = in.readLine()) != null) {
                debugPanel.appendLine(str);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
