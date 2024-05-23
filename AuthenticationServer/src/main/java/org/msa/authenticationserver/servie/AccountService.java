package org.msa.authenticationserver.servie;

import lombok.RequiredArgsConstructor;
import org.msa.authenticationserver.domain.Account;
import org.msa.authenticationserver.dto.AccountDTO;
import org.msa.authenticationserver.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void saveAccount(AccountDTO accountDTO, String token) {
        accountRepository.save(Account.builder()
                .accountId(accountDTO.getAccountId())
                .password(accountDTO.getPassword())
                .token(token)
                .build());
    }

    public Account selectAccount(AccountDTO accountDTO) {
        Optional<Account> optional = accountRepository.findById(accountDTO.getAccountId());

        if(optional.isPresent()) {
            Account account = optional.get();

            if(account.getPassword().equals(accountDTO.getPassword())) {
                return account;
            }
        }

        return null;
    }

}