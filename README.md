# CLI Library Management System

Java JDBC console application to manage multi-library book loans with PostgreSQL.  
This system allows users to borrow books, track book copies, and manage multiple libraries. 
The project demonstrates full database interaction, console-based UI, and automated loan logic.

# Download File Via Repsoitory
     git@github.com:jackon007/Library_Mangament.git

# Requirements
- Java (JDK 17+)
- JDBC
- PostgreSQL
- Gradle (build tool)
- CLI

# How to Run the Project
- Copy  SQL script from **sql.scripts** and run it in pgAdmin4 to create the database and tables.
- Configure JDBC Connection
  In Demo.java (Line 15)  Uncomment the part and change information with yours !!!
  **String url = "jdbc:postgresql://localhost:5432/Library";** -- You Url 
  **String user = "postgres";** 
  **String password = "your_password";** -- Your postgres Password 
- Run the Demo.java


## Library-Management-System
│
├── README.md
├── build.gradle
├── settings.gradle
├── .gitignore
│ 
├── sql.scripts
│   └── initdb.sql       # Database setup script
│
└── src
     └── main/java/org.example
     |                ├── modules
     |                │    ├──  Book.java
     |                │    ├── Copies.java
     |                │    ├── Genres.java
     |                │    ├── Library.java
     |                │    └── Users.java
     |                │
     |                ├── repo
     |                |     └── BookRepository.java
     |                └── Demo.java
     └──test

## Features
- Manage **multiple libraries**, each storing physical copies of books.
- Categorize books by **genres**.
- Maintain **users** and their information.
- Track **physical book copies** separately from book data.
- Borrowing system with **loan dates, due dates, and return dates**.
- **Age restrictions** on books (e.g., users under 15 cannot borrow adult books).
- Automatically update **loan status** and **book copy status**.
- Add new books, genres, and libraries dynamically.
- **Search books by title or author** before listing all books.

---

### ER Diagram
**Logical workflow of the system**:

Libraries        Genres        Users
    │              │              │
    │              ▼              │
    │            Books            │
    │              │              │
    └──────────► Book_Copies ◄────┘
                     │
                     ▼
                    Loan
                     ▲
                     │
                    Users


#### Console Menu
Choose a number!
-- 1 : Available Books --
-- 2 : Genres -- See all genres or add new one --
-- 3 : Books Information --
-- 4 : Register User --
-- 5 : Loan Section -- Borrow or Return books
-- 6 : Loaners List --
-- 7 : Add new Book --
-- 8 : Print Users --
-- 9 : Add Information to new Books --
-- 10 : Libraries
-- 0 : Exit --

##### Instruction to Test
- Create Libraries.
- Create Genres.
- Register Users.
- Add Books linked to genres.
- Add Book Copies to libraries (with serial numbers and initial status available).
- Users borrow books through the Loan system.
- System automatically sets: Loan date
                             Due date (default 7 days)
                             Return date (when returned)
                             Book copy status (available or borrowed)