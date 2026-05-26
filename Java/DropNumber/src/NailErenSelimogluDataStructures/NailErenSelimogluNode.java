/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NailErenSelimogluDataStructures;

/**
 *
 * @author erens
 */
public class NailErenSelimogluNode {
    int data;
    NailErenSelimogluNode rightPnt;
    NailErenSelimogluNode leftPnt;
    NailErenSelimogluNode upPnt;
    int row;
    int col;
    
    
    public NailErenSelimogluNode(int data,int row, int col) {
        this.data = data;
        this.rightPnt = null;
        this.leftPnt = null;
        this.upPnt = null;
        this.row = row;
        this.col = col;
    }   
    
    
    
    
}
