package com.matheusthomaz.libraryapi.api.resource;

import com.matheusthomaz.libraryapi.api.dto.BookDTO;
import com.matheusthomaz.libraryapi.exception.BusimessException;
import com.matheusthomaz.libraryapi.api.exceptions.ApiErrors;
import com.matheusthomaz.libraryapi.model.entity.Book;
import com.matheusthomaz.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
@ResponseStatus(HttpStatus.CREATED)
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper) {

        this.service = service;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto){
        Book entity = modelMapper.map(dto, Book.class);

        entity = service.save(entity);

       return modelMapper.map(entity, BookDTO.class);

    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO get( @PathVariable Long id){
       return service
               .getById(id)
               .map(book -> modelMapper.map(book, BookDTO.class))
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        Book book = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        service.delete(book);


    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO update(@PathVariable Long id, BookDTO dto){
        Book book = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());
        book = service.update(book);
        return modelMapper.map(book, BookDTO.class);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();

        return new ApiErrors(bindingResult);

    }

    @ExceptionHandler(BusimessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusimessException(BusimessException ex){
        return new ApiErrors(ex);

    }
}