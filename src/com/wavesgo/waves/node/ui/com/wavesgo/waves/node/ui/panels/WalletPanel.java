package com.wavesgo.waves.node.ui.com.wavesgo.waves.node.ui.panels;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.LinkedList;

public class WalletPanel extends JPanel {

    private JTextField password = new JTextField();
    private JTextField seed = new JTextField();

    public WalletPanel() {
        setLayout(new GridLayout(2, 2));

        add(new JLabel("Wallet password"));
        add(password);
        add(new JLabel("Seed"));
        add(seed);
    }

    public void store(PrintWriter out) {
        out.println("   wallet {");
        out.println("       password = \"" + password.getText() + "\"");
        out.println("       seed = \"" + seed.getText() + "\"");
        out.println("   }");
        out.flush();
    }

    public void parse(LinkedList<String> lines) {
        String buffer;

        for (String line: lines) {
            if (line.indexOf("password") != -1) {
                buffer = line.substring(line.indexOf(" = \"") + 4);
                buffer = buffer.substring(0, buffer.indexOf("\""));

                password.setText(buffer);
            } else if (line.indexOf("seed = ") != -1) {
                buffer = line.substring(line.indexOf(" = \"") + 4);
                buffer = buffer.substring(0, buffer.indexOf("\""));

               seed.setText(buffer);
            }
        }
    }

}
