package com.sokol.springcourse.Project2Boot.controllers;

import com.sokol.springcourse.Project2Boot.models.Book;
import com.sokol.springcourse.Project2Boot.models.Person;
import com.sokol.springcourse.Project2Boot.services.BookService;
import com.sokol.springcourse.Project2Boot.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final PeopleService peopleService;
    private final BookService bookService;

    @Autowired
    public BookController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "page", required = false) Integer page,
                       @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                       @RequestParam(value = "sort_by_year", required = false) Boolean sortByYear) {
        if (page == null && booksPerPage == null) {
            model.addAttribute("books", bookService.getAllBooks(sortByYear));
        } else {
            model.addAttribute("books", bookService.getBooksByPage(page, booksPerPage, sortByYear));
        }
        return "books/list";
    }

    @GetMapping("/new")
    public String newForm(Model model, @ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping
    public String processNewForm(@Valid @ModelAttribute("book") Book book, BindingResult result) {
        if (result.hasErrors()) {
            return "books/new";
        }
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            model.addAttribute("people", peopleService.findAll());
            Optional<Person> person = Optional.ofNullable(book.get().getPerson());
            person.ifPresent(p -> model.addAttribute("person", p));
            return "books/show";
        } else {
            return "redirect:/books";
        }
    }

    @PatchMapping("/assign")
    public String assign(@RequestParam("person_id") int personId, @RequestParam("book_id") int bookId) {
        Optional<Person> person = peopleService.findById(personId);
        if (person.isPresent()) {
            bookService.setPerson(bookId, person.get());
            return "redirect:/books";
        }
        return null;
    }

    @PatchMapping("/free")
    public String free(@RequestParam("book_id") int bookId) {
        bookService.setPerson(bookId, null);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "books/edit";
        }
        return "redirect:/books";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult result, @PathVariable("id") int id) {
        if (result.hasErrors()) {
            return "books/edit";
        }
        bookService.updateBook(id, book);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search() {
        return "books/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam("q") String searchQuery, Model model) {
        model.addAttribute("searchQuery", searchQuery);
        List<Book> foundBooks = bookService.search(searchQuery);
        model.addAttribute("books", foundBooks);
        return "books/search";
    }

}
