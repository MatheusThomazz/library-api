package com.matheusthomaz.libraryapi.service;

import com.matheusthomaz.libraryapi.exception.BusimessException;
import com.matheusthomaz.libraryapi.model.entity.Book;
import com.matheusthomaz.libraryapi.model.repository.BookRepository;
import com.matheusthomaz.libraryapi.service.Imp.BookServiceImp;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setup(){
        this.service = new BookServiceImp(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        //cenario
        Book book = creatValidBook();
        Mockito.when(repository.save(book)).thenReturn(Book.builder().id(1l).isbn("123").author("Fulano").title("As aventuras").build());

        Book savedBook = service.save(book);

        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123");
        Assertions.assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
        Assertions.assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
    }



    @Test
    @DisplayName("Deve lançar erro ao tentar salvar um livro com isbn duplicado")
    public void shouldBeNotSaveBookWithDuplicatedISBN(){

        Book book = creatValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));
        Assertions.assertThat(exception).isInstanceOf(BusimessException.class)
        .hasMessage("ISBN ja cadastrado");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve obter um livro pelo id")
    public void getByIdTest(){
        Long id = 1l;
        Book book = creatValidBook();
        book.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook =  service.getById(id);

        Assertions.assertThat(foundBook.isPresent()).isTrue();
        Assertions.assertThat(foundBook.get().getId()).isEqualTo(id);
        Assertions.assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        Assertions.assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar vazio quando o livro nao existir")
    public void getNotFoundByIdTest(){
        Long id = 1l;


        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> foundBook =  service.getById(id);

        Assertions.assertThat(foundBook.isPresent()).isFalse();

    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deletebookTest(){
        Book book = Book.builder().id(1l).build();

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->service.delete(book));

        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer um erro ao tentar deletar um livro inexistete")
    public void deleteInvalidbookTest(){
        Book book = new Book();


        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,  () -> service.delete(book));


        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer um erro ao tentar atualizar um livro inexistete")
    public void updateInvalidbookTest(){
        Book book = new Book();


        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,  () -> service.update(book));


        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest(){
        Long id = 1l;
        Book book = Book.builder().id(id).build();

        Book updateBook = creatValidBook();

        Mockito.when(repository.save(book)).thenReturn(updateBook);

        Book updetingBook = service.update(book);

        Assertions.assertThat(updetingBook.getId()).isEqualTo(updateBook.getId());
        Assertions.assertThat(updetingBook.getAuthor()).isEqualTo(updateBook.getAuthor());
        Assertions.assertThat(updetingBook.getTitle()).isEqualTo(updateBook.getTitle());
        Assertions.assertThat(updetingBook.getIsbn()).isEqualTo(updateBook.getIsbn());
    }

    @Test
    @DisplayName("Deve filtrar livros pelas propriedades")
    public void findBookTest(){

        Book book = creatValidBook();
        book.setId(1L);

        PageRequest pageRequest = PageRequest.of(0, 10 );

        List<Book> lista = Arrays.asList(book);
        Page<Book> page = new PageImpl<Book>(lista, pageRequest, 1);
        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

        Page<Book> result = service.find(book, pageRequest);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent()).isEqualTo(lista);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);


    }

    @Test
    @DisplayName("Deve obter um livro pelo isbn")
    public void getBookByIsbnTest(){
        String isbn = "123";
        Mockito.when(repository.findByIsbn(isbn)).thenReturn(Optional.of(Book.builder().id(1L).isbn(isbn).build()));

        Optional<Book> book = service.getBookByIsbn(isbn);

        Assertions.assertThat(book.isPresent()).isTrue();
        Assertions.assertThat(book.get().getId()).isEqualTo(1L);
        Assertions.assertThat(book.get().getIsbn()).isEqualTo(isbn);

        Mockito.verify(repository, Mockito.times(1)).findByIsbn(isbn);

    }



    private Book creatValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }
}
