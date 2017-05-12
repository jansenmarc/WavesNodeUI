package com.wavesgo.waves.node.ui.com.wavesgo.waves.node.ui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.LinkedList;

public class MatcherPanel extends JPanel {

    private JComboBox<String> enabled;
    private JTextField bindAddress = new JTextField();
    private JTextField port = new JTextField();
    private JTextField account = new JTextField();

    public MatcherPanel() {
        enabled = new JComboBox<String>(new String[] { "yes", "no" });
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Enable"));
        add(enabled);
        add(new JLabel("Account"));
        add(account);
        add(new JLabel("Bind address"));
        add(bindAddress);
        add(new JLabel("Port"));
        add(port);

        enabled.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                init();
            }
        });

        init();
    }

    public void store(PrintWriter out) {
        out.println("   matcher {");
        out.println("       enabled = " + enabled.getSelectedItem());
        out.println("       account = \"" + account.getText() + "\"");
        out.println("       bind-address = \"" + bindAddress.getText() + "\"");
        out.println("       port = " + port.getText());
        out.println("   }");
        out.flush();
    }

    public void parse(LinkedList<String> lines) {
        String buffer;
        boolean matcherSectionFound = false;

        for (String line: lines) {
            if (line.indexOf("matcher {") != -1) {
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
            } else if (line.indexOf("account = ") != -1 && matcherSectionFound) {
                buffer = line.substring(line.indexOf(" = \"") + 4);
                buffer = buffer.substring(0, buffer.indexOf("\""));

                account.setText(buffer);
            } else if (line.indexOf("port = ") != -1 && matcherSectionFound) {
                buffer = line.substring(line.indexOf(" = ") + 3);

                port.setText(buffer);
            }
        }
    }

    private void init() {
        if (!((String)enabled.getSelectedItem()).equals("yes")) {
            bindAddress.setEnabled(false);
            account.setEnabled(false);
            port.setEnabled(false);
        } else {
            bindAddress.setEnabled(true);
            account.setEnabled(true);
            port.setEnabled(true);
        }
    }

}
