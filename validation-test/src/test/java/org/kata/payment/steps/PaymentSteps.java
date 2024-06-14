package org.kata.payment.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.kata.payment.application.ports.input.ManagePayment;
import org.kata.payment.configuration.CucumberSpringConfiguration;
import org.kata.payment.domain.aggregat.Payment;
import org.kata.payment.domain.valueobject.PaymentId;
import org.kata.payment.infrastructure.adapters.inbound.rest.dto.PaymentRequest;
import org.kata.payment.infrastructure.adapters.inbound.rest.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PaymentSteps extends CucumberSpringConfiguration {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ManagePayment managePayment;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Payment createdPayment;

    @Given("I create a transaction with payment type {word} for {int} T-shirts costing {double} Euros each")
    public void createTransaction(String paymentType, int quantity, double price) throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        var paymentRequest = new PaymentRequest(
                Payment.PaymentType.valueOf(paymentType),
                Payment.PaymentStatus.NEW,
                List.of(new PaymentRequest.ItemRequest("T-shirt", price, quantity))
        );

        var response = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var paymentResponse = objectMapper.readValue(response, PaymentResponse.class);
        createdPayment = managePayment.readingPayment(new PaymentId(paymentResponse.id()));
    }

    @When("I modify the transaction to status AUTHORIZED")
    public void modifyTransactionToAuthorized() throws Exception {
        createdPayment.setStatus(Payment.PaymentStatus.AUTHORIZED);
        var paymentRequest = new PaymentRequest(
                createdPayment.getPaymentType(),
                createdPayment.getStatus(),
                createdPayment.getItems().stream()
                        .map(item -> new PaymentRequest.ItemRequest(item.name(), item.price().amount(), item.quantity()))
                        .toList()
        );

        mockMvc.perform(put("/payments/" + createdPayment.getId().id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk());

        createdPayment = managePayment.readingPayment(createdPayment.getId());
    }

    @When("I modify the transaction to status CAPTURED")
    public void modifyTransactionToCaptured() throws Exception {
        createdPayment.setStatus(Payment.PaymentStatus.CAPTURED);
        PaymentRequest paymentRequest = new PaymentRequest(
                createdPayment.getPaymentType(),
                createdPayment.getStatus(),
                createdPayment.getItems().stream()
                        .map(item -> new PaymentRequest.ItemRequest(item.name(), item.price().amount(), item.quantity()))
                        .toList()
        );

        mockMvc.perform(put("/payments/" + createdPayment.getId().id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk());

        createdPayment = managePayment.readingPayment(createdPayment.getId());
    }

    @Then("the transaction should have status {word}")
    public void verifyTransactionStatus(String status) {
        assertThat(createdPayment.getStatus()).isEqualTo(Payment.PaymentStatus.valueOf(status));
    }

    @Given("I create a transaction with payment type PAYPAL for {int} bike costing {double} Euros and {int} pair of shoes costing {double} Euros")
    public void createPaypalTransaction(int bikeQuantity, double bikePrice, int shoesQuantity, double shoesPrice) throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        PaymentRequest paymentRequest = new PaymentRequest(
                Payment.PaymentType.PAYPAL,
                Payment.PaymentStatus.NEW,
                List.of(
                        new PaymentRequest.ItemRequest("Bike", bikePrice, bikeQuantity),
                        new PaymentRequest.ItemRequest("Shoes", shoesPrice, shoesQuantity)
                )
        );

        String response = mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PaymentResponse paymentResponse = objectMapper.readValue(response, PaymentResponse.class);
        createdPayment = managePayment.readingPayment(new PaymentId(paymentResponse.id()));
    }

    @When("I modify the transaction to status CANCELED")
    public void modifyTransactionToCanceled() throws Exception {
        createdPayment.setStatus(Payment.PaymentStatus.CANCELED);
        PaymentRequest paymentRequest = new PaymentRequest(
                createdPayment.getPaymentType(),
                createdPayment.getStatus(),
                createdPayment.getItems().stream()
                        .map(item -> new PaymentRequest.ItemRequest(item.name(), item.price().amount(), item.quantity()))
                        .toList()
        );

        mockMvc.perform(put("/payments/" + createdPayment.getId().id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk());

        createdPayment = managePayment.readingPayment(createdPayment.getId());
    }

    @Given("I have created multiple transactions")
    public void createMultipleTransactions() throws Exception {
        createTransaction("CREDIT_CARD", 5, 19.99);
        modifyTransactionToAuthorized();
        modifyTransactionToCaptured();
        createPaypalTransaction(1, 208.00, 1, 30.00);
        modifyTransactionToCanceled();
    }

    @When("I retrieve all transactions")
    public void retrieveAllTransactions() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        String response = mockMvc.perform(get("/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PaymentResponse[] paymentResponses = objectMapper.readValue(response, PaymentResponse[].class);
        assertThat(paymentResponses).isNotNull();
        assertThat(paymentResponses.length).isGreaterThanOrEqualTo(2);
    }

    @Then("I should get all transactions")
    public void verifyAllTransactionsRetrieved() {
        List<Payment> payments = managePayment.getAllPayments();
        assertThat(payments).hasSizeGreaterThanOrEqualTo(2);
    }
}
