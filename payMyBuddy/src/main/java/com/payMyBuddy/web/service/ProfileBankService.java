package com.paymybuddy.web.service;

import org.springframework.stereotype.Service;

import com.paymybuddy.web.dto.ProfilePage;
import com.paymybuddy.web.model.BankAccount;
import com.paymybuddy.web.model.User;

@Service
public class ProfileBankService {

  public ProfilePage getProfilePage(User user, BankAccount bankAccount) {
    ProfilePage profilePage = new ProfilePage();
    profilePage.setBalance(user.getBankBalance());
    profilePage.setBankName(bankAccount.getBankName());
    profilePage.setIBAN(bankAccount.getIBAN());
    return profilePage;
  }
}