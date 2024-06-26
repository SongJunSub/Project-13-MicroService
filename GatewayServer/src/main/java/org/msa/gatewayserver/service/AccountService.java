package org.msa.gatewayserver.service;

import lombok.RequiredArgsConstructor;
import org.msa.gatewayserver.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public boolean existsByAccountIdAndToken(String accountId, String token) {
        return accountRepository.existsByAccountIdAndToken(accountId, token);
    }

}