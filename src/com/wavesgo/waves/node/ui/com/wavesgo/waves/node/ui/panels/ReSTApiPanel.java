package com.wavesgo.waves.node.ui.com.wavesgo.waves.node.ui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.LinkedList;

public class ReSTApiPanel extends JPanel implements ActionListener {

    private JComboBox<String> enabled;
    private JTextField bindAddress = new JTextField();
    private JTextField port = new JTextField();
    private JTextField apiKeyHash = new JTextField();

    public ReSTApiPanel() {
        enabled = new JComboBox<String>(new String[] { "yes", "no" });
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Enable"));
        add(enabled);
        add(new JLabel("Bind address"));
        add(bindAddress);
        add(new JLabel("Port"));
        add(port);
        add(new JLabel("API key hash"));
        add(apiKeyHash);

        enabled.addActionListener(this);

        init();
    }

    public void actionPerformed(ActionEvent e) {
        init();
    }

    public void store(PrintWriter out) {
        out.println("   rest-api {");
        out.println("       enable = " + enabled.getSelectedItem());
        out.println("       bind-address = \"" + bindAddress.getText() + "\"");
        out.println("       port = " + port.getText());
        out.println("       api-key-hash = \"" + apiKeyHash.getText() + "\"");
        out.println("   }");
        out.flush();
    }

    public void parse(LinkedList<String> lines) {
        String buffer;
        boolean matcherSectionFound = false;

        for (String line: lines) {
            if (line.indexOf("rest-api {") != -1) {
                matcherSectionFound = true;
            } else if (line.indexOf("}") != -1) {
                matcherSectionFound = false;
            } else if (line.indexOf("enabled") != -1 && matcherSectionFound) {
                buffer = line.substring(line.indexOf(" = ") + 3);

                enabled.setSelectedItem(buffer);
            } else if (line.indexOf("bind-address") != -1 && matcherSectionFound) {
                buffer = line.substring(line.indexOf(" = \"") + 4);
                buffer = buffer.substring(0, buffer.indexOf("\""));

                bindAddress.setText(buffer);
            } else if (line.indexOf("api-key-hash") != -1 && matcherSectionFound) {
                buffer = line.substring(line.indexOf(" = \"") + 4);
                buffer = buffer.substring(0, buffer.indexOf("\""));

                apiKeyHash.setText(buffer);
            } else if (line.indexOf("port") != -1 && matcherSectionFound) {
                buffer = line.substring(line.indexOf(" = ") + 3);

                port.setText(buffer);
            }
        }

        init();
    }

    public String getHost() {
        return bindAddress.getText();
    }

    public int getPort() {
        return Integer.parseInt(port.getText());
    }

    private void init() {
        if (!((String)enabled.getSelectedItem()).equals("yes")) {
            bindAddress.setEnabled(false);
            apiKeyHash.setEnabled(false);
            port.setEnabled(false);
        } else {
            bindAddress.setEnabled(true);
            apiKeyHash.setEnabled(true);
            port.setEnabled(true);
        }
    }

}
