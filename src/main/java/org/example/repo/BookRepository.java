package org.example.repo;

import org.example.modules.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookRepository {
    Scanner sc=new Scanner(System.in);
    public Connection connection;

    public List<Book> getBooks(String keyword ) {
        String sql;
        if (keyword == null || keyword.isBlank()) {
            sql = "select book_id,book_title,author,genre_id from books;";
        } else {
            sql = "select book_id,book_title,author,genre_id from books where book_title like  '%" + keyword + "%' or author like  '%" + keyword + "%';";
        }
        List<Book> list = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Book book = new Book();
                book.id = resultSet.getInt("book_id");
                book.title = resultSet.getString("book_title");
                book.author = resultSet.getString("author");
                book.genreId = resultSet.getInt("genre_id");
                list.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public void addBook(Book book){
        String sql;
        sql ="insert into books(book_title,author,genre_id) values(?,?,?)";
        try {
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setString(1, book.title);
            statement.setString(2,book.author);
            statement.setInt(3,book.genreId);
            statement.executeUpdate();
            System.out.println("Book added successfully !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Users> getUsers(){
        List<Users> list = new ArrayList<>();
        String sql;
        sql ="select user_id,user_name,user_last_name,user_age,user_email,phone_number,registration_date from users";
        try (Statement statement =connection.createStatement()){
            ResultSet resultSet=statement.executeQuery(sql);
            while (resultSet.next()){
                Users user=new Users();
                user.id=resultSet.getInt("user_id");
                user.name=resultSet.getString("user_name");
                user.lastName =resultSet.getString("user_last_name");
                user.age=resultSet.getInt("user_age");
                user.email=resultSet.getString("user_email");
                user.phoneNum=resultSet.getString("phone_number");
                user.registrationDate=resultSet.getString("registration_date");
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public void addUser(Users users){
        String sql;
        sql="insert into users(user_name, user_last_name, user_age, user_email, phone_number) values(?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,users.name);
            statement.setString(2,users.lastName);
            statement.setInt(3,users.age);
            statement.setString(4,users.email);
            statement.setString(5,users.phoneNum);
            statement.executeUpdate();
            System.out.println("Successfully registered");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Copies> getCopies(){
        String sql;
        List<Copies> list=new ArrayList<>();
        sql="select book_copies_id,serial_number,book_id,library_id,status from book_copies";
        try {
            Statement displayBookCopies = connection.createStatement();
            ResultSet resultSet = displayBookCopies.executeQuery(sql);
            while (resultSet.next()){
                Copies copies=new Copies();
                copies.id=resultSet.getInt(1);
                copies.serialNumber=resultSet.getInt(2);
                copies.bookId=resultSet.getInt(3);
                copies.libraryId=resultSet.getInt(4);
                copies.status=resultSet.getString(5);
                list.add(copies);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public void addCopies(Copies copies){
        String sql;
        sql="insert into book_copies(serial_number,book_id,library_id) values(?,?,?)";
        try {
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setInt(1,copies.serialNumber);
            statement.setInt(2,copies.bookId);
            statement.setInt(3,copies.libraryId);
            statement.executeUpdate();
            System.out.println("Added Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Genres>getGenre(){
        String sql;
        List<Genres> list=new ArrayList<>();
        sql="select * from genres";
        try {
            Statement displayGenres = connection.createStatement();
            ResultSet resultSet = displayGenres.executeQuery(sql);
            while (resultSet.next()) {
                Genres genres =new Genres();
                genres.id=resultSet.getInt(1);
                genres.name=resultSet.getString(2);
                list.add(genres);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addGenre(Genres genres){
        String sql ="insert into genres(genre_name) values (?);";
        try {
            PreparedStatement addGenres = connection.prepareStatement(sql);
            addGenres.setString(1,genres.name);
            addGenres.executeUpdate();
            System.out.println("Successfully added");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public List<Library> getLibrary() {
        List<Library> list = new ArrayList<>();
        String sql ;
        sql = "select library_id,librar_name from libraries";
//        #librar_name wrong pronunced sorry it must be library_name
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Library library = new Library();
                library.id = resultSet.getInt("library_id");
                library.name = resultSet.getString("librar_name");
                list.add(library);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public void addLibrary(Library library){
        String sql;
        sql="insert into libraries(librar_name) values (?);";
        try {
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setString(1,library.name);
            statement.executeUpdate();
            System.out.println("Successfully added");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
