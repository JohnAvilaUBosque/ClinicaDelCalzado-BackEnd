package com.ClinicaDelCalzado_BackEnd.repository.operator;

import com.ClinicaDelCalzado_BackEnd.entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IOperatorRepository  extends JpaRepository<Operator, Long> {

    @Query(value = "SELECT count(id_operator) " +
            "FROM operator " +
            "WHERE (id_operator IN (:operators) OR :operators IS NULL) ", nativeQuery = true)
    Integer findOperatorsCount(
            @Param("operators") long[] operators
    );
}
