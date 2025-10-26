/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

import Objects.*;
import SQL.DatabaseCodes;
import SQL.Readerdb;
import SQL.Usersdb;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayOutputStream;

import javax.swing.JOptionPane;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author erens
 */
public class PublicMethods {

    //It will check our objects main parts.
    public static ArrayList<String> sıgnInController(String name, String surname, String email, String password,
            ArrayList<String> genres, boolean isReader, String gender, String favoriteGenre, String biography,
            byte[] profilPhoto, boolean addAdmin, String permissionLevel) {

        ArrayList<Users> allUsers = Users.getAllUsers();
        ArrayList<String> errors = new ArrayList();

        if (name == null || name.isEmpty()) {
            errors.add("name");
        }
        if (surname == null || surname.isEmpty()) {
            errors.add("surname");
        }
        if ((password == null || password.isEmpty())) {
            errors.add("password");
        } else if (password.length() < 8) {
            errors.add("passwordSize");
        }
        if (!addAdmin) {
            if (genres == null || genres.isEmpty()) {
                errors.add("genres");
            }

        }
        if (gender == "") {
            errors.add("gender");
        }

        if (profilPhoto == null || DatabaseCodes.byteArrayToImageIcon(profilPhoto).getImage() == null || DatabaseCodes.byteArrayToImageIcon(profilPhoto).getIconWidth() == -1) {
            errors.add(("image"));
        }

        ArrayList<News> listNews = new ArrayList<>();

        if ((email != null || !email.isEmpty()) && email.contains("@")
                && email.indexOf(".com") > email.indexOf("@")) {

            boolean emailExists = false;

            for (int i = 0; i < allUsers.size(); i++) {
                Users checkUser = allUsers.get(i);

                if (email.equals(checkUser.getEmail())) {
                    emailExists = true;
                    errors.add("emailExist");
                    break;
                }
            }

            if (errors.size() == 0) {

                if (!emailExists) {
                    if (isReader) {

                        Users newUser = new Reader(name, surname, password, email, genres, new ArrayList<News>(), gender, new ArrayList<Writer>(), new ArrayList<News>(), new ArrayList<News>(), profilPhoto, new ArrayList<Comment>(),
                                new ArrayList<Comment>(), new ArrayList<Comment>());

                        Reader reader = (Reader) newUser;

                        DatabaseCodes.dbReaderAdder(name, surname, email, password, genres, gender, profilPhoto, reader.getFavoriteGenres(), reader.getFavoriteWriters(), reader.getLikedNews(),
                                reader.getDislikedNews(), reader.getMyComments(), reader.getLikedComment(), reader.getDislikedComment());
                    } else if (!isReader && !addAdmin) {

                        Users newWriter = new Writer(name, surname, password, email, listNews, gender, genres, favoriteGenre, biography, profilPhoto);

                        DatabaseCodes.dbWriterAdder(name, surname, password, email, listNews, gender, genres, favoriteGenre, biography, profilPhoto);
                    } else if (addAdmin) {

                        Users newAdmin = new Admin(name, surname, password, email, gender, profilPhoto, permissionLevel);
                        
                        DatabaseCodes.dbAdminAdder(name, surname, password, email, gender, profilPhoto, permissionLevel);
                    }
                }
            }

        } else {
            boolean emailExist = false;
            for (int i = 0; i < allUsers.size(); i++) {
                Users checkUser = allUsers.get(i);

                if (email.equals(checkUser.getEmail())) {
                    emailExist = true;
                    errors.add("emailExist");
                    break;
                }
            }
            if (!emailExist) {
                errors.add("email");
            }
        }
        if (errors.size() > 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Check wrong fields",
                    "WARNING",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        return errors;
    }

    //It will check which object trying to enter.
    public static String logInController(String email, String password) {
        ArrayList<Users> allUsers = Users.getAllUsers();
        boolean emailFound = false;

        if (email.trim().isEmpty() || password.trim().isEmpty()) {

            return "wrongInputs";

        }

        for (Users checkUser : allUsers) {
            if (email.equals(checkUser.getEmail())) {
                emailFound = true;
                if (password.equals(checkUser.getPassword())) {
                    if (checkUser instanceof Writer) {
                        return "writer";
                    }
                    if (checkUser instanceof Admin) {
                        return "admin";
                    }
                    if (checkUser instanceof Reader) {
                        return "reader";
                    }
                } else {
                    return "wrongInputs";
                }
            }
        }

        if (!emailFound) {
            return "noPeople";
        }

        return "wrongInput";
    }

    //Will find writer
    public static Users writerFinder(String mail) {

        ArrayList<Users> allWriters = Writer.getAllWriters();

        for (int i = 0; i < allWriters.size(); i++) {
            if (allWriters.get(i).getEmail().equals(mail)) {
                return allWriters.get(i);
            }
        }
        return null;
    }

    //Update list 
    public static void showNewsInList(JList<String> list, DefaultListModel<String> model) {

        list.setModel(model);
    }

    //Draw list 
    public static void populateNewsList(JList<String> listNews, ArrayList<News> allNews, DefaultListModel listModel) {
        NewsListCellRenderer renderer = new NewsListCellRenderer();
        listNews.setCellRenderer(renderer);

        for (int i = 0; i < allNews.size(); i++) {

            String writerName = findWriterName(allNews.get(i));

            LocalDate date = allNews.get(i).getPublishDate();
            String formattedDate = date.getDayOfMonth() + "." + date.getMonthValue() + "." + date.getYear();

            String fullTitle = allNews.get(i).getTitle();

            String fullContent = "";

            if (allNews.get(i).getContent() != null && !allNews.get(i).getContent().isEmpty()) {
                String contentText = allNews.get(i).getContent().get(0).toString();
                if (contentText != null && !contentText.isEmpty()) {
                    fullContent = contentText;
                }
            }
            String summary = shortenText(fullContent, 9);

            String combined = "<html><b>" + fullTitle
                    + "</b><br>" + summary
                    + "<br><i>Author: " + writerName + "</i>"
                    + "<br><i>Date: " + formattedDate
                    + "<br><i>👍 " + allNews.get(i).getLike()
                    + " | 👎 " + allNews.get(i).getDislike() + "</i></html>";

            listModel.addElement(combined);

            ImageIcon headerImage = allNews.get(i).getHeaderImage();

            renderer.addImage(i, headerImage);
        }

        showNewsInList(listNews, listModel);
    }

    //Find writer name via news
    private static String findWriterName(News news) {
        ArrayList<Users> allWriters = Writer.getAllWriters();

        for (int i = 0; i < allWriters.size(); i++) {
            Writer writer = (Writer) allWriters.get(i);

            for (int j = 0; j < writer.getNews().size(); j++) {

                if (writer.getNews().get(j).getTitle().equals(news.getTitle().trim())) {

                    return writer.getName() + " " + writer.getSurname();
                }
            }
        }

        return "Unknown";
    }

    //remove long parts
    private static String shortenText(String text, int wordLimit) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        if (text.split("\\s+").length <= wordLimit) {
            return text;
        }

        int wordCount = 0;
        int endIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            if (Character.isWhitespace(text.charAt(i))) {
                wordCount++;
                if (wordCount == wordLimit) {
                    endIndex = i;
                    break;
                }
            }
        }

        if (endIndex == 0) {
            return text;
        }

        return text.substring(0, endIndex) + "...";
    }

    //Inner Class for making adjustment for list
    public static class NewsListCellRenderer extends DefaultListCellRenderer {
        //It will prevent errors from images.

        private Map<Integer, ImageIcon> imageMap = new HashMap<>();

        public void addImage(int index, ImageIcon image) {
            imageMap.put(index, resizeIcon(image, 125, 125));
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JPanel panel = new JPanel(new BorderLayout(10, 0));

            JLabel textLabel = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            textLabel.setPreferredSize(new Dimension(280, 125));
            textLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.LIGHT_GRAY));

            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            imageLabel.setVerticalAlignment(SwingConstants.TOP);
            imageLabel.setPreferredSize(new Dimension(125, 125));
            imageLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.LIGHT_GRAY));

            ImageIcon icon = imageMap.get(index);
            if (icon != null) {
                imageLabel.setIcon(icon);
            }

            panel.add(textLabel, BorderLayout.CENTER);
            panel.add(imageLabel, BorderLayout.EAST);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            if (isSelected) {
                panel.setBackground(list.getSelectionBackground());
            } else {
                panel.setBackground(list.getBackground());
            }

            return panel;
        }

        private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
            if (icon == null) {
                return null;
            }
            Image img = icon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        }
    }

    //Filtre appleyer for news
    public static ArrayList<News> filtreApplyerForNews(String filtreGenre, String filtreWriter) {
        ArrayList<News> filteredNews = new ArrayList<>();

        if (filtreGenre.equals("All") && filtreWriter.equals("All")) {
            return News.getAllNews();

        } else if (!filtreGenre.equals("All") && filtreWriter.equals("All")) {

            for (News news : News.getAllNews()) {
                if (news.getGenres().contains(filtreGenre)) {
                    filteredNews.add(news);
                }
            }
        } else if (filtreGenre.equals("All") && !filtreWriter.equals("All")) {
            for (Users writer : Writer.getAllWriters()) {
                Writer useWriter = (Writer) writer;
                if (isWriterMatching(useWriter, filtreWriter)) {
                    for (News news : useWriter.getNews()) {
                        filteredNews.add(news);
                    }
                }
            }
        } else {
            for (Users writer : Writer.getAllWriters()) {
                Writer useWriter = (Writer) writer;
                if (isWriterMatching(useWriter, filtreWriter)) {
                    for (News news : useWriter.getNews()) {
                        if (news.getGenres().contains(filtreGenre)) {
                            filteredNews.add(news);
                        }
                    }
                }
            }
        }

        return filteredNews;
    }

    //Check writer
    private static boolean isWriterMatching(Writer writer, String filtreWriter) {

        int spaceIndex = filtreWriter.lastIndexOf(" ");

        if (spaceIndex == -1) {
            return false;
        }

        String name = filtreWriter.substring(0, spaceIndex);

        String surname = filtreWriter.substring(spaceIndex + 1);

        return writer.getName().equals(name) && writer.getSurname().equals(surname);
    }

    //Find writer with title
    public static Writer writerFinderbyTitle(String title) {
        ArrayList<Users> allWriters = Writer.getAllWriters();
        for (Users user : allWriters) {
            Writer writer = (Writer) user;

            for (News news : writer.getNews()) {
                if (news.getTitle().equals(title)) {
                    return writer;
                }
            }

        }

        return null;
    }

    //Table writer for news with filtre
    public static void tableWriterForNews(JTable table, ArrayList<News> allNews, DefaultTableModel tableO) {
        tableO.setRowCount(0);
        table.revalidate();
        table.repaint();
        if (!News.getAllNews().isEmpty()) {
            for (News news : allNews) {
                Writer writer = PublicMethods.writerFinderbyTitle(news.getTitle());
                tableO.addRow(new Object[]{news.getTitle(), news.getHeaderImage(), news.getLike(), news.getDislike(),
                    news.getGenres(), news.getPublishDate(), writer.getName() + " " + writer.getSurname()

                });
            }
        }

        table.setModel(tableO);

    }

    //Table writer for writer with filtre
    public static void tableWriterForWriter(JTable table, ArrayList<Users> allWriter, DefaultTableModel tableO,
            String filterGenre) {
        tableO.setRowCount(0);

        if (!Writer.getAllWriters().isEmpty()) {
            for (Users writers : allWriter) {
                Writer writer = (Writer) writers;
                int like = 0;
                int dislike = 0;
                int favCount = 0;
                if (writer != null && !writer.getNews().isEmpty()) {
                    for (int j = 0; j < writer.getNews().size(); j++) {
                        if (!filterGenre.equals("All")) {
                            if (writer.getNews().get(j).getGenres().contains(filterGenre)) {
                                like += writer.getNews().get(j).getLike();
                                dislike += writer.getNews().get(j).getDislike();
                            }
                        } else {
                            like += writer.getNews().get(j).getLike();
                            dislike += writer.getNews().get(j).getDislike();
                        }

                    }

                    for (Users users : Reader.getAllReaders()) {
                        Reader reader = (Reader) users;
                        if (reader.getFavoriteWriters().contains(writer)) {
                            favCount++;
                        }
                    }
                }
                tableO.addRow(new Object[]{writer.getName(), writer.getSurname(), writer.getNews().size(), favCount + " / " + like + " / " + dislike,
                    writer.getFavoriteGenre(), writer.getEmail(), writer.getGender()});
            }

            table.setModel(tableO);

        }
    }

    //Filtre appleyer for writers
    public static ArrayList<Users> filtreApplyerForWriter(String filtreGenre, String filtreWriter) {
        ArrayList<Users> filteredWriters = new ArrayList<>();

        if (filtreGenre.equals("All") && filtreWriter.equals("All")) {
            return Writer.getAllWriters();

        } else if (!filtreGenre.equals("All") && filtreWriter.equals("All")) {
            for (Users writer : Writer.getAllWriters()) {
                Writer useWriter = (Writer) writer;
                if (useWriter.getFavoriteGenre().equals(filtreGenre)
                        || useWriter.getGenres().contains(filtreGenre)) {
                    filteredWriters.add(useWriter);
                }
            }
        } else if (filtreGenre.equals("All") && !filtreWriter.equals("All")) {
            for (Users writer : Writer.getAllWriters()) {
                Writer useWriter = (Writer) writer;
                if (isWriterMatching(useWriter, filtreWriter)) {
                    filteredWriters.add(useWriter);
                }
            }
        } else {
            for (Users writer : Writer.getAllWriters()) {
                Writer useWriter = (Writer) writer;
                if (isWriterMatching(useWriter, filtreWriter)) {
                    if (useWriter.getFavoriteGenre().equals(filtreGenre)
                            || useWriter.getGenres().contains(filtreGenre)) {
                        filteredWriters.add(useWriter);
                    }
                }
            }
        }

        return filteredWriters;
    }

    //For just writing current user to label
    public static void generalLabel(JLabel labelNowUser) {
        labelNowUser.setText("Email = " + MainFrame.getEmail() + "\n Password = " + MainFrame.getPassword());
        labelNowUser.repaint();
        labelNowUser.revalidate();
    }

    //For clear label
    public static void accountClear() {
        MainFrame.setEmail("XXXXXX");
        MainFrame.setPassword("000000");

    }

    public static Reader readerFinderWithEmail(String email) {
        for (Users user : Users.getAllUsers()) {
            if (user.getEmail().equals(email)) {
                return (Reader) user;
            }
        }

        return null;
    }

    public static News newsFinder(String title) {
        for (News news : News.getAllNews()) {
            if (news.getTitle().equals(title)) {
                return news;
            }
        }
        return null;
    }

}
