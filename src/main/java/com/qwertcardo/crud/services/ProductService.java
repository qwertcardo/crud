package com.qwertcardo.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qwertcardo.crud.models.Product;
import com.qwertcardo.crud.models.ProductSell;
import com.qwertcardo.crud.models.User;
import com.qwertcardo.crud.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	public Product save(long id, Product product) {
		User user = new User();
		user.setId(id);
		
		Product newProduct = product;
		newProduct.setSelled(0);
		newProduct.setOwner(user);
		
		return repository.saveAndFlush(product);
	}
	
	public Product update(long id, Product product) {
		Product productData = getById(id);
		productData.setName(Optional.ofNullable(product.getName()).orElse(productData.getName()));
		productData.setPrice(Optional.ofNullable(product.getPrice()).orElse(productData.getPrice()));
		productData.setDescription(Optional.ofNullable(product.getDescription()).orElse(productData.getDescription()));
		productData.setInStock(Optional.ofNullable(product.getInStock()).orElse(productData.getInStock()));
		productData.setOwner(Optional.ofNullable(product.getOwner()).orElse(productData.getOwner()));
		
		return repository.saveAndFlush(productData);
	}
	
	public void delete(long id) {
		repository.deleteById(id);			
	}
	
	public Product getById(long id) {
		Optional<Product> product = repository.findById(id);
		return product.orElseThrow(
				() -> new IllegalArgumentException("There is no Product with id: " + id));
	}
	
	public List<Product> getProductList() {
		return repository.findAll();
	}
	
	public Page<Product> getProductPage(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	public Product sellProducts(long id, ProductSell productSell) {
		Product productData = getById(id);
		if(productSell.getUserId() == productData.getOwner().getId()) {
			if(productData.getInStock() - productSell.getAmount() >= 0) {
				productData.setInStock(productData.getInStock() - productSell.getAmount());
				productData.setSelled(productData.getSelled() + productSell.getAmount());
				return repository.saveAndFlush(productData);
			} else {
				throw new DataIntegrityViolationException("Is not possible to sell more than the stock of product: " + productData.getId());
			}			
		} else {
			throw new DataIntegrityViolationException("Is not possible to sell a non-owner product!");
		}
	}
	
	public List<Product> getUsersProducts(long id) {
		return repository.getUsersProducts(id);
	}
}
