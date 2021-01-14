# @author Lukas Amtoft Dahl
Feature: Token

  Scenario: Request 5 tokens
    Given a customer with a random account id
    When the customer requests 5 tokens
    Then he has 5 tokens
    And he has not gotten an error

  Scenario: Request 6 tokens in the right order
    Given a customer with a random account id
    When the customer requests 1 tokens
    And the customer requests 5 tokens
    Then he has 6 tokens
    And he has not gotten an error

  Scenario: Request 6 tokens from scratch in the wrong order
    Given a customer with a random account id
    When the customer requests 5 tokens
    And the customer requests 1 tokens
    Then he has 5 tokens
    And he has gotten an error

  Scenario: Request 6 tokens from scratch in one go
    Given a customer with a random account id
    When the customer requests 6 tokens
    Then he has 0 tokens
    And he has gotten an error