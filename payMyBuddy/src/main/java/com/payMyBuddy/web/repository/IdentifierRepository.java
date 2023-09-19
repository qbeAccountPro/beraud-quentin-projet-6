package com.paymybuddy.web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.web.model.Identifier;

@Repository
public interface IdentifierRepository extends CrudRepository<Identifier, Integer>{
  
}
