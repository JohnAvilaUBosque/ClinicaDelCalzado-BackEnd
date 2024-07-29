package com.ClinicaDelCalzado_BackEnd.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_answers")
    private Integer idAnswers;

    @ManyToOne
    @JoinColumn(name = "id_question")
    private SecurityQuestion securityQuestion;

    @ManyToOne
    @JoinColumn(name = "id_administrator")
    private Administrator administrator;

    @Column(name = "answer")
    private String answer;

}
