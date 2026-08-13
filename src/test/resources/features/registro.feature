@registration @ui
Feature: Registro de un nuevo usuario
  Como visitante del portal ParaBank
  Quiero crear una cuenta de cliente
  Para poder operar con mis productos bancarios

  @smoke
  Scenario: Registro exitoso de un nuevo usuario
    Given el visitante se encuentra en la página de registro
    When completa el formulario con datos válidos y lo envía
    Then el portal confirma que la cuenta fue creada
    And el usuario queda autenticado con acceso al resumen de cuentas

  Scenario: El portal rechaza un nombre de usuario ya existente
    Given existe un usuario registrado en ParaBank
    When intenta registrarse nuevamente con el mismo nombre de usuario
    Then el portal muestra el error "This username already exists."

  Scenario: El formulario exige los campos obligatorios
    Given el visitante se encuentra en la página de registro
    When envía el formulario de registro sin completar ningún campo
    Then el portal exige los siguientes campos obligatorios:
      | First name is required.             |
      | Last name is required.              |
      | Address is required.                |
      | City is required.                   |
      | State is required.                  |
      | Zip Code is required.               |
      | Social Security Number is required. |
      | Username is required.               |
      | Password is required.               |
      | Password confirmation is required.  |
