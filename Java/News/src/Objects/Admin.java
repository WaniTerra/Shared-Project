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
public class Admin extends Users{
   
    
    private static ArrayList<Users> allAdmin = new ArrayList<>();
    private String permissionLevel;
    public Admin(String name, String surname, String password, String email, String gender, byte[] profilPhoto, String permissionLevel) {
        super(name, surname, password, email, gender, profilPhoto);
        this.permissionLevel = permissionLevel;
        allAdmin.add(this);
        allUsers.add(this);
    }

    public static ArrayList<Users> getAllAdmin() {
        return allAdmin;
    }

    public static void setAllAdmin(ArrayList<Users> allAdmin) {
        Admin.allAdmin = allAdmin;
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
    
}
