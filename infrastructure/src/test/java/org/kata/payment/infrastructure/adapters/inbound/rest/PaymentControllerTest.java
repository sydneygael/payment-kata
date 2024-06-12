package org.kata.payment.infrastructure.adapters.inbound.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.kata.payment.application.ports.input.ManagePayment;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.Item;
import org.kata.payment.domain.valueobject.Money;
import org.kata.payment.domain.valueobject.PaymentId;
import org.kata.payment.infrastructure.adapters.inbound.rest.dto.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagePayment managePayment;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPayment() throws Exception {
        // Given
        var paymentRequest = new PaymentRequest(
                Payment.PaymentType.CREDIT_CARD,
                Payment.PaymentStatus.NEW,
                Collections.singletonList(new PaymentRequest.ItemRequest("T-shirt", 19.99, 5))
        );

        var createdPayment = Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .status(Payment.PaymentStatus.NEW)
                .items(List.of(new Item("T-shirt", new Money(19.99), 5)))
                .build();

        given(managePayment.createPayment(any(Payment.class))).willReturn(createdPayment);

        // When & Then
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdPayment.getId().id()))
                .andExpect(jsonPath("$.paymentType").value(createdPayment.getPaymentType().name().toUpperCase()))
                .andExpect(jsonPath("$.paymentStatus").value(createdPayment.getPaymentStatus().name()))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name").value("T-shirt"))
                .andExpect(jsonPath("$.items[0].price").value(19.99))
                .andExpect(jsonPath("$.items[0].quantity").value(5));
    }

    @Test
    void modifyPayment() throws Exception {
        // Given
        var paymentRequest = new PaymentRequest(
                Payment.PaymentType.CREDIT_CARD,
                Payment.PaymentStatus.AUTHORIZED,
                Collections.singletonList(new PaymentRequest.ItemRequest("T-shirt", 19.99, 5))
        );

        var existingPayment = Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .status(Payment.PaymentStatus.NEW)
                .items(List.of(new Item("T-shirt", new Money(19.99), 5)))
                .build();

        var modifiedPayment = Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .status(Payment.PaymentStatus.AUTHORIZED)
                .items(List.of(new Item("T-shirt", new Money(19.99), 5)))
                .build();

        given(managePayment.readingPayment(any(PaymentId.class))).willReturn(existingPayment);
        given(managePayment.modifyPayment(any(Payment.class))).willReturn(modifiedPayment);

        // When & Then
        mockMvc.perform(put("/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.paymentType").value("CREDIT_CARD"))
                .andExpect(jsonPath("$.paymentStatus").value("AUTHORIZED"))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name").value("T-shirt"))
                .andExpect(jsonPath("$.items[0].price").value(19.99))
                .andExpect(jsonPath("$.items[0].quantity").value(5));
    }

    @Test
    void getAllPayments() throws Exception {
        // Given
        Payment payment1 = Payment.builder()
                .id(new PaymentId("1"))
                .paymentType(Payment.PaymentType.CREDIT_CARD)
                .status(Payment.PaymentStatus.CAPTURED)
                .items(List.of(new Item("T-shirt", new Money(19.99), 5)))
                .build();

        Payment payment2 = Payment.builder()
                .id(new PaymentId("2"))
                .paymentType(Payment.PaymentType.PAYPAL)
                .status(Payment.PaymentStatus.CANCELED)
                .items(List.of(
                        new Item("Bike", new Money(208.00), 1),
                        new Item("Shoes", new Money(30.00), 1)
                ))
                .build();

        List<Payment> payments = List.of(payment1, payment2);

        given(managePayment.getAllPayments()).willReturn(payments);

        // When & Then
        mockMvc.perform(get("/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].paymentType").value("CREDIT_CARD"))
                .andExpect(jsonPath("$[0].paymentStatus").value("CAPTURED"))
                .andExpect(jsonPath("$[0].items", hasSize(1)))
                .andExpect(jsonPath("$[0].items[0].name").value("T-shirt"))
                .andExpect(jsonPath("$[0].items[0].price").value(19.99))
                .andExpect(jsonPath("$[0].items[0].quantity").value(5))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].paymentType").value("PAYPAL"))
                .andExpect(jsonPath("$[1].paymentStatus").value("CANCELED"))
                .andExpect(jsonPath("$[1].items", hasSize(2)))
                .andExpect(jsonPath("$[1].items[0].name").value("Bike"))
                .andExpect(jsonPath("$[1].items[0].price").value(208.00))
                .andExpect(jsonPath("$[1].items[0].quantity").value(1))
                .andExpect(jsonPath("$[1].items[1].name").value("Shoes"))
                .andExpect(jsonPath("$[1].items[1].price").value(30.00))
                .andExpect(jsonPath("$[1].items[1].quantity").value(1));
    }
}
