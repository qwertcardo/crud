package com.qwertcardo.crud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qwertcardo.crud.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query(value = "SELECT p.* FROM tb_product p WHERE owner_id = ?1", nativeQuery = true)
	public abstract List<Product> getUsersProducts(long id);
}
