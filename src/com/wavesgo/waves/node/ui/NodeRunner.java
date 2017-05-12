package com.wavesgo.waves.node.ui;

import com.wavesgo.waves.node.ui.com.wavesgo.waves.node.ui.panels.DebugPanel;

public enum NodeRunner {

    INSTANCE;

    private Process node;
    private DebugPanel debugPanel;

    public void setDebugPanel(DebugPanel debugPanel) {
        this.debugPanel = debugPanel;
    }

    public void startNode() {
        ProcessBuilder builder;
        StreamReader inputStreamReader;
        StreamReader errorStreamReader;

        if (node == null) {
            try {
                builder = new ProcessBuilder(new String[] {"java", "-jar", "waves.jar", "waves.conf" });

                node = builder.start();
                inputStreamReader = new StreamReader(node.getInputStream(), debugPanel);
                errorStreamReader = new StreamReader(node.getErrorStream(), debugPanel);
                inputStreamReader.start();
                errorStreamReader.start();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void stopNode() {
        if (node != null) {
            node.destroy();
            node = null;
        }
    }

}
