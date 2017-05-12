package com.wavesgo.waves.node.ui.panels;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.LinkedList;

public class NetworkPanel extends JPanel {

    private JTextField knownPeers = new JTextField();
    private JTextField bindAddress = new JTextField();
    private JTextField port = new JTextField();
    private JTextField nodeName = new JTextField();
    private JTextField declaredAddress = new JTextField();

    public NetworkPanel() {
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Known peers"));
        add(knownPeers);
        add(new JLabel("Bind address"));
        add(bindAddress);
        add(new JLabel("Port"));
        add(port);
        add(new JLabel("Node name"));
        add(nodeName);
        add(new JLabel("Declared address"));
        add(declaredAddress);
    }

    public void setKnownPeers(String knownPeersList) {
        knownPeers.setText(knownPeersList);
    }

    public void store(PrintWriter out) {
        out.println("   network {");
        out.println("       known-peers = [" + knownPeers.getText() + "]");
        out.println("       bind-address = \"" + bindAddress.getText() + "\"");
        out.println("       port = " + port.getText());
        out.println("       node-name = \"" + nodeName.getText() + "\"");
        out.println("       declaredAddress = \"" + declaredAddress.getText() + "\"");
        out.println("   }");
        out.flush();
    }

    public void parse(LinkedList<String> lines) {
        String buffer;
        boolean networkSectionFound = false;

        for (String line: lines) {
            if (line.indexOf("network {") != -1) {
                networkSectionFound = true;
            } else if (line.indexOf("}") != -1) {
                networkSectionFound = false;
            } else if (line.indexOf("known-peers") != -1 && networkSectionFound) {
                buffer = line.substring(line.indexOf(" = ") + 4);
                buffer = buffer.substring(0, buffer.indexOf("]"));

                knownPeers.setText(buffer);
            } else if (line.indexOf("bind-address") != -1 && networkSectionFound) {
                buffer = line.substring(line.indexOf(" = \"") + 4);
                buffer = buffer.substring(0, buffer.indexOf("\""));

                bindAddress.setText(buffer);
            } else if (line.indexOf("node-name") != -1 && networkSectionFound) {
                buffer = line.substring(line.indexOf(" = \"") + 4);
                buffer = buffer.substring(0, buffer.indexOf("\""));

                nodeName.setText(buffer);
            } else if (line.indexOf("declaredAddress") != -1 && networkSectionFound) {
                buffer = line.substring(line.indexOf(" = \"") + 4);
                buffer = buffer.substring(0, buffer.indexOf("\""));

                declaredAddress.setText(buffer);
            } else if (line.indexOf("port = ") != -1 && networkSectionFound) {
                buffer = line.substring(line.indexOf(" = ") + 3);

                port.setText(buffer);
            }
        }
    }

}
