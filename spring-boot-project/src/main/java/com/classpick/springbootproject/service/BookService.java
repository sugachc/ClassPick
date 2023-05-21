package com.classpick.springbootproject.service;

import com.classpick.springbootproject.dao.BookRepository;
import com.classpick.springbootproject.dao.CheckoutRepository;
import com.classpick.springbootproject.dao.HistoryRepository;
import com.classpick.springbootproject.entity.Book;
import com.classpick.springbootproject.entity.Checkout;
import com.classpick.springbootproject.entity.History;
import com.classpick.springbootproject.responsedto.ShelfCurrentLoansResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    private HistoryRepository historyRepository;

    //생성자 종속성주입
    //저장소 설정
    public BookService(BookRepository bookRepository,CheckoutRepository checkoutRepository,HistoryRepository historyRepository){
        this.bookRepository=bookRepository;
        this.checkoutRepository=checkoutRepository;
        this.historyRepository=historyRepository;
    }

    //
    public Book checkoutBook(String userEmail, Long bookId)throws Exception{

        //Id로 Entity 찾아옴(Optional로 감싸주는 것은 null을 대비하기 위함)
        Optional<Book> book=bookRepository.findById(bookId);

        //유저이메일과 아이디로 대출내역 가져옴
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        //사용자가 책을 대출하려고 할때 특정책을 확인하는 이유는?
        //사용자가 한권의 책을 한번만 확인하기 위해서. 사용자가 같은 책을 2번이상 대출하면 안되기 때문에
        //null이 아니라는 것은 책ID와 일치하는 책을 데이터베이스에서 찾았다는 것이다


        //isPresent() 메소드는 Optional 객체의 값이 null인지 여부를 확인해준다
        //if 책이 없거나,대출이 되었거나,책존재여부도 없다면 예외처리 터트려준다
        if(!book.isPresent()||validateCheckout!=null||book.get().getCopiesAvailable()<0){
            throw new Exception("doesn`t exist or already checked out by user");
        }

        //if 책이 있다면 개수-1시켜준다
        //book.get()을 시켜주는 이유? Optional으로 감싼 book객체를 꺼내주는것
        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);
        //book객체 저장
        bookRepository.save(book.get());

        //신청객체
        Checkout checkout=new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                book.get().getId()
        );

        checkoutRepository.save(checkout);

        //책 Entity반환
        return book.get();
    }


    //사용자가 이걸 신청했는지 안했는지 확인여부
    public Boolean checkoutBookByUser(String userEmail,Long bookId){
        Checkout validateCheckout=checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if(validateCheckout!=null){
            return true;
        }else{
            return false;
        }
    }


    public int currentLoansCount(String userEmail){
        //반환 리스트의 size로 총 몇개신청이 되었는지 확인
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail)throws Exception{

            List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses=new ArrayList<>();

            List<Checkout> checkoutList=checkoutRepository.findBooksByUserEmail(userEmail);
            List<Long> bookIdList=new ArrayList<>();

            for(Checkout i:checkoutList){
                bookIdList.add(i.getBookId());
            }

            List<Book> books=bookRepository.findBooksByBookIds(bookIdList);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

            for(Book book:books){
                Optional<Checkout> checkout=checkoutList.stream()
                        .filter(x->x.getBookId()==book.getId()).findFirst();

                if(checkout.isPresent()){
                    Date d1=sdf.parse(checkout.get().getReturnDate());
                    Date d2=sdf.parse(LocalDate.now().toString());

                    TimeUnit time= TimeUnit.DAYS;
                    long difference_In_Time=time.convert(d1.getTime()- d2.getTime(),TimeUnit.MICROSECONDS);

                    shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book,(int)difference_In_Time));
                }
            }
            return shelfCurrentLoansResponses;
    }

    //반납
    public void returnBook(String userEmail,Long bookId)throws Exception{
         Optional<Book> book=bookRepository.findById(bookId);

         Checkout validateCheckout=checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

         if(!book.isPresent() ||validateCheckout==null){
                throw  new Exception("Book does not exist or not checked out by user");
         }

         book.get().setCopiesAvailable(book.get().getCopiesAvailable()+1);

         bookRepository.save(book.get());
         checkoutRepository.deleteById(validateCheckout.getId());

         History history=new History(
                 userEmail,
                 validateCheckout.getCheckoutDate(),
                 LocalDate.now().toString(),
                 book.get().getTitle(),
                 book.get().getAuthor(),
                 book.get().getDescription(),
                 book.get().getImg()
         );

         historyRepository.save(history);

    }


    //갱신 연장
    public void renewLoan(String userEmail,Long bookId)throws Exception{

        Checkout validateCheckout=checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if(validateCheckout==null){
            //존재하지 않거나 체크아웃하지 않았음
            throw new Exception("Book does not exist or not checked out by user");
        }

        SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd");

        Date d1=sdFormat.parse(validateCheckout.getReturnDate());
        Date d2=sdFormat.parse(LocalDate.now().toString());

        //오늘이 마감일이거나 혹은 마감일이 아직 안왔다면
        if(d1.compareTo(d2)>0||d1.compareTo(d2)==0){
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validateCheckout);
        }


    }



}
