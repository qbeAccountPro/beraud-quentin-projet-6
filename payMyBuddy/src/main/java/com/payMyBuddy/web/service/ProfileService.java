package com.paymybuddy.web.service;

import org.springframework.stereotype.Service;

import com.paymybuddy.web.dto.ProfileDto;
import com.paymybuddy.web.model.BankAccount;
import com.paymybuddy.web.model.User;

/**
 * Some javadoc.
 * 
 * Service class responsible for "Profile" entities.
 * This class contains business logic and serves as an intermediary between
 * controllers and repositories.
 */
@Service
public class ProfileService {

  /**
   * Some javadoc.
   * 
   * This class represent the method getProfile to load all information for the
   * profile page.
   * 
   * @param user        is the "user" entity currently connected.
   * 
   * @param bankAccount is the "bankAccount" entity from the currently connected
   *                    user.
   * 
   * @return "ProfileDto" is the profile entity for data transfer object.
   */
  public ProfileDto getProfile(User user, BankAccount bankAccount) {
    ProfileDto profilePage = new ProfileDto();
    profilePage.setBalance(user.getBankBalance());
    profilePage.setBankName(bankAccount.getBankName());
    profilePage.setIBAN(bankAccount.getIBAN());
    return profilePage;
  }
}