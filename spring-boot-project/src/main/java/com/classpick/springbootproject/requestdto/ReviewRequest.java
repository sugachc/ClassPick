package com.classpick.springbootproject.requestdto;


import lombok.Data;

import java.util.Optional;

@Data
public class ReviewRequest {

    private double rating;

    private Long bookId;

    //Optional로 감싸서 리뷰설명 없이 별만 남기는 경우 고려
    private Optional<String> reviewDescription;


}
