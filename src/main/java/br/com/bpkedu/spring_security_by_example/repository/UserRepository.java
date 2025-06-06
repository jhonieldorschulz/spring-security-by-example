package br.com.bpkedu.spring_security_by_example.repository;

import br.com.bpkedu.spring_security_by_example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
} 