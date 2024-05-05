package com.nofrontier.book.dto.v1;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@JsonPropertyOrder({ "id", "firstName", "lastName", "address", "gender", "enabled" })
public class PersonDto extends RepresentationModel<PersonDto> implements Serializable{

	private static final long serialVersionUID = 1L;

	//@Mapping("id")
	@JsonProperty("id")
	private Long key;
	private String firstName;
	private String lastName;
	private String gender;
	private String cpf;
	private LocalDate birth;
	private String phoneNumber;
	private String keyPix;
	private Boolean enabled;
}