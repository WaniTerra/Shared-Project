/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsClasses;

/**
 *
 * @author erens
 */
public class MyLinkedList<T> {

    private Node<T> head;
    private int size;
    private Node<T> tail;

    public MyLinkedList() {
        head = null;
        size = 0;
    }

    public Node<T> getHead() {
        return head;
    }

    public void add(T data) {

        Node<T> newNode = new Node<>(data);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {

            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    public T get(int index) {

        Node<T> temp = head;

        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }

        return temp.data;
    }

    public int size() {
        return size;
    }

    public void removeLast() {

        if (head == null) {
            return;
        }

        if (head.next == null) {
            head = null;
            size = 0;
            return;
        }

        Node<T> temp = head;

        while (temp.next.next != null) {
            temp = temp.next;
        }

        temp.next = null;
        size--;
    }

    public void set(int index, T data) {

        Node<T> temp = head;

        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }

        temp.data = data;
    }

    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}
