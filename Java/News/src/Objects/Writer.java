/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import static Objects.Users.allUsers;
import java.util.ArrayList;
import javax.swing.ImageIcon;



/**
 *
 * @author erens
 */
public class Writer extends Users{
    
    private static ArrayList<Users> allWriters = new ArrayList<>();;
    private ArrayList<News> news = new ArrayList<>();;
    private ArrayList<String> genres = new ArrayList<>();
    private String favoriteGenre;
    private String biography;
    
   
    public Writer(String name, String surname, String password, String email, ArrayList<News> news, 
            String gender, ArrayList<String> genres, String favoriteGenre, String biography, byte[] profilPhoto) {
        super(name, surname, password, email, gender, profilPhoto);
        
        this.favoriteGenre = favoriteGenre;
        this.genres = genres;
        this.news = news;
        this.biography = biography;
        allWriters.add(this);
        allUsers.add(this);
    }

    public static ArrayList<Users> getAllWriters() {
        return allWriters;
    }

    public static void setAllWriters(ArrayList<Users> allWriters) {
        Writer.allWriters = allWriters;
    }

    public ArrayList<News> getNews() {
        return news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    public String getFavoriteGenre() {
        return favoriteGenre;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public void setFavoriteGenre(String favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
    

   
    

    
    
    
    
    
    
    
}
