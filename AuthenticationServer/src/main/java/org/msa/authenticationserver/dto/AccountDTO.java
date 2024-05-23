package org.msa.authenticationserver.dto;

import lombok.Data;

@Data
public class AccountDTO {

    //@NotBlank
    private String accountId;

    private String password;

    private String token;

}