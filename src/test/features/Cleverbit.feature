Feature: Cleverbit


  @1
  Scenario: Check money exchange rate
    Given Open the https://www.xe.com/currencyconverter/ URL
    Then I need to wait for 3 seconds
    Then I see calculator page
    Then I click element: amount at index 1
    Then I enter "5.50" text to amount at index 1
    Then I click element: fromCurrency at index 1
    Then I choose "currencyListFrom" with the "EUR" tag
    Then I click element: toCurrency at index 1
    Then I choose "currencyListTo" with the "TRY" tag
    Then I click element: convertButton at index 1
    Then I need to wait for 5 seconds