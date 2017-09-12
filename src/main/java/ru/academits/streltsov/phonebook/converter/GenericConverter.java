package ru.academits.streltsov.phonebook.converter;

import java.util.List;

public interface GenericConverter<T, S> {
    S convert(T source);

    List<S> convertList(List<T> sourceList);
}
