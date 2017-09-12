package ru.academits.streltsov.phonebook.service;

import ru.academits.streltsov.phonebook.bean.ContactValidation;
import ru.academits.streltsov.phonebook.entity.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> getAllContacts();
    ContactValidation addContact(Contact contact);
    void deleteContact(Contact contact);
    void deleteContacts(List<Contact> contactList);
    List<Contact> filter(String filter);
}
