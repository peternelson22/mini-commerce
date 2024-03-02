package org.nelson.userservice.service;

import org.nelson.userservice.entity.User;

public interface UserService {
  User findUserByUsername(String username);
}
