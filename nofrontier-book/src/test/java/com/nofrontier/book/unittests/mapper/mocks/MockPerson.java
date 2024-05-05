package com.nofrontier.book.unittests.mapper.mocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.nofrontier.book.domain.model.Person;
import com.nofrontier.book.dto.v1.PersonDto;

public class MockPerson {

	public Person mockEntity() {
		return mockEntity(0);
	}

	public PersonDto mockDto() {
		return mockDto(0);
	}

	public List<Person> mockEntityList() {
		List<Person> persons = new ArrayList<Person>();
		for (int i = 0; i < 14; i++) {
			persons.add(mockEntity(i));
		}
		return persons;
	}

	public List<PersonDto> mockDtoList() {
		List<PersonDto> persons = new ArrayList<>();
		for (int i = 0; i < 14; i++) {
			persons.add(mockDto(i));
		}
		return persons;
	}

	public Person mockEntity(Integer number) {
		Person person = new Person();
		person.setId(number.longValue());
		person.setFirstName("First Name Test" + number);
		person.setLastName("Last Name Test" + number);
		person.setGender(((number % 2) == 0) ? "Male" : "Female");
		person.setCpf("Cpf Test" + number);
		person.setBirth(LocalDate.of(0, 0, 0));
		person.setPhoneNumber("Phone Number Test" + number);
		person.setKeyPix("Key Pix Test" + number);		
		return person;
	}

	public PersonDto mockDto(Integer number) {
		PersonDto person = new PersonDto();
		person.setKey(number.longValue());
		person.setFirstName("First Name Test" + number);
		person.setLastName("Last Name Test" + number);
		person.setGender(((number % 2) == 0) ? "Male" : "Female");
		person.setCpf("Cpf Test" + number);
		person.setBirth(LocalDate.of(0, 0, 0));
		person.setPhoneNumber("Phone Number Test" + number);
		person.setKeyPix("Key Pix Test" + number);
		return person;
	}

}