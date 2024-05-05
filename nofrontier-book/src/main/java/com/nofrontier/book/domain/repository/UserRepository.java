package com.nofrontier.book.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByCpf(String cpf);

	Optional<User> findByKeyPix(String keyPix);

	Page<User> findByCitiesAttendedIbgeCode(String ibgeCode, Pageable pageable);

	Boolean existsByCitiesAttendedIbgeCode(String ibgeCode);

	default Boolean isEmailAlreadyRegistered(User user) {
		if (user.getEmail() == null) {
			return false;
		}
		return findByEmail(user.getEmail())
				.map(userFound -> !userFound.getId().equals(user.getId()))
				.orElse(false);
	}

	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.person.cpf = :cpf")
	boolean isCpfAlreadyRegistered(@Param("cpf") String cpf);

	default Boolean isCpfAlreadyRegistered(User user) {
		List<Person> people = user.getPersons();
		if (people == null || people.isEmpty()) {
			return false;
		}
		for (Person person : people) {
			if (person.getCpf() != null
					&& findByCpf(person.getCpf()).isPresent()) {
				return true; // CPF encontrado, retorna true
			}
		}
		return false; // CPF não encontrado em nenhuma pessoa
	}

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.keyPix = :keyPix")
    boolean isKeyPixAlreadyRegistered(@Param("keyPix") String keyPix);
    
	default Boolean isKeyPixAlreadyRegistered(User user) {
		List<Person> people = user.getPersons();
		if (people == null || people.isEmpty()) {
			return false;
		}
		for (Person person : people) {
			if (person.getKeyPix() != null
					&& findByKeyPix(person.getKeyPix()).isPresent()) {
				return true; // KeyPix encontrado, retorna true
			}
		}
		return false; // KeyPix não encontrado em nenhuma pessoa
	}

	boolean existsByEmail(String email);

}