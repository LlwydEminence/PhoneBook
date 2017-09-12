package ru.academits.streltsov.phonebook.converter;

import ru.academits.streltsov.phonebook.dto.ContactDto;
import ru.academits.streltsov.phonebook.entity.Contact;

import java.util.List;

public interface ContactDtoToContactConverter extends GenericConverter<ContactDto, Contact> {
    @Override
    Contact convert(ContactDto contactDto);

    @Override
    List<Contact> convertList(List<ContactDto> contactDtoList);
}
