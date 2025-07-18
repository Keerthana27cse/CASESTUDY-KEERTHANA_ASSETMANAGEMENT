package com.example.assetmanagement.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.assetmanagement.entity.User;
public interface UserRepo extends JpaRepository<User, Long> 
{
	 // to search by email

	//testing
	 Optional<User> findByUsername(String username);
	 Optional<User> findByEmail(String email);
}


