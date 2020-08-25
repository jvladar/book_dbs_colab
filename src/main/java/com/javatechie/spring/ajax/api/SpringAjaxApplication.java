package com.javatechie.spring.ajax.api;

import com.javatechie.spring.ajax.api.dto.Book;
import com.javatechie.spring.ajax.api.dto.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class SpringAjaxApplication {
	@Autowired
	BookRepository repository = new BookRepository() {
		@Override
		public <S extends Book> S save(S s) {
			return null;
		}

		@Override
		public <S extends Book> Iterable<S> saveAll(Iterable<S> iterable) {
			return null;
		}

		@Override
		public Optional<Book> findById(Long aLong) {
			return Optional.empty();
		}

		@Override
		public boolean existsById(Long aLong) {
			return false;
		}

		@Override
		public Iterable<Book> findAll() {
			return null;
		}

		@Override
		public Iterable<Book> findAllById(Iterable<Long> iterable) {
			return null;
		}

		@Override
		public long count() {
			return 0;
		}

		@Override
		public void deleteById(Long aLong) {

		}

		@Override
		public void delete(Book book) {

		}

		@Override
		public void deleteAll(Iterable<? extends Book> iterable) {

		}

		@Override
		public void deleteAll() {

		}
	};
	public static void main(String[] args) {
		SpringApplication.run(SpringAjaxApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(BookRepository repository){
		return args -> {
			repository.save(new Book("peler","jano"));
			repository.save(new Book("meno_knihy","Jozef"));
		};
	}

}
