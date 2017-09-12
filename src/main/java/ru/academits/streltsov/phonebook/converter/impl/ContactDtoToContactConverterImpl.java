package ru.academits.streltsov.phonebook.converter.impl;

import org.springframework.stereotype.Service;
import ru.academits.streltsov.phonebook.converter.ContactDtoToContactConverter;
import ru.academits.streltsov.phonebook.dto.ContactDto;
import ru.academits.streltsov.phonebook.entity.Contact;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactDtoToContactConverterImpl implements ContactDtoToContactConverter {
    @Override
    public Contact convert(ContactDto contactDto) {
        Contact contact = new Contact();
        int contactDtoId = contactDto.getId();

        if (contactDtoId != 0) {
            contact.setId(contactDtoId);
        }

        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setPhone(contactDto.getPhone());
        return contact;
    }

    @Override
    public List<Contact> convertList(List<ContactDto> contactDtoList) {
        List<Contact> contactList = new ArrayList<>();
        for (ContactDto contactDto : contactDtoList) {
            contactList.add(convert(contactDto));
        }
        return contactList;
    }
}
