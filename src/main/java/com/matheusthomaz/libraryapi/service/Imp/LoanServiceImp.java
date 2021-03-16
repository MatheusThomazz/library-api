package com.matheusthomaz.libraryapi.service.Imp;

import com.matheusthomaz.libraryapi.exception.BusimessException;
import com.matheusthomaz.libraryapi.model.entity.Loan;
import com.matheusthomaz.libraryapi.model.repository.LoanRepository;
import com.matheusthomaz.libraryapi.service.LoanService;

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
        return Optional.empty();
    }

    @Override
    public Loan update(Loan loan) {
        return null;
    }
}
