package ru.academits.streltsov.phonebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.academits.streltsov.phonebook.bean.ContactValidation;
import ru.academits.streltsov.phonebook.converter.ContactDtoToContactConverter;
import ru.academits.streltsov.phonebook.converter.ContactToContactDtoConverter;
import ru.academits.streltsov.phonebook.dao.ContactDao;
import ru.academits.streltsov.phonebook.dto.ContactDto;
import ru.academits.streltsov.phonebook.entity.Contact;
import ru.academits.streltsov.phonebook.service.ContactService;

import java.util.List;

@RestController
@RequestMapping("/phoneBook/rcp/api/v1/")
public class ContactController {
    private final ContactService contactService;
    private final ContactToContactDtoConverter contactToContactDtoConverter;
    private final ContactDtoToContactConverter contactDtoToContactConverter;

    @Autowired
    public ContactController(ContactService contactService, ContactToContactDtoConverter contactToContactDtoConverter,
                             ContactDtoToContactConverter contactDtoToContactConverter) {
        this.contactService = contactService;
        this.contactToContactDtoConverter = contactToContactDtoConverter;
        this.contactDtoToContactConverter = contactDtoToContactConverter;
    }

    @RequestMapping(value = "getAllContacts", method = RequestMethod.GET)
    public List<ContactDto> getAllContacts() {
        return contactToContactDtoConverter.convertList(contactService.getAllContacts());
    }

    @RequestMapping(value = "addContact", method = RequestMethod.POST)
    public ContactValidation addContact(@RequestBody ContactDto contactDto) {
        Contact contact = contactDtoToContactConverter.convert(contactDto);
        return contactService.addContact(contact);
    }

    @RequestMapping(value = "deleteContact", method = RequestMethod.POST)
    public void deleteContact(@RequestBody ContactDto contactDto) {
        Contact contact = contactDtoToContactConverter.convert(contactDto);
        contactService.deleteContact(contact);
    }

    @RequestMapping(value = "deleteContacts", method = RequestMethod.POST)
    public void deleteContacts(@RequestBody List<ContactDto> contactDtoList) {
        List<Contact> contactList = contactDtoToContactConverter.convertList(contactDtoList);
        contactService.deleteContacts(contactList);
    }

    @RequestMapping(value = "getFilteredContacts", method = RequestMethod.GET)
    public List<ContactDto> getFilteredContacts(@RequestParam String filter) {
        return contactToContactDtoConverter.convertList(contactService.filter(filter));
    }
}
