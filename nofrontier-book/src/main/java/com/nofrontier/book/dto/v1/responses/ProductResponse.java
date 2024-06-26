package com.nofrontier.book.dto.v1.responses;

import java.io.Serializable;
import java.math.BigDecimal;

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
@JsonPropertyOrder({"id", "description", "format", "edition", "price", "active",
		"book"})
public class ProductResponse extends RepresentationModel<ProductResponse>
		implements
			Serializable {

	private static final long serialVersionUID = 1L;

	// @Mapping("id")
	@JsonProperty("id")
	private Long key;

	private String description;

	private String format;

	private String edition;

	private BigDecimal price;

	private Boolean active;

	private BookResponse book;
}
