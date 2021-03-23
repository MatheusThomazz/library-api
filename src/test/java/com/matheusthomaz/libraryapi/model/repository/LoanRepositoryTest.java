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
import java.util.List;

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

        Loan loan = createAndPersistLoan(LocalDate.now());
        Book book = loan.getBook();

        boolean exists = repository.existsByBookAndNotReturned(book);

        Assertions.assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Deve buscar emprestimo pelo isbn do livro ou customer")
    public void findBookIsbnCustomerTest(){
        createAndPersistLoan(LocalDate.now());

        Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "fulano", PageRequest.of(0, 10));

        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);

    }

    @Test
    @DisplayName("Deve obter emprestimos cuja a data de emprestimo for menor ou igual a tres dias atras e nao retornados")
    public void findByLoanDateLessThanAndNotReturnedTest(){

        Loan loan = createAndPersistLoan(LocalDate.now().minusDays(5));

        List<Loan> result = repository.findBtLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        Assertions.assertThat(result).hasSize(1).contains(loan);

    }

    @Test
    @DisplayName("Deve retornar vazio quando não tiver emprestimos pendentes")
    public void notFindByLoanDateLessThanAndNotReturnedTest(){

        Loan loan = createAndPersistLoan(LocalDate.now());

        List<Loan> result = repository.findBtLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        Assertions.assertThat(result).isEmpty();

    }

    public Loan createAndPersistLoan(LocalDate loanDate){
        Book book = createBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("fulano").loanDate(loanDate).build();

        entityManager.persist(loan);

        return loan;
    }
}
