package org.msa.itemservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @Column(length = 30)
    private String id;

    @Column(length = 30)
    private String name;

    @Column(length = 30)
    private String description;

    @Column(length = 10)
    private Long count;

    @Column(length = 14)
    private String createdAt;

    @Column(length = 14)
    private String updatedAt;

}
