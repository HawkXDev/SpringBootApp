package com.sokol.springcourse.Project2Boot.services;

import com.sokol.springcourse.Project2Boot.models.Book;
import com.sokol.springcourse.Project2Boot.models.Person;
import com.sokol.springcourse.Project2Boot.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksByPerson(Person person) {
        return bookRepository.findBooksByPerson(person);
    }

    public List<Book> getAllBooks(Boolean sortByYear) {
        if (Boolean.TRUE.equals(sortByYear)) {
            return bookRepository.findAllByOrderByYear(null);
        } else {
            return bookRepository.findAll();
        }
    }

    public Optional<Book> getBookById(int id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(int id, Book updatedBook) {
        Book bookToBeUpdated = bookRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setPerson(bookToBeUpdated.getPerson());

        bookRepository.save(updatedBook);
    }

    @Transactional
    public void setPerson(int bookId, Person person) {
        Book book = bookRepository.findById(bookId).get();
        book.setPerson(person);

        if (person != null) {
            book.setBookingTime(LocalDateTime.now());
        } else {
            book.setBookingTime(null);
        }

        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(int id) {
        Optional<Book> byId = bookRepository.findById(id);
        byId.ifPresent(bookRepository::delete);
    }

    public Object getBooksByPage(Integer page, Integer booksPerPage, Boolean sortByYear) {
        Pageable pageable = PageRequest.of(page, booksPerPage);
        if (Boolean.TRUE.equals(sortByYear)) {
            return bookRepository.findAllByOrderByYear(pageable);
        } else {
            return bookRepository.findAll(pageable);
        }
    }

    public List<Book> search(String searchQuery) {
        return bookRepository.findByTitleContaining(searchQuery);
    }

}
