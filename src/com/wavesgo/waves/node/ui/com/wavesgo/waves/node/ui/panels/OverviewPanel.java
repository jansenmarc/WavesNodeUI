package com.wavesgo.waves.node.ui.com.wavesgo.waves.node.ui.panels;

import com.google.gson.JsonParser;
import com.wavesgo.waves.node.ui.Callback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OverviewPanel extends JPanel implements ActionListener {

    private JLabel syncStatus = new JLabel("-");
    private JLabel blockHeight = new JLabel("-");
    private JLabel mainAddress = new JLabel("-");
    private JLabel balanceOnMainAddress = new JLabel("-");
    private JLabel nodeVersion = new JLabel("-");
    private JButton update;
    private ReSTApiPanel restApiPanel;

    public OverviewPanel(ReSTApiPanel restApiPanel) {
        JPanel center = new JPanel();
        JPanel south = new JPanel();
        update = new JButton("Update");

        this.restApiPanel = restApiPanel;

        center.setLayout(new GridLayout(5, 2));
        center.add(new JLabel("Current block height:"));
        center.add(blockHeight);
        center.add(new JLabel("Sync status:"));
        center.add(syncStatus);
        center.add(new JLabel("Main address:"));
        center.add(mainAddress);
        center.add(new JLabel("Balance on main address:"));
        center.add(balanceOnMainAddress);
        center.add(new JLabel("Node version:"));
        center.add(nodeVersion);

        south.add(update);
        update.setEnabled(false);

        setLayout(new BorderLayout());
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        update.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        update();
    }

    public void setButtonEnabled(boolean enabled) {
        update.setEnabled(enabled);
    }

    private void update() {
        String host = "http://" + restApiPanel.getHost() + ":" + restApiPanel.getPort();

        updateBlockHeight();
        updateMainAddress();
        updateSyncStatus();
        updateNodeVersion();
    }

    private void updateNodeVersion() {
        String host = "http://" + restApiPanel.getHost() + ":" + restApiPanel.getPort();

        ReSTThread request = new ReSTThread(host, "/node/version", new Callback() {
            public void requestFinished(String json) {
                String version = new JsonParser().parse(json).getAsJsonObject().get("version").getAsString();

                nodeVersion.setText(version);
            }
        });

        request.start();
    }

    private void updateSyncStatus() {
        String host = "http://" + restApiPanel.getHost() + ":" + restApiPanel.getPort();

        ReSTThread request = new ReSTThread(host, "/blocks/height", new Callback() {
            public void requestFinished(String json) {
                int localHeight = new JsonParser().parse(json).getAsJsonObject().get("height").getAsInt();
                String secondHost = "http://52.30.47.67:6869";

                ReSTThread secondRequest = new ReSTThread(secondHost, "/blocks/height", new Callback() {
                    public void requestFinished(String secondJson) {
                        int remoteHeight = new JsonParser().parse(secondJson).getAsJsonObject().get("height").getAsInt();

                        if (remoteHeight > localHeight) {
                            syncStatus.setText("not synchronized");
                        } else {
                            syncStatus.setText("synchronized");
                        }
                    }
                });

                secondRequest.start();
            }
        });

        request.start();
    }

    private void updateBalanceOnMainAddress() {
        String host = "http://" + restApiPanel.getHost() + ":" + restApiPanel.getPort();

        ReSTThread request = new ReSTThread(host, "/addresses/balance/" + mainAddress.getText(), new Callback() {
            public void requestFinished(String json) {
                String balance = new JsonParser().parse(json).getAsJsonObject().get("balance").getAsString();

                balanceOnMainAddress.setText(balance);
            }
        });

        request.start();
    }

    private void updateMainAddress() {
        String host = "http://" + restApiPanel.getHost() + ":" + restApiPanel.getPort();

        ReSTThread request = new ReSTThread(host, "/addresses", new Callback() {
            public void requestFinished(String json) {
                String address = new JsonParser().parse(json).getAsJsonArray().get(0).getAsString();

                mainAddress.setText(address);
                updateBalanceOnMainAddress();
            }
        });

        request.start();
    }

    private void updateBlockHeight() {
        String host = "http://" + restApiPanel.getHost() + ":" + restApiPanel.getPort();

        ReSTThread request = new ReSTThread(host, "/blocks/height", new Callback() {
            public void requestFinished(String json) {
                String height = new JsonParser().parse(json).getAsJsonObject().get("height").getAsString();

                blockHeight.setText(height);
            }
        });

        request.start();
    }

}
