/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.EOFException;

import java.io.File;
import java.io.OutputStream;
import java.util.Iterator;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

/**
 *
 * @author erens
 */
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    private static ArrayList<News> allNews = new ArrayList<>();
    private String title;
    private static int allNewsId = 0;
    private int newsId;
    private ArrayList<StringBuilder> content;
    private ArrayList<String> genres;
    private ArrayList<ImageIcon> images;
    private LocalDate publishDate;
    private ImageIcon headerImage;
    private int like;
    private int dislike;
    private ArrayList<Comment> newsComments;

    public News() {
    }

    public News(String title, ArrayList<StringBuilder> content, ArrayList<String> genres,
            LocalDate publishDate, int like, int dislike, ArrayList<ImageIcon> images, ImageIcon headerImage,
            ArrayList<Comment> newsComments) {
        this.title = title;
        this.content = content;
        this.genres = genres;
        this.publishDate = publishDate;
        this.like = like;
        this.dislike = dislike;
        this.images = images;
        this.headerImage = headerImage;
        this.newsComments = new ArrayList<>();
        this.newsId = allNewsId++;

    }

    public int getLike() {
        return like;
    }

    public int getDislike() {
        return dislike;
    }

    public static ArrayList<News> getAllNews() {
        return allNews;
    }

    public static void setAllNews(ArrayList<News> allNews) {
        News.allNews = allNews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<StringBuilder> getContent() {
        return content;
    }

    public ArrayList<ImageIcon> getImages() {
        return images;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setLike(int like) {
        this.like += like;
    }

    public void setDislike(int dislike) {
        this.dislike += dislike;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ImageIcon getHeaderImage() {
        return headerImage;
    }

    public void setContent(ArrayList<StringBuilder> content) {
        this.content = content;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public void setImages(ArrayList<ImageIcon> images) {
        this.images = images;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public void setHeaderImage(ImageIcon headerImage) {
        this.headerImage = headerImage;
    }

    public ArrayList<Comment> getNewsComments() {
        return newsComments;
    }

    public void setNewsComments(ArrayList<Comment> newsComments) {
        this.newsComments = newsComments;
    }

    public static int getAllNewsId() {
        return allNewsId;
    }

    public static void setAllNewsId(int allNewsId) {
        News.allNewsId = allNewsId;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public static void loadAllNewsFromFile() {
        ArrayList<News> loadedNews = newsFileToArrayBringer();
        if (!loadedNews.isEmpty()) {
            allNews.clear();
            allNews.addAll(loadedNews);

            int maxId = 0;
            for (News news : allNews) {
                if (news.getNewsId() > maxId) {
                    maxId = news.getNewsId();
                }
            }
            allNewsId = maxId + 1;

        }
        
    }

    public static void saveAllNewsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("news.txt"))) {
            oos.writeObject(allNews);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void addNews(News news) {
        allNews.add(0, news);
        saveAllNewsToFile();

    }

    public static void deleteNews(News newsToDelete) {
        boolean removed = allNews.removeIf(news -> news.getNewsId() == newsToDelete.getNewsId());
        if (removed) {
            saveAllNewsToFile();
        } else {

        }
    }

    public static void updateNewsReaction(int newsId, String action, Comment comment) {
        boolean found = false;
        for (News news : allNews) {
            if (news.getNewsId() == newsId) {
                switch (action) {
                    case "+like":
                        news.setLike(+1);
                        break;
                    case "-like":
                        news.setLike(-1);
                        break;
                    case "+dislike":
                        news.setDislike(+1);
                        break;
                    case "-dislike":
                        news.setDislike(-1);
                        break;
                    case "-comment":
                        news.newsComments.remove(comment);
                        break;
                    default:

                        return;
                }
                found = true;
                break;
            }
        }

        if (found) {
            saveAllNewsToFile();

        } else {

        }
    }

    private static ArrayList<News> newsFileToArrayBringer() {
        ArrayList<News> newsList = new ArrayList<>();
        File file = new File("news.txt");

        if (!file.exists() || file.length() == 0) {

            return newsList;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList) {
                newsList = (ArrayList<News>) obj;
                
                for (News news : newsList) {
                    news.getNewsComments().clear();
                }

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return newsList;
    }

    public static void fixFileFormat() {

        File file = new File("news.txt");

        if (!file.exists()) {

            saveAllNewsToFile();
            return;
        }

        ArrayList<News> allNewsFromFile = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    Object obj = ois.readObject();

                    if (obj instanceof ArrayList) {
                        ArrayList<News> newsList = (ArrayList<News>) obj;
                        allNewsFromFile.addAll(newsList);

                    } else if (obj instanceof News) {
                        News singleNews = (News) obj;
                        allNewsFromFile.add(singleNews);

                    }

                } catch (EOFException e) {
                    break;
                }
            }
        } catch (Exception e) {

        }

        if (!allNewsFromFile.isEmpty()) {
            allNews.clear();
            allNews.addAll(allNewsFromFile);

            int maxId = 0;
            for (News news : allNews) {
                if (news.getNewsId() > maxId) {
                    maxId = news.getNewsId();
                }
            }
            allNewsId = maxId + 1;

            saveAllNewsToFile();

        }
    }

    public static void updateNews(News editorNews, ArrayList<JTextArea> textAreas,
            JList<String> listGenre, DefaultListModel<String> listGenres,
            JTextArea txtAreaTitle, ArrayList<ImageIcon> images, LocalDate publishDate, ImageIcon header) {

        
        ArrayList<StringBuilder> paragraphs = new ArrayList<>();
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
        editorNews.setPublishDate(publishDate);
        editorNews.setLike(0);
        editorNews.setDislike(0);
        editorNews.setImages(images);
        editorNews.setHeaderImage(header);

        
        News.saveAllNewsToFile();
    }
}
