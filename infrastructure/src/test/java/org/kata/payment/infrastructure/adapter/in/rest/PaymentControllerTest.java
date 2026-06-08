package org.kata.payment.infrastructure.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.kata.payment.domain.model.Item;
import org.kata.payment.domain.model.Money;
import org.kata.payment.domain.model.Payment;
import org.kata.payment.domain.model.PaymentId;
import org.kata.payment.domain.usecase.CreatePayment;
import org.kata.payment.domain.usecase.GetAllPayments;
import org.kata.payment.domain.usecase.ModifyPayment;
import org.kata.payment.domain.usecase.ReadPayment;
import org.kata.payment.infrastructure.adapter.in.rest.dto.PaymentRequest;
import org.kata.payment.infrastructure.adapter.in.rest.mapper.PaymentRestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@Import(PaymentRestMapper.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreatePayment createPayment;

    @MockitoBean
    private ReadPayment readPayment;

    @MockitoBean
    private ModifyPayment modifyPayment;

    @MockitoBean
    private GetAllPayments getAllPayments;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createPayment() throws Exception {
        var paymentRequest = new PaymentRequest(
                Payment.PaymentType.CREDIT_CARD,
                Payment.PaymentStatus.NEW,
                Collections.singletonList(new PaymentRequest.ItemRequest("T-shirt", BigDecimal.valueOf(19.99), 5))
        );

        var createdPayment = Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .status(Payment.PaymentStatus.NEW)
                .items(List.of(new Item("T-shirt", new Money(19.99), 5)))
                .build();

        given(createPayment.execute(any(Payment.class))).willReturn(createdPayment);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.paymentType").value("CREDIT_CARD"))
                .andExpect(jsonPath("$.paymentStatus").value("NEW"))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name").value("T-shirt"))
                .andExpect(jsonPath("$.items[0].price").value(19.99))
                .andExpect(jsonPath("$.items[0].quantity").value(5));
    }

    @Test
    void modifyPayment() throws Exception {
        var paymentRequest = new PaymentRequest(
                Payment.PaymentType.CREDIT_CARD,
                Payment.PaymentStatus.AUTHORIZED,
                Collections.singletonList(new PaymentRequest.ItemRequest("T-shirt", BigDecimal.valueOf(19.99), 5))
        );

        var modifiedPayment = Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .status(Payment.PaymentStatus.AUTHORIZED)
                .items(List.of(new Item("T-shirt", new Money(19.99), 5)))
                .build();

        given(modifyPayment.execute(any(Payment.class))).willReturn(modifiedPayment);

        mockMvc.perform(put("/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.paymentType").value("CREDIT_CARD"))
                .andExpect(jsonPath("$.paymentStatus").value("AUTHORIZED"))
                .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    void getAllPayments() throws Exception {
        var payment1 = Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .status(Payment.PaymentStatus.CAPTURED)
                .items(List.of(new Item("T-shirt", new Money(19.99), 5)))
                .build();

        var payment2 = Payment.builder()
                .id(new PaymentId("2"))
                .paymentType(Payment.PaymentType.PAYPAL)
                .status(Payment.PaymentStatus.CANCELED)
                .items(List.of(new Item("Bike", new Money(208.00), 1), new Item("Shoes", new Money(30.00), 1)))
                .build();

        given(getAllPayments.execute()).willReturn(List.of(payment1, payment2));

        mockMvc.perform(get("/payments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].paymentType").value("CREDIT_CARD"))
                .andExpect(jsonPath("$[0].paymentStatus").value("CAPTURED"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].paymentType").value("PAYPAL"))
                .andExpect(jsonPath("$[1].paymentStatus").value("CANCELED"));
    }
}
