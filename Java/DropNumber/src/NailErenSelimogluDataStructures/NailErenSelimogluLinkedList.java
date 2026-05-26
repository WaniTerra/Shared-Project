/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package NailErenSelimogluDataStructures;

/**
 *
 * @author erens
 */
public class NailErenSelimogluLinkedList {

    public NailErenSelimogluNode head;

    public  NailErenSelimogluNode add(int data, int col) {
        int row = rowFinder(col);
        NailErenSelimogluNode newNode = new NailErenSelimogluNode(data, row, col);

        if (head == null) {
            head = newNode;
        } else {
            NailErenSelimogluNode temp = head;
            NailErenSelimogluNode tempRL = temp;
            while (temp.row != newNode.row || temp.col != newNode.col) {
                if (newNode.row != 0) {
                    if (temp.col < newNode.col) {
                        if (temp.rightPnt != null && temp.rightPnt.col <= newNode.col) {
                            temp = temp.rightPnt;
                            continue;
                        }
                    } else if (temp.col > newNode.col) {
                        if (temp.leftPnt != null && temp.leftPnt.col >= newNode.col) {
                            temp = temp.leftPnt;
                            continue;
                        }
                    }

                    if (temp.row < newNode.row - 1) {
                        if (temp.upPnt == null) {
                            return newNode;
                        }
                        temp = temp.upPnt;
                        continue;
                    } else if (temp.row == newNode.row - 1) {

                        newNode.upPnt = temp.upPnt;
                        temp.upPnt = newNode;

                        if (temp.rightPnt != null) {
                            tempRL = temp.rightPnt;
                            if (tempRL.upPnt != null && tempRL.upPnt.row == newNode.row) {
                                tempRL = tempRL.upPnt;

                                newNode.rightPnt = tempRL;
                                tempRL.leftPnt = newNode;
                            }
                        }

                        if (temp.leftPnt != null) {
                            tempRL = temp.leftPnt;
                            if (tempRL.upPnt != null && tempRL.upPnt.row == newNode.row) {
                                tempRL = tempRL.upPnt;

                                newNode.leftPnt = tempRL;
                                tempRL.rightPnt = newNode;
                            }
                        }

                    }
                    temp = temp.upPnt;
                } else if (newNode.row == 0) {
                    if (temp.col < newNode.col) {
                        if (temp.rightPnt != null && temp.rightPnt.col <= newNode.col) {
                            temp = temp.rightPnt;
                            continue;
                        }
                        if (temp.rightPnt == null) {
                            temp.rightPnt = newNode;
                            newNode.leftPnt = temp;
                        } else {
                            newNode.rightPnt = temp.rightPnt;
                            temp.rightPnt = newNode;
                            newNode.leftPnt = temp;
                            newNode.rightPnt.leftPnt = newNode;
                        }
                        break;
                    } else if (temp.col > newNode.col) {
                        if (temp.leftPnt != null && temp.leftPnt.col >= newNode.col) {
                            temp = temp.leftPnt;
                            continue;
                        }
                        if (temp.leftPnt == null) {
                            temp.leftPnt = newNode;
                            newNode.rightPnt = temp;
                        } else {
                            newNode.leftPnt = temp.leftPnt;
                            newNode.rightPnt = temp;
                            temp.leftPnt = newNode;
                            newNode.leftPnt.rightPnt = newNode;
                        }
                        break;
                    } else {
                        temp.rightPnt = newNode;
                        newNode.leftPnt = temp;
                        break;
                    }

                }
            }

        }
        return newNode;
    }

    public void delete(int row, int col) {

        if (head == null) {
            return;
        }
        if (head.row == row && head.col == col) {
            if (head.upPnt != null) {

                NailErenSelimogluNode headUp = head.upPnt;
                headUp.rightPnt = head.rightPnt;
                headUp.leftPnt = null;
                if (head.rightPnt != null) {
                    head.rightPnt.leftPnt = headUp;
                }

                NailErenSelimogluNode fix = headUp;
                while (fix != null) {
                    fix.row--;
                    fix = fix.upPnt;
                }
                head.upPnt = null;
                head.rightPnt = null;
                head = headUp;
            } else {
                head = head.rightPnt;
                if (head != null) {
                    head.leftPnt = null;
                }
            }
            return;
        }

        NailErenSelimogluNode temp = head;

        while (temp.row != row || temp.col != col) {
            if (row != 0) {
                if (temp.col < col) {
                    if (temp.rightPnt != null && temp.rightPnt.col <= col) {
                        temp = temp.rightPnt;
                        continue;
                    }
                } else if (temp.col > col) {
                    if (temp.leftPnt != null && temp.leftPnt.col >= col) {
                        temp = temp.leftPnt;
                        continue;
                    }
                }

                if (temp.row < row - 1) {
                    temp = temp.upPnt;
                    continue;
                } else if (temp.row == row - 1) {
                    NailErenSelimogluNode tempUp = temp.upPnt;

                    if (tempUp.upPnt != null) {
                        if (tempUp.rightPnt != null) {
                            tempUp.upPnt.rightPnt = tempUp.rightPnt;
                            tempUp.rightPnt.leftPnt = tempUp.upPnt;
                        } else {
                            tempUp.upPnt.rightPnt = null;
                        }
                        if (tempUp.leftPnt != null) {
                            tempUp.upPnt.leftPnt = tempUp.leftPnt;
                            tempUp.leftPnt.rightPnt = tempUp.upPnt;
                        } else {
                            tempUp.upPnt.leftPnt = null;
                        }
                    }

                    temp.upPnt = tempUp.upPnt;

                    NailErenSelimogluNode fix = tempUp.upPnt;
                    while (fix != null) {
                        fix.row--;
                        fix = fix.upPnt;
                    }
                    tempUp.upPnt = null;
                    tempUp.leftPnt = null;
                    tempUp.rightPnt = null;
                    break;
                }

            } else if (row == 0) {
                NailErenSelimogluNode tempRL = null;

                if (temp.col < col) {
                    if (temp.rightPnt != null && temp.rightPnt.col < col) {
                        temp = temp.rightPnt;
                        continue;
                    }
                    if (temp.rightPnt != null) {
                        tempRL = temp.rightPnt;
                        NailErenSelimogluNode tempUp = tempRL.upPnt;

                        if (tempUp != null) {
                            temp.rightPnt = tempUp;
                            tempUp.leftPnt = temp;

                            NailErenSelimogluNode fix = tempUp;
                            while (fix != null) {
                                fix.row--;
                                fix = fix.upPnt;
                            }

                            if (tempRL.rightPnt != null) {
                                tempRL.rightPnt.leftPnt = tempUp;
                                tempUp.rightPnt = tempRL.rightPnt;
                            }
                        }
                    }
                    tempRL.upPnt = null;
                    tempRL.leftPnt = null;
                    tempRL.rightPnt = null;
                    break;

                } else if (temp.col > col) {
                    if (temp.leftPnt != null && temp.leftPnt.col > col) {
                        temp = temp.leftPnt;
                        continue;
                    }

                    if (temp.leftPnt != null) {
                        tempRL = temp.leftPnt;
                        NailErenSelimogluNode tempUp = tempRL.upPnt;

                        if (tempUp != null) {
                            tempUp.rightPnt = temp;
                            temp.leftPnt = tempUp;

                            tempUp.leftPnt = tempRL.leftPnt;
                            if (tempRL.leftPnt != null) {
                                tempRL.leftPnt.rightPnt = tempUp;
                            }

                            NailErenSelimogluNode fix = tempUp;
                            while (fix != null) {
                                fix.row--;
                                fix = fix.upPnt;
                            }

                        } else {
                            temp.leftPnt = tempRL.leftPnt;
                            if (tempRL.leftPnt != null) {
                                tempRL.leftPnt.rightPnt = temp;
                            }
                        }
                        tempRL.upPnt = null;
                        tempRL.leftPnt = null;
                        tempRL.rightPnt = null;
                        break;
                    }
                }
            }
        }
    }

    public  int merge(NailErenSelimogluLinkedList list) {

        NailErenSelimogluNode temp = list.head;
        NailErenSelimogluNode tempUp = temp;
        while (temp != null) {
            if (temp.upPnt != null) {
                tempUp = temp.upPnt;
                if (tempUp.data == temp.data) {
                    delete(temp.row, temp.col);
                    tempUp.data *= 2;
                    return 1;
                } else {
                    while (tempUp.upPnt != null) {
                        NailErenSelimogluNode btm = tempUp;
                        tempUp = btm.upPnt;
                        if (btm.data == tempUp.data) {
                            delete(btm.row, btm.col);
                            tempUp.data *= 2;
                            return 1;
                        }

                    }
                    temp = temp.rightPnt;
                }
            } else {
                temp = temp.rightPnt;
            }

        }
        return 0;
    }

    public  int rowFinder(int col) {

        if (head == null) {
            return 0;
        }

        NailErenSelimogluNode temp = head;

        while (temp.col > col && temp.leftPnt != null) {
            temp = temp.leftPnt;
        }

        while (temp.col < col && temp.rightPnt != null) {
            temp = temp.rightPnt;
        }

        if (temp.col == col) {
            NailErenSelimogluNode tempUp = temp;
            while (tempUp.upPnt != null) {
                tempUp = tempUp.upPnt;
            }

            if (tempUp.row >= 6) {
                return -1;
            }

            return tempUp.row + 1;
        } else {

            return 0;
        }
    }
    
    public  boolean rowsFilled(){
        int count = 0;
        while (count < 5) {
            int result = rowFinder(count);
            
            if (result == -1) {
                return true;
            }
            count++;
        }
        
        return false;
    }
    
   

}
