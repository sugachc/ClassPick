package com.classpick.springbootproject.controller;

import com.classpick.springbootproject.entity.Book;
import com.classpick.springbootproject.responsedto.ShelfCurrentLoansResponse;
import com.classpick.springbootproject.service.BookService;
import com.classpick.springbootproject.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//리액트가 이 컨트롤러를 호출할 수 있음을 의미
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token)throws Exception{
        String userEmail=ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return bookService.currentLoans(userEmail);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value="Authorization")String token){
        String userEmail=ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return bookService.currentLoansCount(userEmail);
    }


    //사용자신청여부api
    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(@RequestHeader(value="Authorization")String token,
                                      @RequestParam Long bookId){
         String userEmail= ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
         boolean result=bookService.checkoutBookByUser(userEmail,bookId);
        System.out.println(result);
        return result;
    }


    //신청api
    @PutMapping("/secure/checkout")
    public Book checkoutBook (@RequestHeader(value="Authorization")String token,
                              @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return bookService.checkoutBook(userEmail, bookId);
    }

    @PutMapping("/secure/return")
    public void returnBook(@RequestHeader(value = "Authorization")String token,@RequestParam Long bookId)throws Exception{
        String userEmail=ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        bookService.returnBook(userEmail,bookId);
    }

    @PutMapping("/secure/renew/loan")
    public void renewLoan(@RequestHeader(value = "Authorization")String token,
                          @RequestParam Long bookId)throws Exception{
        String userEmail=ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        bookService.renewLoan(userEmail,bookId);
    }


}
