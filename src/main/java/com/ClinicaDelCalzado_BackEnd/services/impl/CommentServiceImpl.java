package com.ClinicaDelCalzado_BackEnd.services.impl;

import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.workOrders.CommentDTO;;
import com.ClinicaDelCalzado_BackEnd.entity.Comment;
import com.ClinicaDelCalzado_BackEnd.entity.WorkOrder;
import com.ClinicaDelCalzado_BackEnd.repository.workOrders.ICommentRepository;
import com.ClinicaDelCalzado_BackEnd.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    private ICommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(ICommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<CommentDTO> getCommentOrder(String orderNumber) {
        List<Comment> commentList = commentRepository.findCommentByWorkOrder(orderNumber);

        if (commentList.isEmpty()) {
            throw new NotFoundException(String.format("No cuenta con comentarios para la orden %s!!", orderNumber));
        }

        return commentList.stream()
                .map(comment -> CommentDTO.builder()
                        .idComment(comment.getIdComment())
                        .comment(comment.getAdminComment())
                        .build())
                .toList();
    }

    @Override
    public Comment saveCommentOrder(WorkOrderDTORequest workOrderDTORequest, WorkOrder orderNumber) {
        return save(Comment.builder()
                .idOrderCom(WorkOrder.builder().orderNumber(orderNumber.getOrderNumber()).build())
                .adminComment(workOrderDTORequest.getGeneralComment())
                .build());
    }
}
