package com.wavesgo.waves.node.ui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.LinkedList;

public class MainPanel extends JPanel {

    private JComboBox<String> debugLevel;
    private JComboBox<String> blockchainType;
    private JTextField checkpointPrivateKey;
    private JTextField directory;
    private NetworkPanel networkPanel;

    public MainPanel(NetworkPanel networkPanel) {
        String[] debugLevels = { "DEBUG", "INFO", "WARN", "ERROR" };
        String[] blockchainTypes = { "MAINNET", "TESTNET", "DEVNET" };

        this.networkPanel = networkPanel;

        debugLevel = new JComboBox<String>(debugLevels);
        blockchainType = new JComboBox<String>(blockchainTypes);
        checkpointPrivateKey = new JTextField();
        directory = new JTextField();

        checkpointPrivateKey.setEnabled(false);
        blockchainType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String network = ((JComboBox)e.getSource()).getSelectedItem().toString();

                if (network.equals("MAINNET")) {
                    checkpointPrivateKey.setText("7EXnkmJyz1gPfLJwytThcwGwpyfjzFXC3hxBhvVK4EQP");

                    networkPanel.setKnownPeers("\"138.201.152.166:6868\", \"138.201.152.165:6868\", \"138.201.152.164:6868\", \"138.201.152.163:6868\", \"34.192.116.209:6868\", \"52.8.147.91:6868\", \"52.192.108.188:6868\", \"52.62.212.53:6868\", \"52.67.63.123:6868\"");
                } else if (network.equals("TESTNET")) {
                    checkpointPrivateKey.setText("");

                    networkPanel.setKnownPeers("\"52.30.47.67:6863\", \"52.28.66.217:6863\", \"52.77.111.219:6863\", \"52.51.92.182:6863\"");
                } else if (network.equals("DEVNET")) {
                    checkpointPrivateKey.setText("");

                    networkPanel.setKnownPeers("\"34.251.200.245:6864\", \"35.157.212.173:6864\", \"54.255.193.27:6864\"");
                }
            }
        });

        setLayout(new GridLayout(4, 2));

        add(new JLabel("Debug level"));
        add(debugLevel);
        add(new JLabel("Checkpoints public key"));
        add(checkpointPrivateKey);
        add(new JLabel("Blockchain type"));
        add(blockchainType);
        add(new JLabel("Directory"));
        add(directory);
    }

    public void store(PrintWriter out) {
        out.println("   logging-level = " + debugLevel.getSelectedItem());
        out.println("   checkpoints.public-key = \"" + checkpointPrivateKey.getText() + "\"");
        out.println("   blockchain.type = " + blockchainType.getSelectedItem());
        out.println("   directory = \"" + directory.getText() + "\"");
        out.flush();
    }

    public void parse(LinkedList<String> lines) {
        String buffer;

        for (String line: lines) {
            if (line.indexOf("logging-level") != -1) {
                buffer = line.substring(line.indexOf("=") + 2);

                debugLevel.setSelectedItem(buffer);
            } else if (line.indexOf("checkpoints.public-key") != -1) {
                buffer = line.substring(line.indexOf(" = \"") + 4);
                buffer = buffer.substring(0, buffer.indexOf("\""));

                checkpointPrivateKey.setText(buffer);
            } else if (line.indexOf("   blockchain.type") != -1) {
                buffer = line.substring(line.indexOf("=") + 2);

                blockchainType.setSelectedItem(buffer);
            } else if (line.indexOf("directory") != -1) {
                buffer = line.substring(line.indexOf(" = \"") + 4);
                buffer = buffer.substring(0, buffer.indexOf("\""));

                directory.setText(buffer);
            }
        }
    }

}
