package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.Response.QuestionListDTOResponse;

public interface IQuestionService {

    QuestionListDTOResponse findAllQuestions();
}
