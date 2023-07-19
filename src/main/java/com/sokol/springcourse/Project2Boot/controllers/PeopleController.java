package com.sokol.springcourse.Project2Boot.controllers;

import com.sokol.springcourse.Project2Boot.models.Person;
import com.sokol.springcourse.Project2Boot.services.BookService;
import com.sokol.springcourse.Project2Boot.services.PeopleService;
import com.sokol.springcourse.Project2Boot.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final BookService bookService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, BookService bookService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.bookService = bookService;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping
    public String savePerson(@ModelAttribute("person") @Valid Person person, BindingResult result) {

        personValidator.validate(person, result);

        if (result.hasErrors()) {
            return "people/new";
        }

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        Optional<Person> person = peopleService.findById(id);
        if (person.isPresent()) {
            model.addAttribute("person", person.get());
            model.addAttribute("books", bookService.getBooksByPerson(person.get()));
            return "people/show";
        } else {
            return "redirect:/people";
        }
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        Optional<Person> person = peopleService.findById(id);
        if (person.isPresent()) {
            model.addAttribute("person", person.get());
            return "people/edit";
        } else {
            return "redirect:/people";
        }
    }

    @PatchMapping
    public String updatePerson(@ModelAttribute("person") @Valid Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "people/edit";
        }

        peopleService.update(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.deleteById(id);
        return "redirect:/people";
    }

}
