package com.matheusthomaz.libraryapi.model.repository;

import com.matheusthomaz.libraryapi.model.entity.Book;
import com.matheusthomaz.libraryapi.model.entity.Loan;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.matheusthomaz.libraryapi.model.repository.BookRepositoryTest.createBook;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    LoanRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("Deve verificar se existe emprestimo não devolvido para um livro.")
    public void existsByBookAndNotReturnedTest(){

        Loan loan = createAndPersistLoan();
        Book book = loan.getBook();

        boolean exists = repository.existsByBookAndNotReturned(book);

        Assertions.assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Deve buscar emprestimo pelo isbn do livro ou customer")
    public void findBookIsbnCustomerTest(){
        createAndPersistLoan();

        Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "fulano", PageRequest.of(0, 10));

        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);

    }

    public Loan createAndPersistLoan(){
        Book book = createBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("fulano").loanDate(LocalDate.now()).build();

        entityManager.persist(loan);

        return loan;
    }
}
