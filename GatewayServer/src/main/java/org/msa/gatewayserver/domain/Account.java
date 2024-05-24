package org.msa.gatewayserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(length = 30)
    private String accountId;

    @Column(length = 100)
    private String password;

    @Column(length = 500)
    private String token;

}
