/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Panel;

import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import Objects.*;
import SQL.DatabaseCodes;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.MediaTracker;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author erens
 */
public class WriterFrame extends javax.swing.JFrame {

    /**
     * Creates new form WriterFrame
     */
    public WriterFrame(String mail) {
        initComponents();
        PublicMethods.generalLabel(labelNowUser);
        comboImage.addItem("Header Image  ");

        Writer useWriter = (Writer) PublicMethods.writerFinder(mail);
        listGenres.addElement(useWriter.getFavoriteGenre());
        for (int i = 0; i < useWriter.getGenres().size(); i++) {
            listGenres.addElement(useWriter.getGenres().get(i));
        }

        listGenre.setModel(listGenres);
        labels.add(labelCoverImage);

        scrollNewsArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    }
    private boolean editor = false;
    private News editorNews;

    public WriterFrame(String mail, News news) {
        initComponents();
        //Main frame component adder
        editorNews = news;

        ArrayList<JScrollPane> scrolls = new ArrayList<>();
        PublicMethods.generalLabel(labelNowUser);
        comboImage.addItem("Header Image  ");
        scrollNewsArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        editor = true;
        Writer useWriter = (Writer) PublicMethods.writerFinder(mail);

        txtAreaTitle.setText(editorNews.getTitle());
        FontMetrics metrics = txtAreaTitle.getFontMetrics(txtAreaTitle.getFont());

        int width = 458;

        int lines = (int) Math.ceil((double) metrics.stringWidth(editorNews.getTitle()) / width);

        int heightTitle = metrics.getHeight() * lines + 10;

        txtAreaTitle.setBounds(2, 0, width, heightTitle);
        txtAreaTitle.setPreferredSize(new Dimension(width, heightTitle));
        txtAreaTitle.revalidate();
        txtAreaTitle.repaint();

        for (int i = 0, j = 0; i < editorNews.getContent().size(); i++) {
            JTextArea textArea = new JTextArea("New Paragraph");
            textArea.setRows(5);
            textArea.setColumns(8);
            textAreas.add(textArea);

            Font font = new Font("Segoe UI Black", Font.BOLD, 14);
            textArea.setFont(font);

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            textArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    wordCount = 0;
                    for (JTextArea textArea : textAreas) {
                        String text = textArea.getText().trim();
                        if (!text.isEmpty()) {
                            String[] words = text.split("\\s+");
                            wordCount += words.length;

                        }
                    }
                    labelTextLength.setText(wordCount + "");

                }

            }
            );

            textArea.addFocusListener(
                    new FocusListener() {
                @Override
                public void focusGained(FocusEvent e
                ) {
                    if (textArea.getText().equals("New Paragraph")) {
                        textArea.setText("");
                    }
                }

                @Override
                public void focusLost(FocusEvent e
                ) {
                }
            });

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrolls.add(scrollPane);
            panelNewsArea.add(scrollPane,
                    new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, 452, 120));
            panelNewsArea.revalidate();
            panelNewsArea.repaint();

            y += 130;

            if (j < editorNews.getImages().size()) {

                JLabel newLabel = new JLabel("Image " + (labels.size() + 1));

                panelNewsArea.add(newLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 3, y, 445, 100));
                panelNewsArea.revalidate();
                panelNewsArea.repaint();

                y += 110;
                labels.add(newLabel);
                String labelName = "Image " + (labels.size());
                comboImage.addItem(labelName);

                j++;
            }
            newTextArea = true;

            btnNewVisualArea.setEnabled(
                    false);
            btnNewWriteArea.setEnabled(
                    true);

            panelNewsArea.setPreferredSize(
                    new Dimension(panelNewsArea.getWidth(), y + 130));

        }

        for (int i = 0; i < editorNews.getContent().size(); i++) {
            textAreas.get(i).setText(editorNews.getContent().get(i).toString());
        }

        Image scaledImageCover = editorNews.getHeaderImage().getImage().getScaledInstance(
                120,
                60,
                Image.SCALE_SMOOTH
        );
        labelCoverImage.setIcon(new ImageIcon(scaledImageCover));

        for (int i = 0; i < editorNews.getImages().size(); i++) {
            labels.get(i).setText("");
            ImageIcon imageIcon = editorNews.getImages().get(i);

            int labelWidth = 445;
            int labelHeight = 100;

            Image scaledImage = imageIcon.getImage().getScaledInstance(
                    labelWidth,
                    labelHeight,
                    Image.SCALE_SMOOTH
            );

            labels.get(i).setIcon(new ImageIcon(scaledImage));
            images.add(new ImageIcon(scaledImage));

        }

        listGenres.addElement(useWriter.getFavoriteGenre());
        for (int i = 0; i < useWriter.getGenres().size(); i++) {
            listGenres.addElement(useWriter.getGenres().get(i));
        }
        listGenre.setModel(listGenres);
        labels.add(labelCoverImage);

        ArrayList<Integer> selectedIndices = new ArrayList<>();

        for (String genres : news.getGenres()) {
            for (int i = 0; i < listGenres.getSize(); i++) {
                if (listGenres.get(i).equals(genres)) {
                    selectedIndices.add(i);
                }
            }
        }

        int[] indicesArray = selectedIndices.stream().mapToInt(Integer::intValue).toArray();
        listGenre.setSelectedIndices(indicesArray);

        int length = 0;
        for (StringBuilder sb : editorNews.getContent()) {
            String[] words = sb.toString().trim().split("\\s+");
            length += words.length;
        }
        labelTextLength.setText(length + "");

        for (JScrollPane scroll : scrolls) {
            SwingUtilities.invokeLater(() -> {
                scroll.getVerticalScrollBar().setValue(0);
            });
        }

        if (textAreas.size() + 1 - labels.size() >= 1) {
            btnNewVisualArea.setEnabled(true);
            btnNewWriteArea.setEnabled(false);
            newTextArea = false;

        }

    }

    DefaultListModel listGenres = new DefaultListModel();
    ArrayList<StringBuilder> paragraphs = new ArrayList<>();
    ArrayList<JTextArea> textAreas = new ArrayList<>();
    ArrayList<ImageIcon> images = new ArrayList<>();
    ImageIcon header;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnPublish = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        labelImageName = new javax.swing.JLabel();
        btnImage = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listGenre = new javax.swing.JList<>();
        scrollNewsArea = new javax.swing.JScrollPane();
        panelNewsArea = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaTitle = new javax.swing.JTextArea();
        btnNewVisualArea = new javax.swing.JButton();
        btnNewWriteArea = new javax.swing.JButton();
        comboImage = new javax.swing.JComboBox<>();
        labelTextLength = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        btnBack = new javax.swing.JButton();
        labelCoverImage = new javax.swing.JLabel();
        labelNowUser = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(710, 630));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPublish.setText("Publish");
        btnPublish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPublishActionPerformed(evt);
            }
        });
        getContentPane().add(btnPublish, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 470, 105, 89));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("News Area");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 2, 480, 70));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Genre");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 30, 119, 43));

        labelImageName.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        labelImageName.setText("Choose Image");
        getContentPane().add(labelImageName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 130, 43));

        btnImage.setText("Image");
        btnImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageActionPerformed(evt);
            }
        });
        getContentPane().add(btnImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 550, 126, -1));

        listGenre.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listGenre.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listGenreValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(listGenre);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(539, 80, 130, 360));

        panelNewsArea.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelNewsArea.setMinimumSize(new java.awt.Dimension(470, 60));
        panelNewsArea.setName(""); // NOI18N
        panelNewsArea.setPreferredSize(new java.awt.Dimension(470, 60));
        panelNewsArea.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtAreaTitle.setColumns(20);
        txtAreaTitle.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        txtAreaTitle.setLineWrap(true);
        txtAreaTitle.setRows(1);
        txtAreaTitle.setText("Title");
        txtAreaTitle.setWrapStyleWord(true);
        txtAreaTitle.setRequestFocusEnabled(false);
        txtAreaTitle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAreaTitleFocusGained(evt);
            }
        });
        txtAreaTitle.setRequestFocusEnabled(true);
        jScrollPane1.setViewportView(txtAreaTitle);

        panelNewsArea.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 450, 54));

        scrollNewsArea.getVerticalScrollBar().setPreferredSize(new java.awt.Dimension(8, Integer.MAX_VALUE));
        scrollNewsArea.getHorizontalScrollBar().setPreferredSize(new java.awt.Dimension(Integer.MAX_VALUE, 8));

        scrollNewsArea.setViewportView(panelNewsArea);

        getContentPane().add(scrollNewsArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 74, 480, 370));

        btnNewVisualArea.setText("New Visual Area");
        btnNewVisualArea.setEnabled(false);
        btnNewVisualArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewVisualAreaActionPerformed(evt);
            }
        });
        getContentPane().add(btnNewVisualArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 460, 120, 30));

        btnNewWriteArea.setText("New Write Area");
        btnNewWriteArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewWriteAreaActionPerformed(evt);
            }
        });
        getContentPane().add(btnNewWriteArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, 120, 30));

        comboImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboImageActionPerformed(evt);
            }
        });
        getContentPane().add(comboImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 510, -1, -1));

        labelTextLength.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        labelTextLength.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(labelTextLength, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 460, 150, 30));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 0, -1));

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Adsız.png"))); // NOI18N
        btnBack.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBack.setBorderPainted(false);
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 40));

        labelCoverImage.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCoverImage.setText("Cover Image Area");
        getContentPane().add(labelCoverImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 120, 60));

        labelNowUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(labelNowUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 560, 700, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPublishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPublishActionPerformed

        if (!editor) {
            boolean newsTitleExist = false;
            for (News news : News.getAllNews()) {
                if (news.getTitle().equals(txtAreaTitle.getText())) {
                    newsTitleExist = true;
                    break;
                }
            }
            if (listGenre.getSelectedIndex() != -1 && !newsTitleExist) {

                for (int i = 0; i < textAreas.size(); i++) {
                    StringBuilder newParagraph = new StringBuilder(textAreas.get(i).getText());
                    paragraphs.add(newParagraph);
                }
                ArrayList<String> genres = new ArrayList<>();
                int selected[] = listGenre.getSelectedIndices();

                for (int i = 0; i < selected.length; i++) {
                    genres.add(listGenres.getElementAt(selected[i]).toString());
                }

                LocalDate publishDate = LocalDate.now();

                if (labelCoverImage.getText().equals("Cover Image Area")) {

                    header = new ImageIcon("C:/Users/erens/OneDrive/Documents/NetBeansProjects/News/src/Images/NullCoverImage.png");

                }

                ArrayList<Comment> newsCommnet = new ArrayList<>();
                News newNews = new News(txtAreaTitle.getText(), paragraphs, genres, publishDate, 0, 0, images, header, newsCommnet);

                for (int i = 0; i < Writer.getAllWriters().size(); i++) {
                    Writer writer = (Writer) Writer.getAllWriters().get(i);
                    if (writer.getEmail().equals(MainFrame.getEmail())) {
                        writer.getNews().add(newNews);
                        DatabaseCodes.dbWriterNewsChanger(newNews, writer, true);
                        break;
                    }
                }

                News.addNews(newNews);

                WriterMainFrame wmf = new WriterMainFrame();
                wmf.setVisible(true);
                wmf.setLocationRelativeTo(this);

                this.dispose();
            } else {
                String message = "";
                if (listGenre.getSelectedIndex() == -1 && !newsTitleExist) {
                    message = "Choose genre.";
                } else if (listGenre.getSelectedIndex() != -1 && newsTitleExist) {
                    message = " This title is already in use.";

                } else {
                    message = "This title is already in use and choose genre";
                }
                JOptionPane.showConfirmDialog(
                        null,
                        message,
                        "ERROR",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } else {
            for (int i = 0; i < textAreas.size(); i++) {
                StringBuilder newParagraph = new StringBuilder(textAreas.get(i).getText());
                paragraphs.add(newParagraph);
            }
            ArrayList<String> genres = new ArrayList<>();
            int selected[] = listGenre.getSelectedIndices();

            for (int i = 0; i < selected.length; i++) {
                genres.add(listGenres.getElementAt(selected[i]).toString());
            }

            editorNews.setTitle(txtAreaTitle.getText());
            editorNews.setContent(paragraphs);
            editorNews.setGenres(genres);
            editorNews.setPublishDate(editorNews.getPublishDate());
            editorNews.setLike(0);
            editorNews.setDislike(0);
            editorNews.setImages(images);
            if (header == null) {
                header = editorNews.getHeaderImage();
            }
            editorNews.setHeaderImage(header);

            News.updateNews(editorNews, textAreas, listGenre, listGenres, txtAreaTitle, images,
                    editorNews.getPublishDate(), header);

            WriterMainFrame wmf = new WriterMainFrame();
            wmf.setVisible(true);
            wmf.setLocationRelativeTo(this);

            this.dispose();

        }


    }//GEN-LAST:event_btnPublishActionPerformed

    int x = 10;
    int y = 60;

    ArrayList<JLabel> labels = new ArrayList<>();

    boolean newTextArea = true;
    int wordCount = 0;
    private void btnNewWriteAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewWriteAreaActionPerformed
        if (newTextArea) {
            JTextArea textArea = new JTextArea("New Paragraph");
            textArea.setRows(5);
            textArea.setColumns(8);
            textAreas.add(textArea);

            Font font = new Font("Segoe UI Black", Font.BOLD, 14);
            textArea.setFont(font);

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            textArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    wordCount = 0;
                    for (JTextArea textArea : textAreas) {
                        String text = textArea.getText().trim();
                        if (!text.isEmpty()) {
                            String[] words = text.split("\\s+");
                            wordCount += words.length;

                        }
                    }
                    labelTextLength.setText(wordCount + "");

                }
            });
            textArea.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (textArea.getText().equals("New Paragraph")) {
                        textArea.setText("");
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                }
            });
            if (y == 60) {
                JScrollPane scrollPane = new JScrollPane(textArea);
                panelNewsArea.add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, 452, 120));
                panelNewsArea.revalidate();
                panelNewsArea.repaint();

                y += 130;

            } else {
                JScrollPane scrollPane = new JScrollPane(textArea);
                panelNewsArea.add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, 450, 120));
                panelNewsArea.revalidate();
                panelNewsArea.repaint();
                y += 130;

            }
            newTextArea = false;
            btnNewVisualArea.setEnabled(true);
            btnNewWriteArea.setEnabled(false);
        }
        labelTextLength.setText(wordCount + "");
        panelNewsArea.setPreferredSize(new Dimension(panelNewsArea.getWidth(), y + 130));
    }//GEN-LAST:event_btnNewWriteAreaActionPerformed

    private void btnNewVisualAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewVisualAreaActionPerformed
        if (!newTextArea) {
            JLabel newLabel = new JLabel("Image " + labels.size());

            panelNewsArea.add(newLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 3, y, 445, 100));
            panelNewsArea.revalidate();
            panelNewsArea.repaint();

            y += 110;
            labels.add(newLabel);
            String labelName = "Image " + (labels.size() - 1);
            comboImage.addItem(labelName);
            
            repaint();
            revalidate();
        }
        
        newTextArea = true;
        btnNewVisualArea.setEnabled(false);
        btnNewWriteArea.setEnabled(true);

        panelNewsArea.setPreferredSize(new Dimension(panelNewsArea.getWidth(), y + 130));

    }//GEN-LAST:event_btnNewVisualAreaActionPerformed

    private void comboImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboImageActionPerformed

    }//GEN-LAST:event_comboImageActionPerformed

    private void btnImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageActionPerformed
        int i = comboImage.getSelectedIndex();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a visual");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Resim Dosyaları", "jpg", "jpeg", "png", "gif", "bmp"
        ));

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
            Image scaledImage = imageIcon.getImage().getScaledInstance(
                    labels.get(i).getWidth(),
                    labels.get(i).getHeight(),
                    Image.SCALE_SMOOTH);
            if (i == 0) {
                header = new ImageIcon(scaledImage);
                labelCoverImage.setText("");
                labelCoverImage.setIcon(header);
                
            } else {
                
                if(editor){
                    i--;
                }

                labels.get(i).setText("");
                labels.get(i).setIcon(new ImageIcon(scaledImage));

                if (editor && i < images.size()) {
                    images.set(i, new ImageIcon(scaledImage));
                } else {
                    images.add(new ImageIcon(scaledImage));
                }
                
            }
            

        }
        
        repaint();
        revalidate();


    }//GEN-LAST:event_btnImageActionPerformed

    private void txtAreaTitleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAreaTitleFocusGained
        if (txtAreaTitle.getText().equals("Title"))
            txtAreaTitle.setText("");
    }//GEN-LAST:event_txtAreaTitleFocusGained

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed

        WriterMainFrame wmf = new WriterMainFrame();
        wmf.setVisible(true);
        wmf.setLocationRelativeTo(this);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void listGenreValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listGenreValueChanged

    }//GEN-LAST:event_listGenreValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WriterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WriterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WriterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WriterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WriterFrame("ali@gmail.com").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnImage;
    private javax.swing.JButton btnNewVisualArea;
    private javax.swing.JButton btnNewWriteArea;
    private javax.swing.JButton btnPublish;
    private javax.swing.JComboBox<String> comboImage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel labelCoverImage;
    private javax.swing.JLabel labelImageName;
    private javax.swing.JLabel labelNowUser;
    private javax.swing.JLabel labelTextLength;
    private javax.swing.JList<String> listGenre;
    private javax.swing.JPanel panelNewsArea;
    private javax.swing.JScrollPane scrollNewsArea;
    private javax.swing.JTextArea txtAreaTitle;
    // End of variables declaration//GEN-END:variables
}
