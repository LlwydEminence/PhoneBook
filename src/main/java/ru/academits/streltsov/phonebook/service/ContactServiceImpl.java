package ru.academits.streltsov.phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.academits.streltsov.phonebook.bean.ContactValidation;
import ru.academits.streltsov.phonebook.dao.ContactDao;
import ru.academits.streltsov.phonebook.entity.Contact;

import java.util.LinkedList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactDao contactDao;

    @Autowired
    public ContactServiceImpl(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactDao.findAll();
    }

    private boolean isExistContactWithPhone(String phone) {
        List<Contact> contactList = getAllContacts();
        for (Contact contact : contactList) {
            if (contact.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    private ContactValidation validateContact(Contact contact) {
        ContactValidation contactValidation = new ContactValidation();
        contactValidation.setValid(true);
        if (contact.getFirstName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Имя должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getLastName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Фамилия должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getPhone().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Телефон должно быть заполнено.");
            return contactValidation;
        }

        if (isExistContactWithPhone(contact.getPhone())) {
            contactValidation.setValid(false);
            contactValidation.setError("Номер телефона не должен дублировать другие номера в телефонной книге.");
            return contactValidation;
        }
        return contactValidation;
    }

    @Override
    public ContactValidation addContact(Contact contact) {
        ContactValidation contactValidation = validateContact(contact);
        if (contactValidation.isValid()) {
            contactDao.save(contact);
        }
        return contactValidation;
    }

    @Override
    public void deleteContact(Contact contact) {
        contactDao.delete(contact);
    }

    @Override
    public void deleteContacts(List<Contact> contactList) {
        contactDao.delete(contactList);
    }

    @Override
    public List<Contact> filter(String filter) {
        String[] filters = filter.split(" ");
        List<Contact> contactList = contactDao.findAll();
        List<Contact> filteredContacts = new LinkedList<>();

        outer:
        for (Contact contact : contactList) {
            for (String filterPart : filters) {
                if (!contact.getFirstName().equalsIgnoreCase(filterPart) &&
                        !contact.getLastName().equalsIgnoreCase(filterPart) &&
                            !contact.getPhone().equalsIgnoreCase(filterPart)) {
                    continue outer;
                }
            }
            filteredContacts.add(contact);
        }
        return filteredContacts;
    }
}
