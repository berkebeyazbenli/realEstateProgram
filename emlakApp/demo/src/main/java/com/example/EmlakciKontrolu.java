package com.example;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EmlakciKontrolu extends JFrame {

        public EmlakciKontrolu() {
            setTitle("Emlakçı Kontrolü");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setPreferredSize(new Dimension(600, 400));
    
            // Kiracı bilgilerini göstermek için bir JTextArea oluşturun
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            add(scrollPane);
    
            // Kiracı bilgilerini textArea'ya ekleyin
            for (String kiraci : EmlakKiraTakip.kiracilar.keySet()) {
                int kiraBedeli = EmlakKiraTakip.kiracilar.get(kiraci);
                String kiraTarihi = EmlakKiraTakip.kiraTarihleri.get(kiraci);
                textArea.append("Kiracı: " + kiraci + " - Kira Bedeli: " + kiraBedeli + " TL - Ödeme Tarihi: " + kiraTarihi + "\n");
            }
    
            pack();
        }
    }
