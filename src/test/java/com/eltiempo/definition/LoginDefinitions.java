package com.eltiempo.definition;

import java.io.IOException;
import java.util.Map;

import com.eltiempo.steps.LoginSteps;


import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

public class LoginDefinitions {

	@Steps
	LoginSteps loginSteps;
	
	@When("^Ingreso las credenciales de acceso$")
	public void ingresoCredencialesAcceso(Map<String,String> data) {
		loginSteps.ingresoCredencialesAcceso(data);
	}

	@Then("^Valido ingreso exitoso$")
	public void validoMensajeDeRegistroExitoso() throws IOException {
		loginSteps.validoMensajeDeRegistroExitoso();
		
	}

}
