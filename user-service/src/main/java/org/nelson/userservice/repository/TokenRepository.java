package org.nelson.userservice.repository;

import org.nelson.userservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t from Token t INNER JOIN User u on t.user.id = u.id WHERE t.user.id = :userId and t.isLoggedOut = false")
    List<Token> findAllTokenByUser(long userId);

    Optional<Token> findByToken(String token);
}
