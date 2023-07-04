package com.sztus.unicorn.user.object.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {
    private Long id;
    private String name;
    private String open_id;
    private String password;
    private String salt;
    private String token;
    private Long expiredAt;
}
