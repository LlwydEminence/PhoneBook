function ContactForClient(contact, i)  {
    this.id = contact.id;
    this.firstName = contact.firstName;
    this.lastName = contact.lastName;
    this.phone = contact.phone;
    this.checked = ko.observable(false);
    this.number = i + 1;
}

function ContactForServer(firstName, lastName, phone) {
    this.firstName = firstName();
    this.lastName = lastName();
    this.phone = phone();
}

function contactToString(contact) {
    var note = "(";
    note += contact.firstName + ", ";
    note += contact.lastName + ", ";
    note += contact.phone;
    note += ")";
    return note;
}

function PhoneBookViewModel() {
    var self = this;

    self.validation = ko.observable(false);

    self.serverValidation = ko.observable(false);

    self.filter = ko.observable("");
    self.firstName = ko.observable("");
    self.lastName = ko.observable("");
    self.phone = ko.observable("");

    self.firstNameError = ko.computed(function () {
        if (self.firstName()) {
            return {
                message: "",
                error: false
            };
        }
        return {
            message: "Поле Имя должно быть заполнено.",
            error: true
        };
    });

    self.lastNameError = ko.computed(function () {
        if (!self.lastName()) {
            return {
                message: "Поле Фамилия должно быть заполнено.",
                error: true
            };
        }
        return {
            message: "",
            error: false
        };
    });

    self.phoneError = ko.computed(function () {
        if (!self.phone()) {
            return {
                message: "Поле Телефон должно быть заполнено.",
                error: true
            };
        }
        var sameContact = _.find(self.contacts(), {phone: self.phone()});

        if (sameContact) {
            return {
                message: "Номер телефона не должен дублировать другие номера в телефонной книге.",
                error: true
            };
        }
        return {
            message: "",
            error: false
        };
    });

    self.serverError = ko.observable("");
    self.hasError = ko.computed(function () {
        return self.lastNameError().error || self.firstNameError().error || self.phoneError().error;
    });

    self.contacts = ko.observableArray([]);

    self.fillContacts = function (contactsFromServer) {
        var mappedContacts = $.map(contactsFromServer, function (contact, i) {
            return new ContactForClient(contact, i);
        });

        self.contacts(mappedContacts);
    };

    self.ajaxForGetContacts = function () {
        $.ajax({
            type: "GET",
            url: "/phoneBook/rcp/api/v1/getAllContacts",
            success: self.fillContacts
        });
    };


    self.ajaxForDelete = function (data, url) {
        $.ajax({
            type: "POST",
            url: url,
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data)
        }).always(self.getContacts);
    };

    self.filterContacts = function () {
        var filter = self.filter();
        if (!filter) {
            self.ajaxForGetContacts();
            return;
        }

        $.ajax({
            type: "GET",
            url: "/phoneBook/rcp/api/v1/getFilteredContacts",
            success: self.fillContacts,
            data: {filter: filter}
        });
        return true;
    };

    self.getContacts = function () {
        if (self.filterContacts()) {
            return;
        }

        self.ajaxForGetContacts();
    };

    self.resetFilter= function () {
        self.filter("");
        self.ajaxForGetContacts();
    };

    self.addContact = function () {
        if (self.hasError()) {
            self.validation(true);
            self.serverValidation(false);
            return;
        }

        var contact = new ContactForServer(self.firstName, self.lastName, self.phone);
        $.ajax({
            type: "POST",
            url: "/phoneBook/rcp/api/v1/addContact",
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(contact)
        }).done(function() {
            self.serverValidation(false);
        }).fail(function(ajaxRequest) {
            var contactValidation = $.parseJSON(ajaxRequest.responseText);
            self.serverError(contactValidation.error);
            self.serverValidation(true);
        }).always(self.getContacts);

        self.firstName("");
        self.lastName("");
        self.phone("");
        self.validation(false);
    };
    self.deleteContact = function (contact) {
        var content = "Вы уверены, что хотите удалить контакт:\r\n" + contactToString(contact) + "?";
        var deleteDialogSelector = $(".delete-dialog");
        openDeleteDialog(deleteDialogSelector, content, function () {
            self.ajaxForDelete(contact, "/phoneBook/rcp/api/v1/deleteContact");
        });
    };

    self.deleteContacts = function () {
        var contacts = $.map(self.contacts(), function (contact) {
            if (contact.checked()) {
                return contact;
            }
        });

        if (contacts.length === 0) {
            $(".delete-checked-alert").text("Выберите контакты в таблице!").dialog({
                modal: true,
                buttons: {
                    OK: function () {
                        $(this).dialog("close");
                    }
                }
            });
            return;
        }

        var deleteDialogSelector = $(".delete-checked-dialog");
        var content = "Вы уверены, что хотите удалить выбранные контакты?";
        openDeleteDialog(deleteDialogSelector, content, function () {
            self.ajaxForDelete(contacts, "/phoneBook/rcp/api/v1/deleteContacts");
        });
    };


    self.ajaxForGetContacts();
}

function openDeleteDialog(deleteDialogSelector, content, onOk, onCancel) {
    deleteDialogSelector.text(content).dialog({
        autoOpen: false,
        modal: true,
        buttons: {
            "Удалить": function () {
                onOk.call();
                $(this).dialog("close");
            },
            "Отмена": function () {
                if (onCancel) {
                    onCancel.call();
                }
                $(this).dialog("close");
            }
        }
    });
    deleteDialogSelector.dialog("open");
}

$(document).ready(function () {
    var phoneBookViewModel = new PhoneBookViewModel();
    ko.applyBindings(phoneBookViewModel);

    $.fn.bootstrapBtn = $.fn.button.noConflict();
});
