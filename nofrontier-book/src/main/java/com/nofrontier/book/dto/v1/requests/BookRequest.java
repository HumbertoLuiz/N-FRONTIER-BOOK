package com.nofrontier.book.dto.v1.requests;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nofrontier.book.core.enums.BookStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class BookRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String title;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 3, max = 255, message = "{size.message}")
	private String author;

	@NotBlank(message = "{not.blank.message}")
	@Size(min = 13, max = 13, message = "{size.message}")
	private String isbn;

	@NotNull(message = "{not.null.message}")
	private Date launchDate;

	private OffsetDateTime registrationDate;
	
	private OffsetDateTime updateDate;

	private Integer createdBy;

	private Integer lastModifiedBy;
	
	@NotNull(message = "{not.null.message}")
	private Boolean active;
	
	@NotNull(message = "{not.null.message}")
	private BookStatus bookStatus;
	
	@NotNull(message = "{not.null.message}")
	@DecimalMin(value = "0.0", inclusive = true, message = "{positive.message}")
	private BigDecimal shippingRate;
	
	@NotNull(message = "{not.null.message}")
	@DecimalMin(value = "0.0", inclusive = true, message = "{positive.message}")
    private BigDecimal price;
	
    private String observation;

    private String reasonCancellation;
	
	private Long categoryId;
}
