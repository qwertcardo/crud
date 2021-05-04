package com.qwertcardo.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qwertcardo.crud.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT u.* FROM tb_users u WHERE email = ?1", nativeQuery = true)
	public abstract User findByEmail(String email);
	
	@Query(value = "SELECT u.* FROM tb_users u WHERE cpf = ?1", nativeQuery = true)
	public abstract User findByCpf(String cpf);
	
	@Query(value = "SELECT u.* FROM tb_users u WHERE login = ?1", nativeQuery = true)
	public abstract User findByLogin(String login);
}
