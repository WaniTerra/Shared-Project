/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsClasses;

/**
 *
 * @author erens
 */
public class User {

    private int userId;
    private MyLinkedList<Integer> ratings;

    public User(int userId) {
        this.userId = userId;
        ratings = new MyLinkedList<>();
    }

    public int getUserId() {
        return userId;
    }

    public MyLinkedList<Integer> getRatings() {
        return ratings;
    }

    public void addRating(int rating) {
        ratings.add(rating);
    }
}
