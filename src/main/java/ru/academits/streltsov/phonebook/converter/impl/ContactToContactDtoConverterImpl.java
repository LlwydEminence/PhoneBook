package ru.academits.streltsov.phonebook.converter.impl;

import org.springframework.stereotype.Service;
import ru.academits.streltsov.phonebook.converter.ContactToContactDtoConverter;
import ru.academits.streltsov.phonebook.dto.ContactDto;
import ru.academits.streltsov.phonebook.entity.Contact;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactToContactDtoConverterImpl implements ContactToContactDtoConverter {
    @Override
    public ContactDto convert(Contact contact) {
        ContactDto contactDto = new ContactDto();
        contactDto.setId(contact.getId());
        contactDto.setFirstName(contact.getFirstName());
        contactDto.setLastName(contact.getLastName());
        contactDto.setPhone(contact.getPhone());
        return contactDto;
    }

    @Override
    public List<ContactDto> convertList(List<Contact> contactList) {
        List<ContactDto> list = new ArrayList<>();
        for (Contact contact : contactList) {
            list.add(convert(contact));
        }
        return list;
    }
}
