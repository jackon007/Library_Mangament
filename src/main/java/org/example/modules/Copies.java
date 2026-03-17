package org.example.modules;

public class Copies{
    public int id;
    public int serialNumber;
    public int bookId;
    public int libraryId;
    public String status;
    public void print(){
        System.out.println(id+" : "+serialNumber+" : "+bookId+" : "+libraryId+" : "+status);
    };
}
