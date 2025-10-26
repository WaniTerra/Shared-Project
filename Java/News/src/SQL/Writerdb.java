/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SQL;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author erens
 */
@Entity
@Table(name = "writerdb")
@NamedQueries({
    @NamedQuery(name = "Writerdb.findAll", query = "SELECT w FROM Writerdb w"),
    @NamedQuery(name = "Writerdb.findByEmail", query = "SELECT w FROM Writerdb w WHERE w.email = :email"),
    @NamedQuery(name = "Writerdb.findByFavoriteGenre", query = "SELECT w FROM Writerdb w WHERE w.favoriteGenre = :favoriteGenre")})
public class Writerdb implements Serializable {

    @Basic(optional = false)
    @Column(name = "writer_id")
    private int writerId;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Column(name = "favorite_genre")
    private String favoriteGenre;
    @Lob
    @Column(name = "genres_json")
    private String genresJson;
    @Lob
    @Column(name = "news_json")
    private String newsJson;
    @Lob
    @Column(name = "biography")
    private String biography;
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Usersdb users;

    public Writerdb() {
    }

    public Writerdb(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFavoriteGenre() {
        return favoriteGenre;
    }

    public void setFavoriteGenre(String favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }

    public String getGenresJson() {
        return genresJson;
    }

    public void setGenresJson(String genresJson) {
        this.genresJson = genresJson;
    }

    public String getNewsJson() {
        return newsJson;
    }

    public void setNewsJson(String newsJson) {
        this.newsJson = newsJson;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

   

    public Usersdb getUsers() {
        return users;
    }

    public void setUsers(Usersdb users) {
        this.users = users;
        this.setEmail(users.getEmail());
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
        if (!(object instanceof Writerdb)) {
            return false;
        }
        Writerdb other = (Writerdb) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SQL.Writers[ email=" + email + " ]";
    }

    public int getWriterId() {
        return writerId;
    }

    public void setWriterId(int writerId) {
        this.writerId = writerId;
    }
    
}
