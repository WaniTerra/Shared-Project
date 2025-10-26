/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SQL;

import Objects.*;
import Panel.NewsFrame;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.ImageIcon;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author erens
 */
public class DatabaseCodes {

    public static void dbReaderAdder(String name, String surname, String email, String password,
            ArrayList<String> genres, String gender, byte[] profilPhoto, ArrayList<String> favoriteGenres,
            ArrayList<Writer> favoriteWriters, ArrayList<News> likedNews, ArrayList<News> dislikedNews,
            ArrayList<Comment> myComments, ArrayList<Comment> likedComment,
            ArrayList<Comment> dislikedComment) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
        EntityManager em = emf.createEntityManager();

        try {

            ObjectMapper mapper = new ObjectMapper();
            String favoriteGenresJson = mapper.writeValueAsString(genres);
            String favoriteWriterJson = mapper.writeValueAsString(favoriteWriters);
            String likeNewsJson = mapper.writeValueAsString(likedNews);
            String dislikedNewsJson = mapper.writeValueAsString(dislikedNews);
            String myCommentsJson = mapper.writeValueAsString(myComments);
            String likedCommentsJson = mapper.writeValueAsString(likedComment);
            String dislikedCommentsJson = mapper.writeValueAsString(dislikedComment);

            em.getTransaction().begin();

            Usersdb user = new Usersdb(email, name, surname, password, gender, profilPhoto);
            em.persist(user);

            Readerdb reader = new Readerdb();
            reader.setUser(user);
            reader.setFavoriteGenresJson(favoriteGenresJson);
            reader.setDislikedNewsJson(dislikedNewsJson);
            reader.setLikedCommentsJson(likedCommentsJson);
            reader.setMyCommentsJson(myCommentsJson);
            reader.setLikedNewsJson(likeNewsJson);
            reader.setFavoriteWritersJson(favoriteWriterJson);
            reader.setDislikedCommentsJson(dislikedCommentsJson);

            em.persist(reader);

            em.getTransaction().commit();
            em.close();
            emf.close();
        } catch (Exception e) {

        }
    }

    public static void dbReaderDelete(Reader reader) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Readerdb readerdb = em.find(Readerdb.class, reader.getEmail());
        Usersdb usersdb = em.find(Usersdb.class, reader.getEmail());

        em.remove(readerdb);
        em.remove(usersdb);

        em.getTransaction().commit();
        em.close();
        emf.close();

    }

    public static void dbToArraysForReader() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        ObjectMapper mapper = new ObjectMapper();

        List<Readerdb> readerDbList = em.createNamedQuery("Readerdb.findAll", Readerdb.class).getResultList();

        for (Readerdb rdb : readerDbList) {

            try {
                ArrayList<String> favoritegenres = mapper.readValue(rdb.getFavoriteGenresJson(), new TypeReference<ArrayList<String>>() {
                });
                ArrayList<String> favoriteWritersEmail = mapper.readValue(rdb.getFavoriteWritersJson(), new TypeReference<ArrayList<String>>() {
                });
                
                ArrayList<String> likedNewsID = mapper.readValue(rdb.getLikedNewsJson(), new TypeReference<ArrayList<String>>() {
                });
                ArrayList<String> dislikedNewsID = mapper.readValue(rdb.getDislikedNewsJson(), new TypeReference<ArrayList<String>>() {
                });
                ArrayList<String> likedCommentsID = mapper.readValue(rdb.getLikedCommentsJson(), new TypeReference<ArrayList<String>>() {
                });
                ArrayList<String> dislikedCommentsID = mapper.readValue(rdb.getDislikedCommentsJson(), new TypeReference<ArrayList<String>>() {
                });
                ArrayList<String> myCommentsID = mapper.readValue(rdb.getMyCommentsJson(), new TypeReference<ArrayList<String>>() {
                });
                
                ArrayList<Writer> favoriteWriter = new ArrayList<>();
                ArrayList<News> favoriteNews = new ArrayList<>();
                ArrayList<News> likedNews = new ArrayList<>();
                ArrayList<News> dislikedNews = new ArrayList<>();
                ArrayList<Comment> likedComments = new ArrayList<>();
                ArrayList<Comment> dislikedComments = new ArrayList<>();
                ArrayList<Comment> myComments = new ArrayList<>();
                
                for (String id : likedNewsID) {
                    for (News news : News.getAllNews()) {
                        if (String.valueOf(news.getNewsId()).equals(id)) {
                            likedNews.add(news);
                            break;
                        }
                    }
                }
                
                for (String id : dislikedNewsID) {
                    for (News news : News.getAllNews()) {
                        if (String.valueOf(news.getNewsId()).equals(id)) {
                            dislikedNews.add(news);
                            break;
                        }
                    }
                }
                
                for (String email : favoriteWritersEmail) {
                    for (Users user : Writer.getAllWriters()) {
                        Writer writer = (Writer) user;
                        if (String.valueOf(writer.getEmail()).equals(email)) {
                            favoriteWriter.add(writer);
                            break;
                        }
                    }
                }
                
                for (String id : likedCommentsID) {
                    for (Comment comment : Comment.getAllComments()) {
                        if (String.valueOf(comment.getCommentId()).equals(id)) {
                            likedComments.add(comment);
                            break;
                        }
                    }
                }
                
                for (String id : dislikedCommentsID) {
                    for (Comment comment : Comment.getAllComments()) {
                        if (String.valueOf(comment.getCommentId()).equals(id)) {
                            dislikedComments.add(comment);
                            break;
                        }
                    }
                }
                
                for (String id : myCommentsID) {
                    for (Comment comment : Comment.getAllComments()) {
                        if (String.valueOf(comment.getCommentId()).equals(id)) {
                            myComments.add(comment);
                            break;
                        }
                    }
                }
                
                Reader reader = new Reader(
                        rdb.getUsers().getName(),
                        rdb.getUsers().getSurname(),
                        rdb.getUsers().getPassword(),
                        rdb.getEmail(),
                        favoritegenres,
                        favoriteNews,
                        rdb.getUsers().getGender(),
                        favoriteWriter,
                        likedNews,
                        dislikedNews,
                        rdb.getUsers().getProfilePhoto(),
                        myComments,
                        likedComments,
                        dislikedComments
                );
            } catch (JsonProcessingException ex) {
                Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        em.close();
        emf.close();

    }

    public static void dbWriterAdder(String name, String surname, String password, String email, ArrayList<News> news,
            String gender, ArrayList<String> genres, String favoriteGenre, String biography, byte[] profilPhoto) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
        EntityManager em = emf.createEntityManager();

        try {

            ObjectMapper mapper = new ObjectMapper();

            String genresJson = mapper.writeValueAsString(genres);
            String newsJson = mapper.writeValueAsString(news);

            em.getTransaction().begin();

            Usersdb user = new Usersdb(email, name, surname, password, gender, profilPhoto);
            em.persist(user);

            Writerdb writer = new Writerdb();
            writer.setUsers(user);
            writer.setFavoriteGenre(favoriteGenre);
            writer.setGenresJson(genresJson);
            writer.setNewsJson(newsJson);
            writer.setBiography(biography);

            em.persist(writer);

            em.getTransaction().commit();
            em.close();
            emf.close();
        } catch (Exception e) {

        }

    }

    public static void dbWriterDeleter(Writer writer) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Writerdb writerdb = em.find(Writerdb.class, writer.getEmail());
        Usersdb usersdb = em.find(Usersdb.class, writer.getEmail());

        em.remove(writerdb);
        em.remove(usersdb);

        em.getTransaction().commit();

        em.close();
        emf.close();

    }

    public static void dbToArraysForWriter() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        ObjectMapper mapper = new ObjectMapper();

        List<Writerdb> writerDbList = em.createNamedQuery("Writerdb.findAll", Writerdb.class).getResultList();

        for (Writerdb wdb : writerDbList) {

            try {
                ArrayList<String> genres = mapper.readValue(wdb.getGenresJson(), new TypeReference<ArrayList<String>>() {
                });
                ArrayList<String> newsId = mapper.readValue(wdb.getNewsJson(), new TypeReference<ArrayList<String>>() {
                });
                
                ArrayList<News> newsIdList = new ArrayList();
                
                for (String id : newsId) {
                    for (News news : News.getAllNews()) {
                        if (String.valueOf(news.getNewsId()).equals(id)) {
                            newsIdList.add(news);
                            
                            break;
                        }
                    }
                }
                
                Writer writer = new Writer(wdb.getUsers().getName(),
                        wdb.getUsers().getSurname(),
                        wdb.getUsers().getPassword(),
                        wdb.getUsers().getEmail(),
                        newsIdList,
                        wdb.getUsers().getGender(),
                        genres,
                        wdb.getFavoriteGenre(),
                        wdb.getBiography(),
                        wdb.getUsers().getProfilePhoto());
            } catch (JsonProcessingException ex) {
                Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        em.close();
        emf.close();

    }

    public static void dbAdminAdder(String name, String surname, String password, String email, String gender, byte[] profilPhoto, String permissionLevel) {
        try {
            Connection conn = DataBaseConnection.getConn();

            PreparedStatement psUser = conn.prepareStatement("INSERT INTO usersdb (email, name, surname, password, gender, profile_photo) VALUES (?,?,?,?,?,?) ");
            psUser.setString(1, email);
            psUser.setString(2, name);
            psUser.setString(3, surname);
            psUser.setString(4, password);
            psUser.setString(5, gender);
            psUser.setBytes(6, profilPhoto);

            psUser.executeUpdate();

            PreparedStatement psAdmin = conn.prepareStatement("INSERT INTO admindb (email, permission_level) VALUES (?,?) ");
            psAdmin.setString(1, email);
            psAdmin.setString(2, permissionLevel);

            psAdmin.executeUpdate();

            DataBaseConnection.getCloseConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void dbToArraysForAdmin() {
        try {
            Connection conn = DataBaseConnection.getConn();

            PreparedStatement ps = conn.prepareStatement("SELECT u.email, u.name, u.surname, u.password, u.gender, u.profile_photo, a.permission_level "
                    + "FROM usersdb u "
                    + "JOIN admindb a ON u.email = a.email");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Users admin = new Admin(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(1), rs.getString(5), rs.getBytes(6), rs.getString(7));
            }

            DataBaseConnection.getCloseConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void dbAdminEdit(String newValue, String change, String email) {

        try {
            Connection conn = DataBaseConnection.getConn();
            String sql = "UPDATE usersdb SET " + change + " = ? WHERE email = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, newValue);
            ps.setString(2, email);

            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void dbAdminDeleter(String email) {
        try {
            Connection conn = DataBaseConnection.getConn();

            String sql2 = "DELETE FROM admindb WHERE email = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);

            ps2.setString(1, email);

            ps2.executeUpdate();

            ps2.close();

            String sql1 = "DELETE FROM usersdb WHERE email = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);

            ps1.setString(1, email);

            ps1.executeUpdate();

            ps1.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void arrayToDbLikedCommentId(int commentId, String readerEmail, boolean liked) {

        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
            EntityManager em = emf.createEntityManager();
            
            em.getTransaction().begin();
            
            Readerdb readerdb = em.find(Readerdb.class, readerEmail);
            
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<String> commentIdList = mapper.readValue(readerdb.getLikedCommentsJson(), new TypeReference<ArrayList<String>>() {
            });
            String updatedJson = null;
            if (liked) {
                commentIdList.add(commentId + "");
            } else {
                commentIdList.remove(commentId + "");
                
            }
            
            updatedJson = mapper.writeValueAsString(commentIdList);
            readerdb.setLikedCommentsJson(updatedJson);
            em.merge(readerdb);
            em.getTransaction().commit();
            
            em.close();
            emf.close();
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void arrayToDbDislikedCommentId(int commentId, String readerEmail, boolean disliked){

        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
            EntityManager em = emf.createEntityManager();
            
            em.getTransaction().begin();
            
            Readerdb readerdb = em.find(Readerdb.class, readerEmail);
            
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<String> commentIdList = mapper.readValue(readerdb.getDislikedCommentsJson(), new TypeReference<ArrayList<String>>() {
            });
            
            String updatedJson = null;
            if (disliked) {
                commentIdList.add(commentId + "");
                
            } else {
                commentIdList.remove(commentId + "");
            }
            
            updatedJson = mapper.writeValueAsString(commentIdList);
            
            readerdb.setDislikedCommentsJson(updatedJson);
            
            em.merge(readerdb);
            em.getTransaction().commit();
            
            em.close();
            emf.close();
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void arrayToDbMyCommentIdDeleter(int commentId, String readerEmail) {

        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
            EntityManager em = emf.createEntityManager();
            
            em.getTransaction().begin();
            
            Readerdb readerdb = em.find(Readerdb.class, readerEmail);
            
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<String> commentIdList = mapper.readValue(readerdb.getMyCommentsJson(), new TypeReference<ArrayList<String>>() {
            });
            String updatedJson = null;
            
            commentIdList.remove(commentId + "");
            
            updatedJson = mapper.writeValueAsString(commentIdList);
            readerdb.setMyCommentsJson(updatedJson);
            em.merge(readerdb);
            em.getTransaction().commit();
            
            em.close();
            emf.close();
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static byte[] convertImageIconToByteArray(ImageIcon profilPhoto)  {
        if (profilPhoto == null) {
            return null;
        }

        BufferedImage bufferedImage = new BufferedImage(
                profilPhoto.getIconWidth(),
                profilPhoto.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = bufferedImage.createGraphics();
        try {

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            profilPhoto.paintIcon(null, g2, 0, 0);
        } finally {
            g2.dispose();
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ImageIcon byteArrayToImageIcon(byte[] imageBytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            Image image = ImageIO.read(bais);
            return new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void commentToDbOwner(String commentOwnerEmail, int commentId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("comments.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Email:")) {
                    int startIndex = line.indexOf("Email:") + 6;
                    int endIndex = line.indexOf("- News:");

                    if (commentOwnerEmail.equals(line.substring(startIndex, endIndex).trim())) {

                        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
                        EntityManager em = emf.createEntityManager();
                        em.getTransaction().begin();

                        ObjectMapper mapper = new ObjectMapper();

                        Readerdb readerdb = em.find(Readerdb.class, commentOwnerEmail.trim());
                        ArrayList<String> commentIdList = mapper.readValue(readerdb.getMyCommentsJson(), new TypeReference<ArrayList<String>>() {
                        });

                        commentIdList.add(commentId + "");

                        String updatedJson = mapper.writeValueAsString(commentIdList);

                        readerdb.setMyCommentsJson(updatedJson);
                        em.merge(readerdb);
                        em.getTransaction().commit();
                        em.close();
                        emf.close();

                        break;
                    }

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void favoriteWriterToReaderDb(Writer writer, Reader reader, boolean add) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
            EntityManager em = emf.createEntityManager();

            Readerdb readerdb = em.find(Readerdb.class, reader.getEmail());

            em.getTransaction().begin();
            ObjectMapper mapper = new ObjectMapper();

            ArrayList<String> favoriteWriters = mapper.readValue(readerdb.getFavoriteWritersJson(), new TypeReference<ArrayList<String>>() {
            });
            String updatedJson = "";

            if (add) {
                favoriteWriters.add(writer.getEmail());

                updatedJson = mapper.writeValueAsString(favoriteWriters);
            } else {
                favoriteWriters.remove(writer.getEmail());
                updatedJson = mapper.writeValueAsString(favoriteWriters);
            }

            readerdb.setFavoriteWritersJson(updatedJson);

            em.merge(readerdb);
            em.getTransaction().commit();
            em.close();
            emf.close();

        } catch (JsonProcessingException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void likedNewsToReaderDb(News news, Reader reader, boolean add) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
            EntityManager em = emf.createEntityManager();

            Readerdb readerdb = em.find(Readerdb.class, reader.getEmail());

            em.getTransaction().begin();
            ObjectMapper mapper = new ObjectMapper();

            ArrayList<String> likedNews = mapper.readValue(readerdb.getLikedNewsJson(), new TypeReference<ArrayList<String>>() {
            });
            String updatedJson = "";

            if (add) {
                likedNews.add(String.valueOf(news.getNewsId()));

                updatedJson = mapper.writeValueAsString(likedNews);
            } else {
                likedNews.remove(String.valueOf(news.getNewsId()));
                updatedJson = mapper.writeValueAsString(likedNews);
            }

            readerdb.setLikedNewsJson(updatedJson);

            em.merge(readerdb);
            em.getTransaction().commit();
            em.close();
            emf.close();

        } catch (JsonProcessingException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void dislikedNewsToReaderDb(News news, Reader reader, boolean add) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
            EntityManager em = emf.createEntityManager();

            Readerdb readerdb = em.find(Readerdb.class, reader.getEmail());

            em.getTransaction().begin();
            ObjectMapper mapper = new ObjectMapper();

            ArrayList<String> dislikedNews = mapper.readValue(readerdb.getDislikedNewsJson(), new TypeReference<ArrayList<String>>() {
            });
            String updatedJson = "";

            if (add) {
                dislikedNews.add(String.valueOf(news.getNewsId()));

                updatedJson = mapper.writeValueAsString(dislikedNews);
            } else {
                dislikedNews.remove(String.valueOf(news.getNewsId()));
                updatedJson = mapper.writeValueAsString(dislikedNews);
            }

            readerdb.setDislikedNewsJson(updatedJson);

            em.merge(readerdb);
            em.getTransaction().commit();
            em.close();
            emf.close();

        } catch (JsonProcessingException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void dbToWriterAttributeChanger(Writer writer, String change, String newValue) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Writerdb writerdb = em.find(Writerdb.class, writer.getEmail());

        switch (change) {
            case "Name":
                writerdb.getUsers().setName(newValue);
                break;
            case "Surname":
                writerdb.getUsers().setSurname(newValue);
                break;
            case "Genre":
                writerdb.setFavoriteGenre(newValue);
                break;
            case "Gender":
                writerdb.getUsers().setGender(newValue);
                break;
            case "Password":
                writerdb.getUsers().setPassword(newValue);
                break;
            case "Biography":
                writerdb.setBiography(newValue);
                break;
        }

        em.merge(writerdb);
        em.getTransaction().commit();

        em.close();
        emf.close();
    }

    public static void dbWriterNewsChanger(News news, Writer writer, boolean addNews) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            Writerdb writerdb = em.find(Writerdb.class, writer.getEmail());

            ObjectMapper mapper = new ObjectMapper();

            ArrayList<String> writerNews = mapper.readValue(writerdb.getNewsJson(), new TypeReference<ArrayList<String>>() {
            });
            if (addNews) {
                writerNews.add(news.getNewsId() + "");
            } else {
                writerNews.remove(news.getNewsId() + "");
            }

            String updatedJson = mapper.writeValueAsString(writerNews);

            writerdb.setNewsJson(updatedJson);

            em.merge(writerdb);
            em.getTransaction().commit();

            em.close();
            emf.close();
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DatabaseCodes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void dbCommentDeleter(int id, String email, Comment comment) {
        for (Users user : Reader.getAllReaders()) {
            Reader reader = (Reader) user;
            if (reader.getLikedComment().contains(comment)) {
                reader.getLikedComment().remove(comment);
                DatabaseCodes.arrayToDbLikedCommentId(id, email, false);
            }
            
            if (reader.getDislikedComment().contains(comment)) {
                reader.getDislikedComment().remove(comment);
                DatabaseCodes.arrayToDbDislikedCommentId(id, email, false);
            }
            
            if (reader.getMyComments().contains(comment)) {
                reader.getMyComments().remove(comment);
                DatabaseCodes.arrayToDbMyCommentIdDeleter(id, email);
            }
        }
    }

    private static void cleanInvalidComments() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewsPU");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            ArrayList<Comment> allComments = Comment.getAllComments();
            Set<Integer> validCommentIds = new HashSet<>();
            for (Comment comment : allComments) {
                validCommentIds.add(comment.getCommentId());
            }

            ObjectMapper mapper = new ObjectMapper();

            for (Users users : Reader.getAllReaders()) {
                Reader reader = (Reader) users;
                Readerdb readerdb = em.find(Readerdb.class, reader.getEmail());

                if (readerdb != null) {
                    try {

                        ArrayList<Integer> liked = mapper.readValue(
                                readerdb.getLikedCommentsJson(),
                                new TypeReference<ArrayList<Integer>>() {
                        }
                        );
                        ArrayList<Integer> disliked = mapper.readValue(
                                readerdb.getDislikedCommentsJson(),
                                new TypeReference<ArrayList<Integer>>() {
                        }
                        );
                        ArrayList<Integer> myComments = mapper.readValue(
                                readerdb.getMyCommentsJson(),
                                new TypeReference<ArrayList<Integer>>() {
                        }
                        );

                        boolean hasChanges = false;

                        if (liked.removeIf(id -> !validCommentIds.contains(id))) {
                            hasChanges = true;
                        }

                        if (disliked.removeIf(id -> !validCommentIds.contains(id))) {
                            hasChanges = true;
                        }

                        if (myComments.removeIf(id -> !validCommentIds.contains(id))) {
                            hasChanges = true;
                        }

                        if (hasChanges) {
                            readerdb.setLikedCommentsJson(mapper.writeValueAsString(liked));
                            readerdb.setDislikedCommentsJson(mapper.writeValueAsString(disliked));
                            readerdb.setMyCommentsJson(mapper.writeValueAsString(myComments));
                            em.merge(readerdb);
                        }
                    } catch (Exception jsonException) {

                    }
                }
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    public static void callDeleter() {
        try {
            cleanInvalidComments();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
