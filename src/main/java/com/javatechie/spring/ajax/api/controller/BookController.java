package com.javatechie.spring.ajax.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.javatechie.spring.ajax.api.dto.BookRepository;
import com.javatechie.spring.ajax.api.dto.FetchBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javatechie.spring.ajax.api.dto.Book;
import com.javatechie.spring.ajax.api.dto.ServiceResponse;

@RestController
public class BookController {

	List<Book> bookStore = new ArrayList<>();
	@Autowired
	BookRepository repository ;
	@Autowired
	FetchBookRepository fetchBookRepository;

	@PostMapping("/saveBook")
	public ResponseEntity<Object> addBook(@RequestBody Book book) {
		bookStore.add(book);
		ServiceResponse<Book> response = new ServiceResponse<Book>("success", book);
		if(response.getStatus()=="success")
			repository.save(new Book(book.getBookName(),book.getAuthor()));
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@GetMapping("/getBooks")
	public ResponseEntity<Object> getAllBooks() {
		bookStore.clear();
		bookStore.addAll(fetchBookRepository.findAll()); //vsetky knihy si pridam ako objekty do programu
		ServiceResponse<List<Book>> response = new ServiceResponse<>("success", bookStore);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
