package com.qwertcardo.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qwertcardo.crud.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
