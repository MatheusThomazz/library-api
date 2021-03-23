package com.matheusthomaz.libraryapi.service.Imp;

import com.matheusthomaz.libraryapi.api.dto.LoanFilterDTO;
import com.matheusthomaz.libraryapi.exception.BusimessException;
import com.matheusthomaz.libraryapi.model.entity.Book;
import com.matheusthomaz.libraryapi.model.entity.Loan;
import com.matheusthomaz.libraryapi.model.repository.LoanRepository;
import com.matheusthomaz.libraryapi.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanServiceImp implements LoanService {

    private LoanRepository repository;

    public LoanServiceImp(LoanRepository repository){
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if (repository.existsByBookAndNotReturned(loan.getBook())){
            throw new BusimessException("Book already loaned");
        }
        return repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filter, Pageable pageable) {
        return repository.findByBookIsbnOrCustomer(filter.getIsbn(), filter.getCustomer(), pageable);
    }

    @Override
    public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
        return repository.findByBook(book, pageable);
    }

    @Override
    public List<Loan> getAllLateLoans() {
        final Integer loanDays = 4;
        LocalDate threeDaysAgo = LocalDate.now().minusDays(loanDays);
        return repository.findBtLoanDateLessThanAndNotReturned(threeDaysAgo);
    }
}
