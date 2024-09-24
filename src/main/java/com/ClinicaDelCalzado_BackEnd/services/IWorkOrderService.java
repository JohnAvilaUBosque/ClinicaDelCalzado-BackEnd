package com.ClinicaDelCalzado_BackEnd.services;

import com.ClinicaDelCalzado_BackEnd.dtos.request.AddCommentDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.request.UpdatePaymentDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.request.WorkOrderDTORequest;
import com.ClinicaDelCalzado_BackEnd.dtos.response.MessageDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.OrderByIdNumberDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.OrderListDTOResponse;
import com.ClinicaDelCalzado_BackEnd.dtos.response.WorkOrderDTOResponse;

public interface IWorkOrderService {

    WorkOrderDTOResponse createWorkOrder(WorkOrderDTORequest workOrderDTORequest, Long userAuth);
    MessageDTOResponse updateStatusWorkOrder(String orderNumber, Long userAuth);
    MessageDTOResponse updatePaymentWorkOrder(String orderNumber, Long userAuth,  UpdatePaymentDTORequest updatePaymentDTORequest);
    MessageDTOResponse addCommentWorkOrder(String orderNumber, Long userAuth, AddCommentDTORequest addCommentDTORequest);
    OrderByIdNumberDTOResponse getWorkOrderByOrderNumber(String orderNumber);
    OrderListDTOResponse getWorkOrderList(String orderStatus, String orderNumber, Long identification, String name, String phone, String attendedBy);
}
