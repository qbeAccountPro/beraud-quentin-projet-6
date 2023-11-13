package com.paymybuddy.web.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.web.model.User;

/**
 * Some javadoc.
 * 
 * Repository interface for managing User entities.
 * This interface provides CRUD (Create, Read, Update, Delete) operations for
 * User objects.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  User findByFirstName(String firstName);

  User findById(int id);

  List<User> findAllByIdIn(List<Integer> ids);

  User findByMail(String mail);

  @Transactional
  @Modifying
  @Query("UPDATE User u SET u.bankBalance = :newBalance WHERE u.id = :userId")
  void updateBankBalance(@Param("userId") int userId, @Param("newBalance") BigDecimal newBalance);
}