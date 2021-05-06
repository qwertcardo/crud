package com.qwertcardo.crud.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.qwertcardo.crud.models.Product;
import com.qwertcardo.crud.models.ProductSell;
import com.qwertcardo.crud.services.ProductService;

@RestController
@RequestMapping(value = "/product")
public class ProductRestController {

	@Autowired
	private ProductService service;
	
	@RequestMapping(value = "/{id}/save", method = RequestMethod.POST)
	public ResponseEntity<Product> create(@PathVariable long id, @RequestBody Product product) {
		Product newProduct = service.save(id, product);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(newProduct.getId()).toUri();
		
		return ResponseEntity.created(uri).body(newProduct);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Product> update(@PathVariable long id, @RequestBody Product product) {
		try {
			return ResponseEntity.ok(service.update(id, product));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Product> delete(@PathVariable long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> getById(@PathVariable long id) {
		try {
			return ResponseEntity.ok(service.getById(id));			
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Product>> getProductList() {
		return ResponseEntity.ok(service.getProductList());
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Page<Product>> getProductPage(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return ResponseEntity.ok(service.getProductPage(pageable));
	}
	
	@RequestMapping(value = "/{id}/sell", method = RequestMethod.POST)
	public ResponseEntity<Product> sellProducts(@PathVariable long id, @RequestBody ProductSell productSell) {
		try {
			return ResponseEntity.ok(service.sellProducts(id, productSell));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().build();
		} 
	}
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<Product>> getUsersProducts(@PathVariable long id) {
		return ResponseEntity.ok(service.getUsersProducts(id));
	}
	
}
