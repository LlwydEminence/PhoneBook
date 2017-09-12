package ru.academits.streltsov.phonebook.dao;

import org.springframework.data.repository.CrudRepository;
import ru.academits.streltsov.phonebook.entity.Contact;

import java.util.List;

public interface ContactDao extends CrudRepository<Contact, Integer> {
    List<Contact> findAll();
    List<Contact> findAllByFirstNameIn(String[] firstName);
}
