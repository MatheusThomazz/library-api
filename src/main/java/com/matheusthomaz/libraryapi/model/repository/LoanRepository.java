package com.matheusthomaz.libraryapi.model.repository;


import com.matheusthomaz.libraryapi.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
