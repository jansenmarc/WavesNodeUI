package com.wavesgo.waves.node.ui;

import com.wavesgo.waves.node.ui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

public class WavesNodeUI extends WindowAdapter implements ActionListener {

    private JFrame frame = new JFrame();
    private JButton stopButton = new JButton("Stop node");;
    private JButton startButton = new JButton("Start node");;
    private NetworkPanel networkPanel = new NetworkPanel();
    private MainPanel mainConfigPanel = new MainPanel(networkPanel);
    private WalletPanel walletPanel = new WalletPanel();
    private MatcherPanel matcherPanel = new MatcherPanel();
    private ReSTApiPanel restPanel = new ReSTApiPanel();
    private OverviewPanel overviewPanel = new OverviewPanel(restPanel);


    private WavesNodeUI() {
        JPanel mainPanel = createMainPanel();

        frame.setTitle("WavesGo Waves Node UI");
        frame.addWindowListener(this);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void windowClosing(WindowEvent e) {
        System.out.println ("stopping node!");
        NodeRunner.INSTANCE.stopNode();
        System.exit(0);
    }

    private JPanel createMainPanel() {
        LinkedList<String> configFile = parseConfigFile();
        JPanel mainPanel = new JPanel();
        JPanel southPanel = new JPanel();
        JTabbedPane tabPane = new JTabbedPane();
        JButton saveButton = new JButton("Save config");
        DebugPanel debugPanel = new DebugPanel();

        NodeRunner.INSTANCE.setDebugPanel(debugPanel);

        overviewPanel.setEnabled(false);

        mainConfigPanel.parse(configFile);
        walletPanel.parse(configFile);
        networkPanel.parse(configFile);
        matcherPanel.parse(configFile);
        restPanel.parse(configFile);

        tabPane.add("Main", mainConfigPanel);
        tabPane.add("Network", networkPanel);
        tabPane.add("Wallet", walletPanel);
        tabPane.add("Matcher", matcherPanel);
        tabPane.add("ReST API", restPanel);
        tabPane.add("Overview", overviewPanel);
        tabPane.add("Debug", debugPanel);

        mainPanel.setLayout(new BorderLayout());
        southPanel.setLayout(new FlowLayout());

        stopButton.setEnabled(false);

        startButton.setActionCommand("startNode");
        startButton.addActionListener(this);
        stopButton.setActionCommand("stopNode");
        stopButton.addActionListener(this);
        saveButton.setActionCommand("saveConfig");
        saveButton.addActionListener(this);

        southPanel.add(saveButton);
        southPanel.add(startButton);
        southPanel.add(stopButton);

        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.add(tabPane, BorderLayout.CENTER);

        return mainPanel;
    }

    public void actionPerformed(ActionEvent evt) {
        String actionCommand = evt.getActionCommand();

        if (actionCommand.equals("startNode")) {
            System.out.println ("starting node...");
            NodeRunner.INSTANCE.startNode();
            stopButton.setEnabled(true);
            startButton.setEnabled(false);
            overviewPanel.setButtonEnabled(true);
        } else if (actionCommand.equals("stopNode")) {
            System.out.println ("Stopping node!");
            NodeRunner.INSTANCE.stopNode();
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
            overviewPanel.setButtonEnabled(false);
        } else if (actionCommand.equals("saveConfig")) {
            PrintWriter out;

            try {
                out = new PrintWriter(new FileWriter("waves.conf"));

                out.println ("waves {");
                out.flush();

                mainConfigPanel.store(out);
                networkPanel.store(out);
                walletPanel.store(out);
                matcherPanel.store(out);
                restPanel.store(out);

                out.println("}");
                out.flush();

                out.close();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private LinkedList<String> parseConfigFile() {
        BufferedReader in;
        LinkedList<String> entries = new LinkedList<String>();
        String str;

        try {
            in = new BufferedReader(new FileReader("waves.conf"));

            while ((str = in.readLine()) != null) {
                entries.add(str);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entries;
    }

    public static void main(String[] args) {
        new WavesNodeUI();
    }

}
