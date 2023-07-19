package com.sokol.springcourse.Project2Boot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters long")
    @Column(name = "title")
    private String title;

    @NotEmpty
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters long")
    @Column(name = "author")
    private String author;

    @NotEmpty
    @Pattern(regexp = "^\\d{4}$", message = "Year of birth must be a four digit")
    @Column(name = "year")
    private String year;

    @Column(name = "booking_time")
    private LocalDateTime bookingTime;

    @Transient
    private boolean isOverdue;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public Book() {
    }

    public Book(int id, String title, String author, String year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public boolean isOverdue() {
        if (this.bookingTime != null) {
            Duration duration = Duration.between(this.bookingTime, LocalDateTime.now());
            return duration.toDays() > 10;
        } else {
            return false;
        }
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
