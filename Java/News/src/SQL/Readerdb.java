/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SQL;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author erens
 */
@Entity
@Table(name = "readerdb")
@NamedQueries({
    @NamedQuery(name = "Readerdb.findAll", query = "SELECT r FROM Readerdb r"),
    @NamedQuery(name = "Readerdb.findByEmail", query = "SELECT r FROM Readerdb r WHERE r.email = :email")})
public class Readerdb implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Lob
    @Column(name = "favorite_genres_json")
    private String favoriteGenresJson;
    @Lob
    @Column(name = "favorite_writers_json")
    private String favoriteWritersJson;
    @Lob
    @Column(name = "liked_news_json")
    private String likedNewsJson;
    @Lob
    @Column(name = "disliked_news_json")
    private String dislikedNewsJson;
    @Lob
    @Column(name = "my_comments_json")
    private String myCommentsJson;
    @Lob
    @Column(name = "liked_comments_json")
    private String likedCommentsJson;
    @Lob
    @Column(name = "disliked_comments_json")
    private String dislikedCommentsJson;
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usersdb users;

    public Readerdb() {
    }

    public Readerdb(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFavoriteGenresJson() {
        return favoriteGenresJson;
    }

    public void setFavoriteGenresJson(String favoriteGenresJson) {
        this.favoriteGenresJson = favoriteGenresJson;
    }

    public String getFavoriteWritersJson() {
        return favoriteWritersJson;
    }

    public void setFavoriteWritersJson(String favoriteWritersJson) {
        this.favoriteWritersJson = favoriteWritersJson;
    }

    public String getLikedNewsJson() {
        return likedNewsJson;
    }

    public void setLikedNewsJson(String likedNewsJson) {
        this.likedNewsJson = likedNewsJson;
    }

    public String getDislikedNewsJson() {
        return dislikedNewsJson;
    }

    public void setDislikedNewsJson(String dislikedNewsJson) {
        this.dislikedNewsJson = dislikedNewsJson;
    }

    public String getMyCommentsJson() {
        return myCommentsJson;
    }

    public void setMyCommentsJson(String myCommentsJson) {
        this.myCommentsJson = myCommentsJson;
    }

    public String getLikedCommentsJson() {
        return likedCommentsJson;
    }

    public void setLikedCommentsJson(String likedCommentsJson) {
        this.likedCommentsJson = likedCommentsJson;
    }

    public String getDislikedCommentsJson() {
        return dislikedCommentsJson;
    }

    public void setDislikedCommentsJson(String dislikedCommentsJson) {
        this.dislikedCommentsJson = dislikedCommentsJson;
    }

    public Usersdb getUsers() {
        return users;
    }

    public void setUsers(Usersdb users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Readerdb)) {
            return false;
        }
        Readerdb other = (Readerdb) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SQL.Readers[ email=" + email + " ]";
    }

    public void setUser(Usersdb users) {
        this.users = users;
        this.setEmail(users.getEmail());

    }

}
