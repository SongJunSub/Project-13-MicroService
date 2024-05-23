package org.msa.authenticationserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msa.authenticationserver.domain.Account;
import org.msa.authenticationserver.dto.AccountDTO;
import org.msa.authenticationserver.dto.ResponseDTO;
import org.msa.authenticationserver.servie.AccountService;
import org.msa.authenticationserver.util.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/account")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    private final AccountService accountService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDTO> join(@Valid @RequestBody AccountDTO accountDTO) {
        ResponseDTO.ResponseDTOBuilder builder = ResponseDTO.builder();

        Account account = accountService.selectAccount(accountDTO);

        if(account != null) {
            builder.code("100").message("Already Join User");
        }
        else {
            accountService.saveAccount(accountDTO, null);

            builder.code("200").message("Success");
        }

        log.debug("Account ID: {}", accountDTO.getAccountId());

        return ResponseEntity.ok(builder.build());
    }

    @GetMapping("/token")
    public ResponseEntity<ResponseDTO> token(@Valid @RequestBody AccountDTO accountDTO) {
        ResponseDTO.ResponseDTOBuilder builder = ResponseDTO.builder();

        Account account = accountService.selectAccount(accountDTO);

        if(account == null) {
            builder.code("101").message("Unknown User");
        }
        else {
            String token = getToken(accountDTO);

            accountService.saveAccount(accountDTO, token);

            builder.code("200").message("Success");
            builder.token(token);
        }

        log.debug("Token Account ID: {}", accountDTO.getAccountId());

        return ResponseEntity.ok(builder.build());
    }

    private String getToken(AccountDTO accountDTO) {
        return JWTUtil.generate(accountDTO);
    }


}