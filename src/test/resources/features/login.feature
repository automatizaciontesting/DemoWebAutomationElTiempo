#Author: hervincamargo@gmail.com
@IngresoPaginaSahiTrainingSite
Feature: Logueo en pagina Sahi Training Site

  @LoginExitoso
  Scenario Outline: Logueo exitoso pagina Sahi Training Site
    Given Que me encuentro en la pagina "http://sahitest.com/demo/training/login.htm"
    When Ingreso las credenciales de acceso
      | userName | <userName> |
      | password | <password> |
    Then Valido ingreso exitoso

    Examples: 
      | userName | password |
      | test     | secret   |
