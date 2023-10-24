package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EmlakKiraTakip {

    static Map<String, Integer> kiracilar = new HashMap<>();
    static Map<String, String> kiraTarihleri = new HashMap<>();

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:sqlserver://localhost:1433;databaseName=ETRADE2";
        String username = "SA";
        String password = "reallyStrongPwd123";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            if (connection.isClosed()) {
                System.out.println("Bağlantı kapalı durumda.");
            } else {
                System.out.println("Bağlantı açık durumda.");
            }

            String sqlQuery = "SELECT * FROM EmlakTakip ";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Veritabanı sonuçlarını burada işleyebilirsiniz
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Veritabanı bağlantısı veya sorgu hatası: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
    private static void ekleVeritabanina(Connection connection) throws SQLException {
       
            String insertQuery = "INSERT INTO EmlakTakip (KiraciAdiSoyadi, BinaIsmi, KiraBedeli, OdemeTarihi) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        
            for (String kiraci : kiracilar.keySet()) {
                int kiraBedeli = kiracilar.get(kiraci);
                String kiraTarihi = kiraTarihleri.get(kiraci);
        
                String[] kiraciBilgileri = kiraci.split(" - ");
                String kiraciAdiSoyadi = kiraciBilgileri[0];
                String binaIsmi = kiraciBilgileri[1];
        
                insertStatement.setString(0, kiraciAdiSoyadi);
                insertStatement.setString(1, binaIsmi);
                insertStatement.setInt(2, kiraBedeli);
                insertStatement.setString(3, kiraTarihi);
        
                int rowsAffected = insertStatement.executeUpdate();
        
                if (rowsAffected > 0) {
                    System.out.println("Veri başarıyla eklendi: " + kiraci);
                } else {
                    System.err.println("Veri eklenemedi: " + kiraci);
                }
            }
        
            insertStatement.close();
            connection.commit(); // Add this line to commit the transaction
        
    }

    // createAndShowGUI metodu ve kiraTarihiniBelirle metodu aynı şekilde devam eder...


    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Emlak Kira Takip");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 200));

        JButton kiraciEkleButton = new JButton("Kiracı Ekle");

        kiraciEkleButton.addActionListener(e -> {
            JDialog dialog = new JDialog(frame, "Kiracı Ekle", true);
            dialog.setLayout(new GridLayout(3, 3));

            JTextField isimSoyisimField = new JTextField();
            JTextField binaIsmiField = new JTextField();
            JTextField kiraBedeliField = new JTextField();
            JTextField odemeGunuField = new JTextField();

            dialog.add(new JLabel("Kiracı Adı Soyadı:"));
            dialog.add(isimSoyisimField);
            dialog.add(new JLabel("Bina İsmi:"));
            dialog.add(binaIsmiField);
            dialog.add(new JLabel("Kira Bedeli:"));
            dialog.add(kiraBedeliField);
            dialog.add(new JLabel("Ödeme Günü (1-30):"));
            dialog.add(odemeGunuField);

            JButton ekleButton = new JButton("Ekle");
            ekleButton.addActionListener(e1 -> {
                String isimSoyisim = isimSoyisimField.getText();
                String binaIsmi = binaIsmiField.getText();
                int kiraBedeli = Integer.parseInt(kiraBedeliField.getText());
                int odemeGunu = Integer.parseInt(odemeGunuField.getText());

                kiracilar.put(isimSoyisim + " - " + binaIsmi, kiraBedeli);
                kiraTarihleri.put(isimSoyisim + " - " + binaIsmi, kiraTarihiniBelirle(odemeGunu));

                JOptionPane.showMessageDialog(frame, isimSoyisim + " için kira bilgileri başarıyla eklendi.",
                        "Başarılı", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();

              
            });

            JButton iptalButton = new JButton("İptal");
            iptalButton.addActionListener(e1 -> {
                dialog.dispose();
            });

            dialog.add(ekleButton);
            dialog.add(iptalButton);
            dialog.pack();
            dialog.setVisible(true);
        });

        JButton kiraciBilgileriButton = new JButton("Kiracı Bilgilerini Göster");
        kiraciBilgileriButton.addActionListener(e -> {
            KiraciBilgileri kiraciBilgileri = new KiraciBilgileri();
            kiraciBilgileri.setVisible(true);
        });

        JButton emlakciKontroluButton = new JButton("Emlakçı Kontrolü");
        emlakciKontroluButton.addActionListener(e -> {
            EmlakciKontrolu emlakciKontrolu = new EmlakciKontrolu();
            emlakciKontrolu.setVisible(true);
        });

        JPanel panel = new JPanel();
        panel.add(kiraciEkleButton);
        panel.add(kiraciBilgileriButton);
        panel.add(emlakciKontroluButton);
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }

    private static String kiraTarihiniBelirle(int odemeGunu) {
        if (odemeGunu >= 1 && odemeGunu <= 10) {
            return "1-10";
        } else if (odemeGunu >= 11 && odemeGunu <= 20) {
            return "10-20";
        } else if (odemeGunu >= 21 && odemeGunu <= 30) {
            return "20-30";
        } else {
            return "Belirsiz";
        }
    }
}

