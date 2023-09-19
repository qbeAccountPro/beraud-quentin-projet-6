package com.paymybuddy.web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.web.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}