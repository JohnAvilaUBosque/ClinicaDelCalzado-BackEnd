package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.CommentDTO;
import com.ClinicaDelCalzado_BackEnd.entity.Comment;
import com.ClinicaDelCalzado_BackEnd.entity.WorkOrder;

import java.util.List;

public interface ICommentService {
    Comment save(Comment comment);
    List<CommentDTO> getCommentOrder(String orderNumber);
    Comment saveCommentOrder(WorkOrderDTORequest workOrderDTORequest, WorkOrder orderNumber);
}
