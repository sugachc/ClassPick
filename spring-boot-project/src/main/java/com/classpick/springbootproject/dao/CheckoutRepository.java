package com.classpick.springbootproject.dao;

import com.classpick.springbootproject.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout,Long>{

    //사용자이메일과 ID를 기반으로 대출여부 확인
    Checkout findByUserEmailAndBookId(String userEmail, Long bookId);

    //사용자가 총 몇개의 신청을 했는지
    List<Checkout> findBooksByUserEmail(String userEmail);

    @Modifying
    @Query("delete from Checkout where book_id in :book_id")
    //삭제하면 대출무효처리
    void deleteAllByBookId(@Param("book_id") Long bookId);

}
