package com.matheusthomaz.libraryapi.service.Imp;

import com.matheusthomaz.libraryapi.model.entity.Loan;
import com.matheusthomaz.libraryapi.model.repository.LoanRepository;
import com.matheusthomaz.libraryapi.service.LoanService;

public class LoanServiceImp implements LoanService {

    private LoanRepository repository;

    public LoanServiceImp(LoanRepository repository){
        this.repository = repository;
    }
    @Override
    public Loan save(Loan loan) {
        return repository.save(loan);
    }
}
