/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesUtiles;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 *
 * @author david
 */
public class FileBrowseApplet extends JApplet {

    private JButton button = new JButton("Browse");

    public void init() {
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(button);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(FileBrowseApplet.this);
            }
        });
    }
}
