package com.paymybuddy.web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.web.model.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {

}
