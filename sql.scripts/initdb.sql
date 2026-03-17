ll-- Database: Library

-- DROP DATABASE IF EXISTS "Library";

CREATE DATABASE "Library"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    ICU_LOCALE = 'en-US'
    LOCALE_PROVIDER = 'icu'
    TABLESPACE = pg_defaultm
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
-- Table: public.libraries

-- DROP TABLE IF EXISTS public.libraries;

CREATE TABLE IF NOT EXISTS public.libraries
(
    library_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    librar_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT libraries_pkey PRIMARY KEY (library_id),
    CONSTRAINT libraries_librar_name_key UNIQUE (librar_name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.libraries
    OWNER to postgres;
	
-- Table: public.genres

-- DROP TABLE IF EXISTS public.genres;

CREATE TABLE IF NOT EXISTS public.genres
(
    genre_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    genre_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT genres_pkey PRIMARY KEY (genre_id),
    CONSTRAINT genres_genre_name_key UNIQUE (genre_name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.genres
    OWNER to postgres;
-- Table: public.books

-- DROP TABLE IF EXISTS public.books;

CREATE TABLE IF NOT EXISTS public.books
(
    book_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    book_title character varying(100) COLLATE pg_catalog."default" NOT NULL,
    author character varying(150) COLLATE pg_catalog."default" NOT NULL,
    genre_id integer,
    CONSTRAINT books_pkey PRIMARY KEY (book_id),
    CONSTRAINT books_genre_id_fkey FOREIGN KEY (genre_id)
        REFERENCES public.genres (genre_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.books
    OWNER to postgres;
-- Table: public.book_copies

-- DROP TABLE IF EXISTS public.book_copies;

CREATE TABLE IF NOT EXISTS public.book_copies
(
    book_copies_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    serial_number integer NOT NULL,
    book_id integer,
    library_id integer,
    status character varying(50) COLLATE pg_catalog."default" DEFAULT 'available'::character varying,
    CONSTRAINT book_copies_pkey PRIMARY KEY (book_copies_id),
    CONSTRAINT book_copies_serial_number_key UNIQUE (serial_number),
    CONSTRAINT book_copies_book_id_fkey FOREIGN KEY (book_id)
        REFERENCES public.books (book_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT book_copies_library_id_fkey FOREIGN KEY (library_id)
        REFERENCES public.libraries (library_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.book_copies
    OWNER to postgres;
	-- Table: public.users

-- DROP TABLE IF EXISTS public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    user_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    user_last_name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    user_age integer NOT NULL,
    user_email character varying(150) COLLATE pg_catalog."default" NOT NULL,
    phone_number character varying(50) COLLATE pg_catalog."default" NOT NULL,
    registration_date date NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT users_phone_number_key UNIQUE (phone_number),
    CONSTRAINT users_user_email_key UNIQUE (user_email)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;
	-- Table: public.loan

-- DROP TABLE IF EXISTS public.loan;

CREATE TABLE IF NOT EXISTS public.loan
(
    loan_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    user_id integer,
    loan_date date NOT NULL DEFAULT CURRENT_DATE,
    due_date date NOT NULL DEFAULT (CURRENT_DATE + '7 days'::interval),
    return_date date,
    loan_status character varying(20) COLLATE pg_catalog."default" NOT NULL DEFAULT 'Active'::character varying,
    copy_id bigint NOT NULL,
    CONSTRAINT loan_pkey PRIMARY KEY (loan_id),
    CONSTRAINT copy_id_fk FOREIGN KEY (copy_id)
        REFERENCES public.book_copies (book_copies_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT loan_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.loan
    OWNER to postgres;
	-- Table: public.hold

-- DROP TABLE IF EXISTS public.hold;

CREATE TABLE IF NOT EXISTS public.hold
(
    hold_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    user_id integer,
    serial_number integer,
    hold_date date NOT NULL,
    release_date date NOT NULL,
    status character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT hold_pkey PRIMARY KEY (hold_id),
    CONSTRAINT hold_serial_number_fkey FOREIGN KEY (serial_number)
        REFERENCES public.book_copies (serial_number) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT hold_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.hold
    OWNER to postgres;
	-- Table: public.market

-- DROP TABLE IF EXISTS public.market;

CREATE TABLE IF NOT EXISTS public.market
(
    m_book_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    m_book_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    m_book_author character varying(100) COLLATE pg_catalog."default" NOT NULL,
    genre_id integer,
    price character varying(20) COLLATE pg_catalog."default" NOT NULL,
    serial_number integer,
    CONSTRAINT market_pkey PRIMARY KEY (m_book_id),
    CONSTRAINT market_genre_id_fkey FOREIGN KEY (genre_id)
        REFERENCES public.genres (genre_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT market_serial_number_fkey FOREIGN KEY (serial_number)
        REFERENCES public.book_copies (serial_number) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.market
    OWNER to postgres;