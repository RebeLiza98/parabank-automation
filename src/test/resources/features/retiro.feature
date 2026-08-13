@retiro @api
Feature: Retiro de dinero
  Como cliente de ParaBank
  Quiero retirar dinero de mi cuenta
  Para disponer de mis fondos

  # El portal no expone la operación de retiro por interfaz gráfica, por lo que
  # esta funcionalidad se automatiza contra el servicio REST del banco. La
  # cuenta pertenece al usuario creado en el Background.
  Background:
    Given estoy autenticado en ParaBank
    And conozco el número de mi cuenta principal

  @smoke
  Scenario: Retiro exitoso descuenta el importe del saldo
    When solicito un retiro de "50.00" dólares sobre mi cuenta
    Then el servicio confirma el retiro
    And el saldo de mi cuenta disminuye en "50.00"

  Scenario Outline: El servicio rechaza retiros sobre cuentas inexistentes
    When solicito un retiro de "<monto>" dólares sobre la cuenta "<cuenta>"
    Then el servicio responde con el código <codigo> y el mensaje "<mensaje>"

    Examples:
      | caso               | cuenta | monto | codigo | mensaje                              |
      | Cuenta inexistente | 999999 | 50.00 | 400    | Could not find account number 999999 |
      | Cuenta con ceros   | 000000 | 10.00 | 400    | Could not find account number 0      |
