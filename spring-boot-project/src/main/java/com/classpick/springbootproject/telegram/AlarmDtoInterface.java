package com.classpick.springbootproject.telegram;

import java.time.LocalDateTime;

public interface AlarmDtoInterface {

    String title();
    String description();
    String image();
    String url();
    LocalDateTime contentsStartAt();
//    ContentsProviderType contentsProvider();
}
