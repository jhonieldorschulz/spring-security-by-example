package br.com.bpkedu.spring_security_by_example.repository;

import br.com.bpkedu.spring_security_by_example.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
} 