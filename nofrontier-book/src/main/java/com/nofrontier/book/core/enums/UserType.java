package com.nofrontier.book.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
	
	ADMIN (1),
    CUSTOMER (2);

    private Integer id;
}