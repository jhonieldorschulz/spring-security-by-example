package br.com.bpkedu.spring_security_by_example.service;

import br.com.bpkedu.spring_security_by_example.domain.User;
import br.com.bpkedu.spring_security_by_example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Tentando autenticar usuário: {}", username);
        
        return userRepository.findByUsername(username)
            .map(user -> {
                logger.debug("Usuário encontrado: {}, Role: {}", user.getUsername(), user.getRole());
                return new UserSecurity(user);
            })
            .orElseThrow(() -> {
                logger.debug("Usuário não encontrado: {}", username);
                return new UsernameNotFoundException("Usuário não encontrado: " + username);
            });
    }
}