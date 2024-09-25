/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.crudappgui;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class CRUDAppGUI {
    private JFrame frame;
    private JTextField nameField, numberField;
    private JTextArea outputArea;

    public CRUDAppGUI() {
        frame = new JFrame("CRUD App - Friends Contact");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 100, 30);
        frame.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(120, 20, 200, 30);
        frame.add(nameField);

        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setBounds(20, 60, 100, 30);
        frame.add(numberLabel);

        numberField = new JTextField();
        numberField.setBounds(120, 60, 200, 30);
        frame.add(numberField);

        JButton createButton = new JButton("Create");
        createButton.setBounds(20, 100, 100, 30);
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createContact();
            }
        });
        frame.add(createButton);

        JButton readButton = new JButton("Read");
        readButton.setBounds(130, 100, 100, 30);
        readButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readContacts();
            }
        });
        frame.add(readButton);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(240, 100, 100, 30);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateContact();
            }
        });
        frame.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(20, 140, 100, 30);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });
        frame.add(deleteButton);

        outputArea = new JTextArea();
        outputArea.setBounds(20, 180, 320, 150);
        frame.add(outputArea);

        frame.setVisible(true);
    }

    private void createContact() {
        String name = nameField.getText();
        String number = numberField.getText();

        if (name.isEmpty() || number.isEmpty()) {
            outputArea.setText("Please enter both name and number.");
            return;
        }

        try {
            File file = new File("friendsContact.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            boolean found = false;

            while (raf.getFilePointer() < raf.length()) {
                String nameNumberString = raf.readLine();
                String[] lineSplit = nameNumberString.split("!");
                String existingName = lineSplit[0];

                if (existingName.equals(name)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                raf.writeBytes(name + "!" + number);
                raf.writeBytes(System.lineSeparator());
                outputArea.setText("Contact added: " + name + " - " + number);
            } else {
                outputArea.setText("Contact already exists.");
            }

            raf.close();
        } catch (IOException e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private void readContacts() {
        try {
            File file = new File("friendsContact.txt");
            if (!file.exists()) {
                outputArea.setText("No contacts available.");
                return;
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            outputArea.setText("Contacts:\n");

            while (raf.getFilePointer() < raf.length()) {
                String nameNumberString = raf.readLine();
                String[] lineSplit = nameNumberString.split("!");
                String name = lineSplit[0];
                String number = lineSplit[1];
                outputArea.append("Name: " + name + ", Number: " + number + "\n");
            }

            raf.close();
        } catch (IOException e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private void updateContact() {
        String name = nameField.getText();
        String newNumber = numberField.getText();

        if (name.isEmpty() || newNumber.isEmpty()) {
            outputArea.setText("Please enter both name and new number.");
            return;
        }

        try {
            File file = new File("friendsContact.txt");
            if (!file.exists()) {
                outputArea.setText("No contacts available.");
                return;
            }

            File tmpFile = new File("temp.txt");
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            RandomAccessFile tmpraf = new RandomAccessFile(tmpFile, "rw");

            boolean found = false;
            while (raf.getFilePointer() < raf.length()) {
                String nameNumberString = raf.readLine();
                String[] lineSplit = nameNumberString.split("!");
                String existingName = lineSplit[0];
                String number = lineSplit[1];

                if (existingName.equals(name)) {
                    tmpraf.writeBytes(name + "!" + newNumber);
                    found = true;
                } else {
                    tmpraf.writeBytes(nameNumberString);
                }
                tmpraf.writeBytes(System.lineSeparator());
            }

            if (found) {
                raf.seek(0);
                tmpraf.seek(0);
                raf.setLength(0);

                while (tmpraf.getFilePointer() < tmpraf.length()) {
                    raf.writeBytes(tmpraf.readLine());
                    raf.writeBytes(System.lineSeparator());
                }

                outputArea.setText("Contact updated: " + name + " - " + newNumber);
            } else {
                outputArea.setText("Contact not found.");
            }

            raf.close();
            tmpraf.close();
            tmpFile.delete();
        } catch (IOException e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private void deleteContact() {
        String name = nameField.getText();

        if (name.isEmpty()) {
            outputArea.setText("Please enter the name.");
            return;
        }

        try {
            File file = new File("friendsContact.txt");
            if (!file.exists()) {
                outputArea.setText("No contacts available.");
                return;
            }

            File tmpFile = new File("temp.txt");
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            RandomAccessFile tmpraf = new RandomAccessFile(tmpFile, "rw");

            boolean found = false;
            while (raf.getFilePointer() < raf.length()) {
                String nameNumberString = raf.readLine();
                String[] lineSplit = nameNumberString.split("!");
                String existingName = lineSplit[0];

                if (existingName.equals(name)) {
                    found = true;
                } else {
                    tmpraf.writeBytes(nameNumberString);
                    tmpraf.writeBytes(System.lineSeparator());
                }
            }

            if (found) {
                raf.seek(0);
                tmpraf.seek(0);
                raf.setLength(0);

                while (tmpraf.getFilePointer() < tmpraf.length()) {
                    raf.writeBytes(tmpraf.readLine());
                    raf.writeBytes(System.lineSeparator());
                }

                outputArea.setText("Contact deleted: " + name);
            } else {
                outputArea.setText("Contact not found.");
            }

            raf.close();
            tmpraf.close();
            tmpFile.delete();
        } catch (IOException e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new CRUDAppGUI();
    }
}
