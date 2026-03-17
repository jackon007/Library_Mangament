package org.example;


import java.sql.*;
import java.util.List;
import java.util.Scanner;
import org.example.modules.*;
import org.example.repo.BookRepository;


class Base{
    BookRepository bookRepository=new BookRepository();
    Scanner sc=new Scanner(System.in);

// Configure JDBC Connection
//    public String url="jdbc:postgresql://localhost:5432/Library";
//    String name="postgres";
//    String password="????";

    public int userId;
    public int serialNumber;
    public int userAge;
    public int bookCopyId;
    public int scannerId;
    public int genreId;

    public void createConnection (){
        try {
            Class.forName("org.postgresql.Driver");
            bookRepository.connection = DriverManager.getConnection(url,name,password);
            System.out.println("Connected");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(){
        try {
            bookRepository.connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void printBooks(){
        System.out.println("For search write Title or Author of Book OR just press enter to see all available books  " );
        System.out.println("Example -- Hidden Truth -- ✔✔ ");
        System.out.println("Wrong -- hidden truth -- xxx");
        String keyword=sc.nextLine();
        List<Book> books=bookRepository.getBooks(keyword);
        System.out.println("|- Id --|-- Title --|-- Author --|-- Genre -| ");
        books.forEach(Book::print);
    }

    public void createBook(){
        Book book=new Book();
        System.out.println("Enter the title of Book ! ");
        book.title=sc.nextLine();
        System.out.println("The Author of Book !");
        book.author=sc.nextLine();
        System.out.println("The Genre Id of Book !");
        book.genreId=sc.nextInt();
        bookRepository.addBook(book);
    }

    public void printUsers(){
        List<Users> usersList=bookRepository.getUsers();
        usersList.forEach(Users::print);
    }

    public void registerUser(){
        Users users=new Users();
        System.out.println("Enter user's name !");
        users.name=sc.next();
        System.out.println("Enter user's last name !");
        users.lastName=sc.next();
        System.out.println("Enter user's age !");
        users.age=sc.nextInt();
        System.out.println("Enter user's email !");
        users.email=sc.next();
        System.out.println("Enter user's phone number !");
        users.phoneNum=sc.next();
        bookRepository.addUser(users);
    }

    public void printCopies(){
        System.out.println("-- Copy Id -- Serial Number -- Book Id -- Library Id -- Status --");
        List<Copies> copies=bookRepository.getCopies();
        copies.forEach(Copies::print);
    }

    public void createCopies(){
        Copies copies=new Copies();
        System.out.println("Write or Scan serial number ! ");
        copies.serialNumber=sc.nextInt();
        System.out.println("Enter Book Id ! ");
        copies.bookId=sc.nextInt();
        System.out.println("Enter Library Id ! ");
        copies.libraryId=sc.nextInt();
        bookRepository.addCopies(copies);
    }

    public void printGenre(){
        List<Genres> genre=bookRepository.getGenre();
        genre.forEach(Genres::print);
    }

    public void createGenre(){
        Genres genres=new Genres();
        System.out.println("Enter new Genre");
        genres.name=sc.nextLine();
        bookRepository.addGenre(genres);
    }

    public void printLibrary(){
        List<Library> libraries=bookRepository.getLibrary();
        libraries.forEach(Library::print);
    }

    public void registerLibrary(){
        Library library=new Library();
        System.out.println("Enter a new Library Name");
        library.name=sc.nextLine();
        bookRepository.addLibrary(library);
    }

    public void printLoaners(){
        String sql;
        sql="select loan_id,u.user_name,u.phone_number,loan_date,due_date,return_date,loan_status,copy_id from loan l join users u on l.user_id=u.user_id;";
        try {
            Statement displayLoans= bookRepository.connection.createStatement();
            ResultSet resultSet=displayLoans.executeQuery(sql);
            while (resultSet.next()){
                System.out.print("Loan id is : "+resultSet.getInt(1)+" -- ");
                System.out.print("User name : "+resultSet.getString(2)+" -- ");
                System.out.print("User phone Number : "+resultSet.getString(3)+" -- ");
                System.out.print("Give date : "+resultSet.getString(4)+" -- ");
                System.out.print("Finishing date : "+resultSet.getString(5)+" -- ");
                System.out.print("Return date : "+resultSet.getString(6)+" -- ");
                System.out.print("Loan Status : "+resultSet.getString(7)+" -- ");
                System.out.println("Book copy id : "+resultSet.getString(8));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sectionLoan(){
        String sql;
        sql=" select book_copies_id from book_copies where serial_number=? ";
        try {
            System.out.println("-- Loan section --");
            System.out.println("Press 1 : Rent a book !");
            System.out.println("Press 2 : Return a book !");
            scannerId=sc.nextInt();

            PreparedStatement getBookId;
            getBookId=bookRepository.connection.prepareStatement(sql);
            System.out.println("Enter user Id !");
            userId=sc.nextInt();
            System.out.println("Enter book's serial number or scan it ! ");
            serialNumber=sc.nextInt();
            getBookId.setInt(1,serialNumber);
            ResultSet findBookId = getBookId.executeQuery();
            while (findBookId.next()){
                bookCopyId=findBookId.getInt(1);}
            if(scannerId==1) {
                sql="select user_age from users where user_id=?";
                getBookId =bookRepository.connection.prepareStatement(sql);
                getBookId.setInt(1,userId);
                ResultSet resultSet =getBookId.executeQuery();
                while (resultSet.next()){
                    userAge=resultSet.getInt(1);}
                sql="select genre_id from books b join book_copies bc on b.book_id=bc.book_id where bc.book_copies_id=?;";
                getBookId=bookRepository.connection.prepareStatement(sql);
                getBookId.setInt(1,bookCopyId);
                ResultSet getGenred=getBookId.executeQuery();
                while (getGenred.next()){
                    genreId=getGenred.getInt(1);}

//                #if user's age is lower than any age, we can restrict to take any type of book
//                #here if user's age is under 15 ,user can not borrow adult books
                if(userAge<15){
                    if (genreId!=25) {
                        sql = "insert into loan(user_id,copy_id) values(?,?)";
                        getBookId = bookRepository.connection.prepareStatement(sql);
                        getBookId.setInt(1, userId);
                        getBookId.setInt(2, bookCopyId);
                        getBookId.execute();
                        System.out.println("Loan is processed");
                        sql= "update book_copies set status='Borrowed' where book_copies_id=?;";
                        getBookId = bookRepository.connection.prepareStatement(sql);
                        getBookId.setInt(1, bookCopyId);
                        getBookId.executeUpdate();
                    }
                    else System.out.println("you are not allowed to get this type of book because of age");
                }
                else {
                    sql = "insert into loan(user_id,copy_id) values(?,?)";
                    getBookId = bookRepository.connection.prepareStatement(sql);
                    getBookId.setInt(1, userId);
                    getBookId.setInt(2, bookCopyId);
                    getBookId.execute();
                    System.out.println("Loan is processed");
                    sql = "update book_copies set status='Borrowed' where book_copies_id=?;";
                    getBookId = bookRepository.connection.prepareStatement(sql);
                    getBookId.setInt(1, bookCopyId);
                    getBookId.executeUpdate();
                }
            }
            else if (scannerId == 2){
                sql="UPDATE loan SET return_date = CURRENT_DATE ,loan_status='Returned' WHERE copy_id = ?;";
                getBookId=bookRepository.connection.prepareStatement(sql);
                getBookId.setInt(1,bookCopyId);
                getBookId.executeUpdate();
                System.out.println("Loan is returned ! Thanks for being with us !");

                sql="update book_copies set status='available' where book_copies_id =?";
                getBookId=bookRepository.connection.prepareStatement(sql);
                getBookId.setInt(1,bookCopyId);
                getBookId.executeUpdate();
            }
            else System.out.println("You enter invalid number !!!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

public class Demo {
    public static void main(String[] args) {
        Base base = new Base();
        base.createConnection();
        Scanner scan = new Scanner(System.in);
        int scanId;
        while (true) {
            System.out.println("Choose a number ! ");
            System.out.println(" -- 1 : Available Books -- ");
            System.out.println(" -- 2 : Genres --See all genres or add new one -- ");
            System.out.println(" -- 3 : Books Information --");
            System.out.println(" -- 4 : Register User -- ");
            System.out.println(" -- 5 : Loan Section -- Borrow or Return  books ");
            System.out.println(" -- 6 : Loaners List -- ");
            System.out.println(" -- 7 : Add new Book -- ");
            System.out.println(" -- 8 : Print Users -- ");
            System.out.println(" -- 9 : Add Information to new Books -- ");
            System.out.println(" -- 10 : Libraries");

            System.out.println(" -- 0 : Exit -- ");
            if (!scan.hasNextInt()) {
                System.out.println("Please enter a number!");
                scan.next();
                continue;
            }
            scanId = scan.nextInt();

            switch (scanId) {
                case 1:
                   base.printBooks();
                break;
                case 2:
                    System.out.println("Press 1 to see all genres");
                    System.out.println("Press 2 to add new genre");
                    scanId= scan.nextInt();
                    if (scanId==1){
                    base.printGenre();
                    } else if (scanId==2) {
                        base.createGenre();
                    }
                    else System.out.println("write only 1 or 2");
                    break;
                case 3:
                    base.printCopies();
                    break;
                case 4:
                    base.registerUser();
                    break;
                case 5:
                    base.sectionLoan();
                    break;
                case 6:
                    base.printLoaners();
                    break;
                case 7:
                    base.createBook();
                    break;
                case 8:
                    base.printUsers();
                    break;
                case 9:
                    base.createCopies();
                    break;
                case 10:
                    System.out.println("Press 1 to see all Libraries ");
                    System.out.println("Press 2 to add new Library ");
                    scanId= scan.nextInt();
                    if (scanId==1){
                        base.printLibrary();
                    } else if (scanId==2) {
                        base.registerLibrary();
                    }
                    else System.out.println("write only 1 or 2");
                    break;
                case 0 : System.out.println("Exiting program...");
                        base.closeConnection();
                        scan.close();
                        base.sc.close();
                        return;
                default:
                    System.out.println("Make sure you entered correct number !!!");
                    System.out.println("Try again ");
                    break;
            }
        }
    }
}
