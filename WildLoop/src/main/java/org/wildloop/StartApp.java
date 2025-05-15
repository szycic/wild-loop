package org.wildloop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartApp extends JFrame implements ActionListener {
    JPanel panel1;

    JButton button1;
    JButton button2;

    StartApp() {
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Wild-loop");
        this.setResizable(false);

        // panels
        panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(330, 800));
        panel1.setBackground(Color.GREEN);

        // starting button
        button1 = new JButton();
        button1.setPreferredSize(new Dimension(300, 60));
        button1.setFocusable(false);
        button1.setText("Rozpocznij symulacje");
        button1.addActionListener(this);

        // exit button
        button2 = new JButton();
        button2.setPreferredSize(new Dimension(300, 60));
        button2.setFocusable(false);
        button2.setText("Wyjście");
        button2.addActionListener(this);

        // adds
        this.add(panel1, BorderLayout.CENTER);
        panel1.add(button1);
        panel1.add(button2);

        // center window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = this.getSize().width;
        int frameHeight = this.getSize().height;
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;
        this.setLocation(x, y);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button1) {
            this.setVisible(false);
            //new Frame();
            System.exit(0);
        }
        if(e.getSource() == button2) {
            System.exit(0);
        }
    }
}
