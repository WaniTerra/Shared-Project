/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author erens
 */
public class Users {

    private String name;
    private String surname;
    private String password;
    private String email;
    private String gender;
    private byte[] profilPhoto;

    protected static ArrayList<Users> allUsers = new ArrayList<>();

    public Users(){
        
    }
    
    public Users(String name, String surname, String password, String email, String gender, byte[] profilPhoto) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.profilPhoto = profilPhoto;

    }

    public static ArrayList<Users> getAllUsers() {
        return allUsers;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static void setAllUsers(ArrayList<Users> allUsers) {
        Users.allUsers = allUsers;
    }

    public byte[] getProfilPhoto() {
        return profilPhoto;
    }

    public void setProfilPhoto(byte[] profilPhoto) {
        this.profilPhoto = profilPhoto;
    }
    

}
