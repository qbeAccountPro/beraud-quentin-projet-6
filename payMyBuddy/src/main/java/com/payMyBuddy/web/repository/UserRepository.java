package com.paymybuddy.web.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.web.model.User;

/**
 * Some javadoc.
 * 
 * Repository interface for managing User entities.
 * This interface provides CRUD (Create, Read, Update, Delete) operations for
 * User objects.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
  User getUserByFirstname(String firstName);

  List<User> findAllByIdIn(List<Integer> ids);

  User findUserByMail(String mail);
}