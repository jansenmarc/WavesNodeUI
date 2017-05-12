package com.wavesgo.waves.node.ui.panels;

import javax.swing.*;
import java.awt.*;

public class DebugPanel extends JPanel {

    private JTextArea debug = new JTextArea();

    public DebugPanel() {
        JScrollPane scrollPane = new JScrollPane(debug);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setAutoscrolls(true);
    }

    public void appendLine(String line) {
        debug.append(line + System.getProperty("line.separator"));
        debug.invalidate();
        debug.getParent().validate();
        debug.setCaretPosition(debug.getDocument().getLength());
    }

}

