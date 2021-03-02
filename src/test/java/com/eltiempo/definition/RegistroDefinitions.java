package com.eltiempo.definition;

import java.io.IOException;
import java.util.Map;
import com.eltiempo.steps.RegistroSteps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

public class RegistroDefinitions {
	
	@Steps
	RegistroSteps registroSteps;
	
	@Given("^Que me encuentro en la pagina \"([^\"]*)\"$")
	public void queMeEncuentroEnLaPagina(String url) {
		registroSteps.queMeEncuentroEnLaPagina(url);
	}


	@When("^Selecciono la opción register e ingreso los valores$")
	public void seleccionoLaOpciónRegisterEIngresoLosValores(Map<String,String> data) {
		registroSteps.seleccionoLaOpcionRegisterEIngresoLosValores(data);
	}

	@Then("^Valido mensaje de registro exitoso$")
	public void validoMensajeDeRegistroExitoso() throws IOException {
		registroSteps.validoMensajeDeRegistroExitoso();
	}
	
}
