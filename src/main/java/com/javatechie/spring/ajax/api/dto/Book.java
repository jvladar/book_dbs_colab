package com.javatechie.spring.ajax.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity

public class Book {
	@Id @GeneratedValue
	private Long bookId;
	private String bookName;
	private String author;

	public Book(String bookName, String author) {
		this.bookName=bookName;
		this.author=author;
	}
}
