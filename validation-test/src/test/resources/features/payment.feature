@payment
Feature: Payment Management

  Scenario: Create and modify a CREDIT_CARD transaction
    Given I create a transaction with payment type CREDIT_CARD for 5 T-shirts costing 19.99 Euros each
    When I modify the transaction to status AUTHORIZED
    And I modify the transaction to status CAPTURED
    Then the transaction should have status CAPTURED

  Scenario: Create and modify a PAYPAL transaction
    Given I create a transaction with payment type PAYPAL for 1 bike costing 208.00 Euros and 1 pair of shoes costing 30.00 Euros
    When I modify the transaction to status CANCELED
    Then the transaction should have status CANCELED

  Scenario: Retrieve all transactions
    Given I have created multiple transactions
    When I retrieve all transactions
    Then I should get all transactions
