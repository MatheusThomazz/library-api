package com.matheusthomaz.libraryapi.service;

import com.matheusthomaz.libraryapi.api.dto.LoanFilterDTO;
import com.matheusthomaz.libraryapi.api.resource.BookController;
import com.matheusthomaz.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO filter, Pageable pageable);
}
