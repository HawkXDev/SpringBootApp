package com.sokol.springcourse.Project2Boot.repositories;

import com.sokol.springcourse.Project2Boot.models.Book;
import com.sokol.springcourse.Project2Boot.models.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByPerson(Person person);

    List<Book> findAllByOrderByYear(Pageable pageable);

    List<Book> findByTitleContaining(String searchQuery);
}
