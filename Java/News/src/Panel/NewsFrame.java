/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Panel;

import Objects.*;
import SQL.DatabaseCodes;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author erens
 */
public class NewsFrame extends javax.swing.JFrame {

    private ReaderFrame rf;
    private int deletedCount;

    /**
     * Creates new form NewsFrame
     */
    public NewsFrame(String title) {
        initComponents();
        PublicMethods.generalLabel(labelNowUser);

    }
    private static boolean adminRead = false;
    private static boolean writerRead = false;
    News readNews = new News();
    String cleanTitle = "";

    public NewsFrame(String title, ReaderFrame rf) {
        initComponents();
        PublicMethods.generalLabel(labelNowUser);
        this.rf = rf;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (adminRead) {
                    adminRead = false;
                }
                if (writerRead) {
                    writerRead = false;
                }

                dispose();

            }
        });

        scrollNewsArea.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        scrollNewsArea.getHorizontalScrollBar().setPreferredSize(new Dimension(Integer.MAX_VALUE, 8));
        scrollNewsArea.setHorizontalScrollBarPolicy(scrollNewsArea.HORIZONTAL_SCROLLBAR_NEVER);

        ArrayList<News> allNews = News.getAllNews();
        //Title cleaner and news area filler
        String boldPart = title.replaceAll("(?s).*?<b>(.*?)</b>.*", "$1");

        if (boldPart.contains("...")) {
            cleanTitle = boldPart.substring(0, boldPart.indexOf("..."));
        } else {
            cleanTitle = boldPart;
        }

        for (int i = 0; i < allNews.size(); i++) {
            if (allNews.get(i).getTitle().equals(cleanTitle)) {
                readNews = allNews.get(i);

                txtAreaTitle.setText(cleanTitle);
                FontMetrics metrics = txtAreaTitle.getFontMetrics(txtAreaTitle.getFont());

                int width = 458;

                int lines = (int) Math.ceil((double) metrics.stringWidth(cleanTitle) / width);

                int height = metrics.getHeight() * lines + 10;

                txtAreaTitle.setBounds(2, 0, width, height);
                txtAreaTitle.setPreferredSize(new Dimension(width, height));
                txtAreaTitle.revalidate();
                txtAreaTitle.repaint();

            }
        }

        int y = 60;

        for (int i = 0, j = 0; i < readNews.getContent().size(); i++, j++) {
            JTextArea newsText = new JTextArea(readNews.getContent().get(i).toString());

            Font font = new Font("Segoe UI Black", Font.BOLD, 14);
            newsText.setFont(font);
            newsText.setLineWrap(true);
            newsText.setWrapStyleWord(true);
            newsText.setEditable(false);
            newsText.setOpaque(false);
            newsText.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            int width = 458;

            newsText.setSize(width, Short.MAX_VALUE);
            int height = newsText.getPreferredSize().height;

            newsText.setBounds(0, y, width, height);
            panelNewsArea.add(newsText, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, y, width, height));
            y += height + 10;
            panelNewsArea.revalidate();
            panelNewsArea.repaint();

            if (j < readNews.getImages().size()) {
                JLabel newImageLabel = new JLabel();
                int imageHeight = 150;
                panelNewsArea.add(newImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, y, width, imageHeight));

                ImageIcon imageIcon = readNews.getImages().get(j);

                if (imageIcon.getIconHeight() > imageHeight || imageIcon.getIconWidth() > width) {
                    Image scaledImage = imageIcon.getImage().getScaledInstance(
                            width,
                            imageHeight,
                            Image.SCALE_SMOOTH
                    );
                    newImageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    newImageLabel.setIcon(imageIcon);
                }

                y += 160;
            }

        }

        ArrayList<Users> allWriters = Writer.getAllWriters();
        for (int i = 0; i < allWriters.size(); i++) {
            Writer titleWriter = (Writer) allWriters.get(i);

            for (int j = 0; j < titleWriter.getNews().size(); j++) {
                if (titleWriter.getNews().get(j).getNewsId() == (readNews.getNewsId())) {

                    labelWriterName.setText(titleWriter.getName() + " " + titleWriter.getSurname());

                    txtBiography.setText(titleWriter.getBiography());
                    scrollBiographyArea.getVerticalScrollBar().setPreferredSize(new Dimension(1, Integer.MAX_VALUE));

                    ImageIcon imageIcon = DatabaseCodes.byteArrayToImageIcon(titleWriter.getProfilPhoto());
                    if ((imageIcon.getIconHeight() > labelImageIcon.getBounds().height)
                            || (imageIcon.getIconWidth() > labelImageIcon.getBounds().width)) {
                        Image scaledImage = imageIcon.getImage().getScaledInstance(
                                labelImageIcon.getWidth(),
                                labelImageIcon.getHeight(),
                                Image.SCALE_SMOOTH
                        );
                        labelImageIcon.setText("");
                        labelImageIcon.setIcon(new ImageIcon(scaledImage));

                    } else {
                        labelImageIcon.setText("");
                        labelImageIcon.setIcon(imageIcon);

                    }

                }

            }
        }
        //Take scroll top side
        SwingUtilities.invokeLater(() -> {
            scrollNewsArea.getVerticalScrollBar().setValue(0);
            scrollBiographyArea.getVerticalScrollBar().setValue(0);
        });

        ArrayList<Users> allReaders = Reader.getAllReaders();

        for (int i = 0; i < allReaders.size(); i++) {
            if (allReaders.get(i).getEmail().equals(MainFrame.getEmail())) {
                nowReader = (Reader) allReaders.get(i);
                break;
            }

        }

        commentPlacer();

        labelLike.setText(readNews.getLike() + "");
        labelDislike.setText(readNews.getDislike() + "");

        if (adminRead) {
            btnPublish.setEnabled(false);
            btnLike.setEnabled(false);
            txtCommentWrite.setEnabled(false);
            btnDislike.setEnabled(false);
            btnAddFavorite.setEnabled(false);
        }

        if (!adminRead) {
            Writer writer = PublicMethods.writerFinderbyTitle(txtAreaTitle.getText());
            if (!nowReader.getFavoriteWriters().contains(writer)) {
                btnAddFavorite.setBackground(Color.BLACK);
            } else {
                btnAddFavorite.setBackground(Color.GREEN);

            }

            //News Like
            if (!nowReader.getLikedNews().contains(readNews)) {
                btnLike.setBackground(Color.WHITE);
            } else {
                btnLike.setBackground(Color.GREEN);

            }

            if (!nowReader.getDislikedNews().contains(readNews)) {
                btnDislike.setBackground(Color.WHITE);
            } else {
                btnDislike.setBackground(Color.RED);

            }

        }

        revalidate();
        repaint();
    }
    Reader nowReader = null;

    public JLabel getLabelImageIcon() {
        return labelImageIcon;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        labelNowUser = new javax.swing.JLabel();
        labelMain = new javax.swing.JLabel();
        labelImageIcon = new javax.swing.JLabel();
        scrollBiographyArea = new javax.swing.JScrollPane();
        txtBiography = new javax.swing.JTextArea();
        btnLike = new javax.swing.JButton();
        btnDislike = new javax.swing.JButton();
        labelLike = new javax.swing.JLabel();
        labelDislike = new javax.swing.JLabel();
        scrollNewsArea = new javax.swing.JScrollPane();
        panelNewsArea = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaTitle = new javax.swing.JTextArea();
        labelWriterName = new javax.swing.JLabel();
        btnAddFavorite = new javax.swing.JButton();
        scrollComment = new javax.swing.JScrollPane();
        panelCommentArea = new javax.swing.JPanel();
        btnPublish = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtCommentWrite = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(710, 630));
        setResizable(false);
        setSize(new java.awt.Dimension(800, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelNowUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(labelNowUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 580, 700, 30));

        labelMain.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        labelMain.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMain.setText("DolicHenus News");
        jPanel1.add(labelMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(-3, -4, 710, 70));
        jPanel1.add(labelImageIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 80, 120, 110));

        txtBiography.setEditable(false);
        txtBiography.setColumns(10);
        txtBiography.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        txtBiography.setLineWrap(true);
        txtBiography.setRows(5);
        txtBiography.setWrapStyleWord(true);
        txtBiography.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 3, true));
        txtBiography.setFocusable(false);
        scrollBiographyArea.setViewportView(txtBiography);

        jPanel1.add(scrollBiographyArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 200, 190, 270));

        btnLike.setText("👍");
        btnLike.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLikeActionPerformed(evt);
            }
        });
        jPanel1.add(btnLike, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 473, 50, 40));

        btnDislike.setText("👎");
        btnDislike.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDislikeActionPerformed(evt);
            }
        });
        jPanel1.add(btnDislike, new org.netbeans.lib.awtextra.AbsoluteConstraints(615, 473, 50, 40));

        labelLike.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        labelLike.setText("Like");
        jPanel1.add(labelLike, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 510, 30, -1));

        labelDislike.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        labelDislike.setText("Dislike");
        jPanel1.add(labelDislike, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 510, -1, -1));

        panelNewsArea.setMinimumSize(new java.awt.Dimension(470, 40));
        panelNewsArea.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelNewsArea.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 470, 40));

        txtAreaTitle.setEditable(false);
        txtAreaTitle.setColumns(20);
        txtAreaTitle.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        txtAreaTitle.setLineWrap(true);
        txtAreaTitle.setRows(1);
        txtAreaTitle.setWrapStyleWord(true);
        txtAreaTitle.setFocusable(false);
        txtAreaTitle.setOpaque(false);
        txtAreaTitle.setRequestFocusEnabled(false);
        txtAreaTitle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAreaTitleFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(txtAreaTitle);

        panelNewsArea.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 54));

        scrollNewsArea.setViewportView(panelNewsArea);

        jPanel1.add(scrollNewsArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 62, -1, 530));

        labelWriterName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        labelWriterName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelWriterName.setText("Writer Name");
        jPanel1.add(labelWriterName, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 210, 57));

        btnAddFavorite.setBackground(new java.awt.Color(0, 204, 204));
        btnAddFavorite.setText("Favourite");
        btnAddFavorite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFavoriteActionPerformed(evt);
            }
        });
        jPanel1.add(btnAddFavorite, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 540, 156, 47));

        panelCommentArea.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPublish.setText("Publish");
        btnPublish.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPublish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPublishActionPerformed(evt);
            }
        });
        panelCommentArea.add(btnPublish, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, -3, 80, 90));

        txtCommentWrite.setColumns(20);
        txtCommentWrite.setLineWrap(true);
        txtCommentWrite.setRows(3);
        txtCommentWrite.setTabSize(4);
        txtCommentWrite.setText("\n");
        txtCommentWrite.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCommentWriteFocusGained(evt);
            }
        });
        jScrollPane4.setViewportView(txtCommentWrite);

        panelCommentArea.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 610, 90));

        scrollComment.setViewportView(panelCommentArea);

        jPanel1.add(scrollComment, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 600, 700, 290));

        jScrollPane2.setViewportView(jPanel1);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, 620));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void commentPlacer() {
        // Comment adder
        ArrayList<Comment> allComments = readNews.getNewsComments();
        ArrayList<Users> allReaders = Reader.getAllReaders();
        int width = 600;
        int commentY = 70;

        Component[] components = panelCommentArea.getComponents();

        for (Component comp : components) {
            if (comp != btnPublish && comp != txtCommentWrite && comp != jScrollPane4) {
                panelCommentArea.remove(comp);
            }
        }

        for (int i = 0; i < allComments.size(); i++) {
            JTextArea newsComment = new JTextArea();

            newsComment.setEditable(false);
            Comment comment = allComments.get(i);
            int height = 60;
            String commentOwner = "";
            for (int j = 0; j < allReaders.size(); j++) {
                Reader reader = (Reader) allReaders.get(j);
                if (reader.getMyComments().contains(allComments.get(i))) {
                    commentOwner = reader.getName() + " " + reader.getSurname();
                    break;
                }
            }
            JLabel label = new JLabel(commentOwner);
            Font fontLabel = new Font("Segoe UI Black", Font.BOLD, 15);
            label.setFont(fontLabel);
            panelCommentArea.add(label, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, commentY + 20, width, height));

            //buttonEdit
            JButton buttonEdit = new JButton("Edit");
            buttonEdit.addActionListener(new ActionListener() {
                int a = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (a == 0) {
                        buttonEdit.setBackground(Color.BLUE);
                        newsComment.setEditable(true);
                        a++;
                    } else {
                        buttonEdit.setBackground(Color.WHITE);
                        comment.setComment(newsComment.getText());
                        Comment.editCommentFromFile(comment.getCommentId(), newsComment.getText());
                        newsComment.setEditable(false);
                        a--;

                    }

                }

            });

            commentY += 65;
            panelCommentArea.add(buttonEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, commentY, 80, 40));
            height = 80;
            //buttonDelete
            JButton buttonDelete = new JButton("Delete");
            buttonDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int id = comment.getCommentId();

                    for (Users user : Reader.getAllReaders()) {
                        Reader reader = (Reader) user;
                        News.updateNewsReaction(readNews.getNewsId(), "-comment", comment);
                        DatabaseCodes.dbCommentDeleter(id, reader.getEmail(), comment);
                    }
                    Comment.commentDeleter(id, readNews);
                    DatabaseCodes.callDeleter();

                    commentPlacer();
                }
            });

            panelCommentArea.add(buttonDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, commentY + 40, 80, 40));

            JButton buttonLike = new JButton("👍");
            JButton buttonDislike = new JButton("👎");
            JLabel labelDislikeC = new JLabel("");
            JLabel labelLikeC = new JLabel("");
            JButton buttonBanUser = new JButton("X");

            //buttonLike
            buttonLike.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int id = comment.getCommentId();
                    boolean alreadyLiked = false;
                    boolean alreadyDisliked = false;

                    for (Comment liked : nowReader.getLikedComment()) {
                        if (liked.getCommentId() == id) {
                            buttonLike.setBackground(Color.GREEN);
                            alreadyLiked = true;
                            break;
                        }
                    }

                    for (Comment disliked : nowReader.getDislikedComment()) {
                        if (disliked.getCommentId() == id) {
                            buttonDislike.setBackground(Color.RED);
                            alreadyDisliked = true;
                            break;
                        }
                    }

                    if (!alreadyLiked && !alreadyDisliked) {
                        comment.setLike(1);
                        Comment.likeCommentFromFile(comment.getCommentId(), true);
                        DatabaseCodes.arrayToDbLikedCommentId(comment.getCommentId(), nowReader.getEmail(), true);
                        buttonLike.setBackground(Color.GREEN);
                        nowReader.getLikedComment().add(comment);

                    } else if (!alreadyLiked && alreadyDisliked) {
                        comment.setLike(1);
                        comment.setDislike(-1);
                        DatabaseCodes.arrayToDbLikedCommentId(comment.getCommentId(), nowReader.getEmail(), true);
                        DatabaseCodes.arrayToDbDislikedCommentId(comment.getCommentId(), nowReader.getEmail(), false);

                        Comment.likeCommentFromFile(comment.getCommentId(), true);
                        Comment.dislikeCommentFromFile(comment.getCommentId(), false);
                        buttonLike.setBackground(Color.GREEN);
                        buttonDislike.setBackground(Color.WHITE);
                        nowReader.getLikedComment().add(comment);
                        nowReader.getDislikedComment().remove(comment);
                    } else if (!alreadyDisliked && alreadyLiked) {
                        comment.setLike(-1);
                        DatabaseCodes.arrayToDbLikedCommentId(comment.getCommentId(), nowReader.getEmail(), false);

                        Comment.likeCommentFromFile(comment.getCommentId(), false);
                        buttonLike.setBackground(Color.WHITE);
                        nowReader.getLikedComment().remove(comment);

                    }
                    labelDislikeC.setText(comment.getDislike() + "");
                    labelLikeC.setText(comment.getLike() + "");
                }
            });

            panelCommentArea.add(buttonLike, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, commentY + 80, 40, 40));
            //buttondisLike
            buttonDislike.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int id = comment.getCommentId();
                    boolean alreadyLiked = false;
                    boolean alreadyDisliked = false;

                    for (Comment liked : nowReader.getLikedComment()) {
                        if (liked.getCommentId() == id) {
                            alreadyLiked = true;
                            break;
                        }
                    }

                    for (Comment disliked : nowReader.getDislikedComment()) {
                        if (disliked.getCommentId() == id) {
                            alreadyDisliked = true;
                            break;
                        }
                    }

                    if (!alreadyDisliked && !alreadyLiked) {
                        comment.setDislike(1);
                        DatabaseCodes.arrayToDbDislikedCommentId(comment.getCommentId(), nowReader.getEmail(), true);

                        Comment.dislikeCommentFromFile(comment.getCommentId(), true);
                        nowReader.getDislikedComment().add(comment);
                        buttonDislike.setBackground(Color.RED);

                    } else if (!alreadyDisliked && alreadyLiked) {
                        comment.setDislike(1);
                        comment.setLike(-1);

                        DatabaseCodes.arrayToDbDislikedCommentId(comment.getCommentId(), nowReader.getEmail(), true);
                        DatabaseCodes.arrayToDbLikedCommentId(comment.getCommentId(), nowReader.getEmail(), false);

                        Comment.likeCommentFromFile(comment.getCommentId(), false);
                        Comment.dislikeCommentFromFile(comment.getCommentId(), true);
                        buttonDislike.setBackground(Color.RED);
                        buttonLike.setBackground(Color.WHITE);
                        nowReader.getDislikedComment().add(comment);
                        nowReader.getLikedComment().remove(comment);

                    } else if (alreadyDisliked && !alreadyLiked) {
                        comment.setDislike(-1);
                        DatabaseCodes.arrayToDbDislikedCommentId(comment.getCommentId(), nowReader.getEmail(), false);
                        Comment.dislikeCommentFromFile(comment.getCommentId(), false);
                        nowReader.getDislikedComment().remove(comment);
                        buttonDislike.setBackground(Color.WHITE);

                    }

                    labelDislikeC.setText(comment.getDislike() + "");
                    labelLikeC.setText(comment.getLike() + "");

                }

            });

            panelCommentArea.add(buttonDislike, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, commentY + 80, 40, 40));

            Font fontLabelLD = new Font("Segoe UI Black", Font.BOLD, 10);
            labelDislikeC.setFont(fontLabelLD);
            labelDislikeC.setText("0");
            panelCommentArea.add(labelDislikeC, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, commentY + 110, 20, 30));

            labelLikeC.setFont(fontLabelLD);
            labelLikeC.setText("0");
            panelCommentArea.add(labelLikeC, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, commentY + 110, 20, 30));

            //buttonBan
            buttonBanUser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int id = comment.getCommentId();

                    Users userToRemove = null;
                    Comment commentToRemove = null;

                    for (Users user : Reader.getAllReaders()) {
                        Reader reader = (Reader) user;
                        for (Comment c : reader.getMyComments()) {
                            if (id == c.getCommentId()) {
                                userToRemove = user;
                                commentToRemove = c;
                                break;
                            }
                        }
                        if (userToRemove != null) {
                            break;
                        }
                    }

                    if (userToRemove != null && commentToRemove != null) {

                        Reader reader = (Reader) userToRemove;

                        ArrayList<Comment> myComments = new ArrayList<>(reader.getMyComments());
                        for (Comment myComment : myComments) {
                            Comment.deleteCommentFromFile(myComment.getCommentId());
                            News.updateNewsReaction(readNews.getNewsId(), "-comment", myComment);
                        }

                        ArrayList<Comment> likedComments = new ArrayList<>(reader.getLikedComment());
                        for (Comment likedComment : likedComments) {
                            Comment.likeCommentFromFile(likedComment.getCommentId(), false);
                            News.updateNewsReaction(readNews.getNewsId(), "-comment", likedComment);
                            likedComment.setLike(-1);
                        }

                        ArrayList<Comment> dislikedComments = new ArrayList<>(reader.getDislikedComment());
                        for (Comment dislikedComment : dislikedComments) {
                            Comment.dislikeCommentFromFile(dislikedComment.getCommentId(), false);
                            News.updateNewsReaction(readNews.getNewsId(), "-comment", dislikedComment);
                            dislikedComment.setDislike(-1);
                        }

                        for (Users user : Reader.getAllReaders()) {
                            if (user != userToRemove) {
                                Reader otherReader = (Reader) user;

                                for (Comment myComment : myComments) {
                                    if (otherReader.getLikedComment().contains(myComment)) {
                                        otherReader.getLikedComment().remove(myComment);
                                        DatabaseCodes.arrayToDbLikedCommentId(myComment.getCommentId(), otherReader.getEmail(), false);
                                    }

                                    if (otherReader.getDislikedComment().contains(myComment)) {
                                        otherReader.getDislikedComment().remove(myComment);
                                        DatabaseCodes.arrayToDbDislikedCommentId(myComment.getCommentId(), otherReader.getEmail(), false);
                                    }
                                }
                            }
                        }

                        if (reader.getLikedNews().contains(readNews)) {
                            News.updateNewsReaction(readNews.getNewsId(), "-like", null);
                            labelLike.setText(readNews.getLike() + "");
                        }
                        if (reader.getDislikedNews().contains(readNews)) {
                            News.updateNewsReaction(readNews.getNewsId(), "-dislike", null);
                            labelDislike.setText(readNews.getDislike() + "");
                        }

                        for (Users users : Writer.getAllWriters()) {
                            Writer writer = (Writer) users;
                            if (reader.getFavoriteWriters().contains(writer)) {
                                DatabaseCodes.favoriteWriterToReaderDb(writer, reader, false);
                            }
                        }

                        DatabaseCodes.dbReaderDelete(reader);

                        Reader.getAllReaders().remove(userToRemove);
                        Users.getAllUsers().remove(userToRemove);

                        Comment.getAllComments().removeAll(myComments);

                        DatabaseCodes.callDeleter();

                        commentPlacer();

                    }
                }
            });
            Font fontBan = new Font("Segoe UI Black", Font.BOLD, 14);
            buttonBanUser.setFont(fontBan);
            buttonBanUser.setForeground(Color.WHITE);
            buttonBanUser.setBackground(Color.RED);
            panelCommentArea.add(buttonBanUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(565, commentY + 80, 40, 40));

            //Text Area
            String commentStr = comment.getComment();
            newsComment.setText(commentStr);
            Font fontTextArea = new Font("Segoe UI Black", Font.BOLD, 14);
            newsComment.setFont(fontTextArea);
            newsComment.setLineWrap(true);
            newsComment.setWrapStyleWord(true);

            if (!adminRead && !writerRead) {

                if (nowReader.getMyComments().contains(allComments.get(i))) {
                    buttonEdit.setVisible(true);
                    buttonDelete.setVisible(true);
                    buttonBanUser.setVisible(false);

                } else {

                    buttonEdit.setVisible(false);
                    buttonDelete.setVisible(false);
                    buttonBanUser.setVisible(false);
                }

                if (nowReader.getLikedComment().contains(comment)) {
                    buttonLike.setBackground(Color.GREEN);
                } else {
                    buttonLike.setBackground(Color.WHITE);
                }

                if (nowReader.getDislikedComment().contains(comment)) {

                    buttonDislike.setBackground(Color.RED);
                } else {
                    buttonDislike.setBackground(Color.WHITE);
                }

            } else {
                buttonBanUser.setVisible(true);
                buttonLike.setEnabled(false);
                buttonDislike.setEnabled(false);
                buttonEdit.setEnabled(false);
                buttonDelete.setVisible(true);
                btnPublish.setEnabled(false);
                txtCommentWrite.setEnabled(false);

                if (writerRead) {
                    buttonBanUser.setVisible(false);
                }

            }

            JScrollPane scrollPane = new JScrollPane(newsComment);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);

            panelCommentArea.add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, commentY, width, height));
            commentY += height + 40;

            labelLike.setText(readNews.getLike() + "");
            labelDislike.setText(readNews.getDislike() + "");
            labelLikeC.setText(comment.getLike() + "");
            labelDislikeC.setText(comment.getDislike() + "");

        }

        panelCommentArea.setPreferredSize(new Dimension(width, commentY + 20));
        panelCommentArea.repaint();
        panelCommentArea.revalidate();

    }

    private void txtAreaTitleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAreaTitleFocusGained
        if (txtAreaTitle.getText().equals("Title"))
            txtAreaTitle.setText("");
    }//GEN-LAST:event_txtAreaTitleFocusGained

    private void btnLikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLikeActionPerformed

        boolean alreadyLiked = false;
        boolean alreadyDisliked = false;

        for (News liked : nowReader.getLikedNews()) {
            if (liked.getTitle().equals(cleanTitle)) {
                alreadyLiked = true;
                break;
            }
        }

        for (News disliked : nowReader.getDislikedNews()) {
            if (disliked.getTitle().equals(cleanTitle)) {
                alreadyDisliked = true;
                break;
            }
        }

        if (!alreadyLiked && !alreadyDisliked) {

            DatabaseCodes.likedNewsToReaderDb(readNews, nowReader, true);
            News.updateNewsReaction(readNews.getNewsId(), "+like", null);
            btnLike.setBackground(Color.GREEN);
            nowReader.getLikedNews().add(readNews);

            rf.listWriter(rf.getFilterGenre(), rf.getFilterWriter(), rf.getListModel());
        } else if (!alreadyLiked && alreadyDisliked) {

            News.updateNewsReaction(readNews.getNewsId(), "+like", null);
            News.updateNewsReaction(readNews.getNewsId(), "-dislike", null);
            DatabaseCodes.likedNewsToReaderDb(readNews, nowReader, true);
            DatabaseCodes.dislikedNewsToReaderDb(readNews, nowReader, false);
            btnLike.setBackground(Color.GREEN);
            btnDislike.setBackground(Color.WHITE);
            nowReader.getLikedNews().add(readNews);
            nowReader.getDislikedNews().remove(readNews);
            rf.listWriter(rf.getFilterGenre(), rf.getFilterWriter(), rf.getListModel());
        } else if (!alreadyDisliked && alreadyLiked) {

            News.updateNewsReaction(readNews.getNewsId(), "-like", null);

            DatabaseCodes.likedNewsToReaderDb(readNews, nowReader, false);
            btnLike.setBackground(Color.WHITE);
            nowReader.getLikedNews().remove(readNews);
            rf.listWriter(rf.getFilterGenre(), rf.getFilterWriter(), rf.getListModel());
        }

        labelLike.setText(readNews.getLike() + "");
        labelDislike.setText(readNews.getDislike() + "");

        rf.likeNewsTableWriter();
        rf.dislikeNewsTableWriter();


    }//GEN-LAST:event_btnLikeActionPerformed

    private void btnDislikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDislikeActionPerformed

        boolean alreadyDisliked = false;
        boolean alreadyLiked = false;

        for (News disliked : nowReader.getDislikedNews()) {
            if (disliked.getTitle().equals(cleanTitle)) {
                alreadyDisliked = true;
                break;
            }
        }

        for (News liked : nowReader.getLikedNews()) {
            if (liked.getTitle().equals(cleanTitle)) {
                alreadyLiked = true;
                break;
            }
        }

        if (!alreadyDisliked && !alreadyLiked) {

            News.updateNewsReaction(readNews.getNewsId(), "+dislike", null);
            DatabaseCodes.dislikedNewsToReaderDb(readNews, nowReader, true);
            nowReader.getDislikedNews().add(readNews);
            btnDislike.setBackground(Color.RED);
            rf.listWriter(rf.getFilterGenre(), rf.getFilterWriter(), rf.getListModel());
        } else if (!alreadyDisliked && alreadyLiked) {

            News.updateNewsReaction(readNews.getNewsId(), "+dislike", null);
            News.updateNewsReaction(readNews.getNewsId(), "-like", null);
            DatabaseCodes.dislikedNewsToReaderDb(readNews, nowReader, true);
            DatabaseCodes.likedNewsToReaderDb(readNews, nowReader, false);
            btnDislike.setBackground(Color.RED);
            btnLike.setBackground(Color.WHITE);
            nowReader.getDislikedNews().add(readNews);
            nowReader.getLikedNews().remove(readNews);
            rf.listWriter(rf.getFilterGenre(), rf.getFilterWriter(), rf.getListModel());
        } else if (alreadyDisliked && !alreadyLiked) {

            News.updateNewsReaction(readNews.getNewsId(), "-dislike", null);
            DatabaseCodes.dislikedNewsToReaderDb(readNews, nowReader, false);
            nowReader.getDislikedNews().remove(readNews);
            btnDislike.setBackground(Color.WHITE);
            rf.listWriter(rf.getFilterGenre(), rf.getFilterWriter(), rf.getListModel());
        }

        labelLike.setText(readNews.getLike() + "");
        labelDislike.setText(readNews.getDislike() + "");
        rf.dislikeNewsTableWriter();
        rf.likeNewsTableWriter();
    }//GEN-LAST:event_btnDislikeActionPerformed

    private void btnAddFavoriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFavoriteActionPerformed

        if (MainFrame.getEmail().equals(nowReader.getEmail())) {
            Writer writer = PublicMethods.writerFinderbyTitle(txtAreaTitle.getText());
            if (!nowReader.getFavoriteWriters().contains(writer)) {
                nowReader.getFavoriteWriters().add(writer);
                btnAddFavorite.setBackground(Color.GREEN);
                DatabaseCodes.favoriteWriterToReaderDb(writer, nowReader, true);
            } else {
                nowReader.getFavoriteWriters().removeIf(w -> w.getEmail().equals(writer.getEmail()));
                btnAddFavorite.setBackground(Color.BLACK);
                DatabaseCodes.favoriteWriterToReaderDb(writer, nowReader, false);
            }
            btnAddFavorite.revalidate();
            btnAddFavorite.repaint();
        }

        rf.favWriterTableWriter();

    }//GEN-LAST:event_btnAddFavoriteActionPerformed

    private void txtCommentWriteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCommentWriteFocusGained

    }//GEN-LAST:event_txtCommentWriteFocusGained

    private void btnPublishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPublishActionPerformed
        if (!txtCommentWrite.getText().trim().isEmpty()) {
            String userComment = txtCommentWrite.getText().replaceAll("-", " ");
            if (!userComment.replaceAll("\\s+", "").isEmpty()) {
                Comment c1 = new Comment(0, 0, userComment, false, nowReader.getEmail(), 0, cleanTitle);
                DatabaseCodes.commentToDbOwner(nowReader.getEmail(), c1.getCommentId());

                readNews.getNewsComments().add(0, c1);
                nowReader.getMyComments().add(0, c1);

                Component[] components = panelCommentArea.getComponents();

                for (Component comp : components) {
                    if (comp != btnPublish && comp != txtCommentWrite && comp != jScrollPane4) {
                        panelCommentArea.remove(comp);
                    }
                }
            }

            panelCommentArea.revalidate();
            panelCommentArea.repaint();
            txtCommentWrite.setText("");
            commentPlacer();
        } else {
            //pass
        }


    }//GEN-LAST:event_btnPublishActionPerformed

    public static boolean isAdminRead() {
        return adminRead;
    }

    public static void setAdminRead(boolean adminRead) {
        NewsFrame.adminRead = adminRead;
    }

    public static boolean isWriterRead() {
        return writerRead;
    }

    public static void setWriterRead(boolean writerRead) {
        NewsFrame.writerRead = writerRead;
    }

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
            java.util.logging.Logger.getLogger(NewsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewsFrame("Title").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddFavorite;
    private javax.swing.JButton btnDislike;
    private javax.swing.JButton btnLike;
    private javax.swing.JButton btnPublish;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelDislike;
    private javax.swing.JLabel labelImageIcon;
    private javax.swing.JLabel labelLike;
    private javax.swing.JLabel labelMain;
    private javax.swing.JLabel labelNowUser;
    private javax.swing.JLabel labelWriterName;
    private javax.swing.JPanel panelCommentArea;
    private javax.swing.JPanel panelNewsArea;
    private javax.swing.JScrollPane scrollBiographyArea;
    private javax.swing.JScrollPane scrollComment;
    private javax.swing.JScrollPane scrollNewsArea;
    private javax.swing.JTextArea txtAreaTitle;
    private javax.swing.JTextArea txtBiography;
    private javax.swing.JTextArea txtCommentWrite;
    // End of variables declaration//GEN-END:variables
}
