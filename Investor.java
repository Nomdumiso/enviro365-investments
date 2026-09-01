package com.enviro.assessment.junior.nomdumiso.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "investors")
public class Investor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @OneToOne(mappedBy = "investor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Portfolio portfolio;

    public Investor() {
    }

    public Investor(String firstName, String lastName, String email, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    /** Calculated age used by the retirement-withdrawal business rule (age > 65). */
    @Transient
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public Portfolio getPortfolio() { return portfolio; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }
}
