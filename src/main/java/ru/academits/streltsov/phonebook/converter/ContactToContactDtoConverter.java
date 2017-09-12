package ru.academits.streltsov.phonebook.converter;

import ru.academits.streltsov.phonebook.dto.ContactDto;
import ru.academits.streltsov.phonebook.entity.Contact;

import java.util.List;

public interface ContactToContactDtoConverter extends GenericConverter<Contact, ContactDto> {
    @Override
    ContactDto convert(Contact contact);

    @Override
    List<ContactDto> convertList(List<Contact> contactList);
}
