package com.filsanguinaire.tournament.bo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Entity
public class Tournament extends Event {

	@Column(length = 100)
    private String location;

    @Column(length = 150)
    private String address;

    @Column(length = 5)
    private String postalCode;

    @Column(length = 50)
    private String city;
}
