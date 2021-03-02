#Author: hervincamargo@gmail.com
@FormatoRegistro
Feature: Registro en pagina Sahi Training Site
  Como usuario quiero realizar el registro en la pagina Sahi Training Site

  @RegistroExitoso
  Scenario Outline: Registro exitoso de usuario en Sahi Training Site
    Given Que me encuentro en la pagina "http://sahitest.com/demo/training/login.htm"
    When Selecciono la opción register e ingreso los valores
      | userName       | <userName>       |
      | password       | <password>       |
      | repeatPassword | <repeatPassword> |
      | gender         | <gender>         |
      | address        | <address>        |
      | billingAddress | <billingAddress> |
      | state          | <state>          |
    Then Valido mensaje de registro exitoso

    Examples: 
      | userName       | password | repeatPassword | gender | address         | billingAddress  | state      |
      | Hervin Camargo |   123456 |         123456 | Male   | cra 106 #141-32 | cra 106 #141-32 | Tamil Nadu |
