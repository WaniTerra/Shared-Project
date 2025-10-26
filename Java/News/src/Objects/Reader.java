/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import static Objects.Users.allUsers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author erens
 */
public class Reader extends Users {

    private static ArrayList<Users> allReaders = new ArrayList<>();
    private ArrayList<String> favoriteGenres = new ArrayList<>();
    private ArrayList<Writer> favoriteWriters = new ArrayList<>();
    private ArrayList<News> likedNews = new ArrayList<>();
    private ArrayList<News> dislikedNews = new ArrayList<>();
    private ArrayList<Comment> myComments = new ArrayList<>();
    private ArrayList<Comment> likedComment = new ArrayList<>();
    private ArrayList<Comment> dislikedComment = new ArrayList<>();

    public Reader() {
        super();
        
        this.favoriteGenres = new ArrayList<>();
        this.favoriteWriters = new ArrayList<>();
        this.likedNews = new ArrayList<>();
        this.dislikedNews = new ArrayList<>();
        this.myComments = new ArrayList<>();
        this.likedComment = new ArrayList<>();
        this.dislikedComment = new ArrayList<>();
        
        allReaders.add(this);
        allUsers.add(this);
    }

    public Reader(String name, String surname, String password, String email,
            ArrayList<String> favoriteGenres, ArrayList<News> news, String gender,
            ArrayList<Writer> favoriteWriters, ArrayList<News> likedNews, ArrayList<News> dislikedNews,
            byte[] profilPhoto, ArrayList<Comment> myComments, ArrayList<Comment> likedComment,
            ArrayList<Comment> dislikedComment) {
        super(name, surname, password, email, gender, profilPhoto);
        this.favoriteGenres = favoriteGenres;
        this.favoriteWriters = favoriteWriters;
        this.dislikedNews = dislikedNews;
        this.likedNews = likedNews;
        this.myComments = myComments;
        this.likedComment = likedComment;
        this.dislikedComment = dislikedComment;

        allReaders.add(this);
        allUsers.add(this);
    }

    public static ArrayList<Users> getAllReaders() {
        return allReaders;
    }

    

    public ArrayList<News> getLikedNews() {
        return likedNews;
    }

    public ArrayList<News> getDislikedNews() {
        return dislikedNews;
    }

    public ArrayList<Writer> getFavoriteWriters() {
        return favoriteWriters;
    }

    public ArrayList<String> getFavoriteGenres() {
        return favoriteGenres;
    }

    public ArrayList<Comment> getMyComments() {
        return myComments;
    }

    public void setMyComments(ArrayList<Comment> myComments) {
        this.myComments = myComments;
    }

    public ArrayList<Comment> getLikedComment() {
        return likedComment;
    }

    public void setLikedComment(ArrayList<Comment> likedComment) {
        this.likedComment = likedComment;
    }

    public ArrayList<Comment> getDislikedComment() {
        return dislikedComment;
    }

    public void setDislikedComment(ArrayList<Comment> dislikedComment) {
        this.dislikedComment = dislikedComment;
    }
   
        public void setFavoriteGenresJson(String genresJson) {
        if (genresJson != null && !genresJson.trim().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<String> genres = mapper.readValue(genresJson, 
                    new TypeReference<ArrayList<String>>() {});
                this.favoriteGenres = genres;
            } catch (Exception e) {
               
                this.favoriteGenres = new ArrayList<>();
            }
        }
    }
   
    public void setFavoriteGenres(ArrayList<String> favoriteGenres) {
        this.favoriteGenres = favoriteGenres != null ? favoriteGenres : new ArrayList<>();
    }
    
    
    public void setFavoriteWriters(ArrayList<Writer> favoriteWriters) {
        this.favoriteWriters = favoriteWriters != null ? favoriteWriters : new ArrayList<>();
    }
    
    public void setLikedNews(ArrayList<News> likedNews) {
        this.likedNews = likedNews != null ? likedNews : new ArrayList<>();
    }
    
    public void setDislikedNews(ArrayList<News> dislikedNews) {
        this.dislikedNews = dislikedNews != null ? dislikedNews : new ArrayList<>();
    }
    
    

}
