@transferencia @ui
Feature: Transferencia de dinero entre dos cuentas
  Como cliente autenticado de ParaBank
  Quiero transferir dinero entre mis cuentas
  Para mover fondos según mis necesidades

  Background:
    Given estoy autenticado en ParaBank
    And conozco el número de mi cuenta principal
    And dispongo de una segunda cuenta para recibir los fondos

  @smoke
  Scenario: Transferencia exitosa entre dos cuentas propias
    When transfiero "25.00" dólares de mi cuenta origen a mi cuenta destino
    Then el portal confirma que la transferencia fue completada
    And el saldo de la cuenta origen disminuye en "25.00"
    And el saldo de la cuenta destino aumenta en "25.00"

  Scenario: El portal no procesa una transferencia sin monto
    When transfiero "" dólares de mi cuenta origen a mi cuenta destino
    Then el portal no confirma la transferencia
    And muestra el mensaje de validación "The amount cannot be empty."
