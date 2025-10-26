/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SQL;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.swing.ImageIcon;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "usersdb")
@NamedQueries({
    @NamedQuery(name = "Usersdb.findAll", query = "SELECT u FROM Usersdb u"),
    @NamedQuery(name = "Usersdb.findByEmail", query = "SELECT u FROM Usersdb u WHERE u.email = :email"),
    @NamedQuery(name = "Usersdb.findByName", query = "SELECT u FROM Usersdb u WHERE u.name = :name"),
    @NamedQuery(name = "Usersdb.findBySurname", query = "SELECT u FROM Usersdb u WHERE u.surname = :surname"),
    @NamedQuery(name = "Usersdb.findByPassword", query = "SELECT u FROM Usersdb u WHERE u.password = :password"),
    @NamedQuery(name = "Usersdb.findByGender", query = "SELECT u FROM Usersdb u WHERE u.gender = :gender")})
public class Usersdb implements Serializable {

    @Lob
    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    @Basic(optional = false)
    @Column(name = "users_id")
    private int usersId;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "gender")
    private String gender;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "users")
    private Readerdb readers;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "users")
    private Writerdb writers;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "users")
    private Admindb admins;
    

    public Usersdb() {
    }

    public Usersdb(String email) {
        this.email = email;
    }

    public Usersdb(String email, String name, String surname, String password, String gender, byte[] profilePhoto) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Readerdb getReaders() {
        return readers;
    }

    public void setReaders(Readerdb readers) {
        this.readers = readers;
    }

    public Writerdb getWriters() {
        return writers;
    }

    public void setWriters(Writerdb writers) {
        this.writers = writers;
    }

    public Admindb getAdmins() {
        return admins;
    }

    public void setAdmins(Admindb admins) {
        this.admins = admins;
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
        if (!(object instanceof Usersdb)) {
            return false;
        }
        Usersdb other = (Usersdb) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SQL.Users[ email=" + email + " ]";
    }

    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

  

   

   

}
