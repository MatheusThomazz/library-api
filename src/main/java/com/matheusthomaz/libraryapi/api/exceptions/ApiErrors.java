package com.matheusthomaz.libraryapi.api.exceptions;

import com.matheusthomaz.libraryapi.exception.BusimessException;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrors(BusimessException ex){
        this.errors =  Arrays.asList(ex.getMessage());
    }

    public ApiErrors(ResponseStatusException ex){
        this.errors =  Arrays.asList(ex.getReason());
    }

    public List<String> getErrors() {
        return errors;
    }
}
