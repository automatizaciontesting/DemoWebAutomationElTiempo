package com.eltiempo.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import com.eltiempo.pageobjects.*;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;

public class RegistroSteps {

	RegistroPageObjects registroPageObjects;
	
	@Step("Abrir url")
	public void queMeEncuentroEnLaPagina(String url) {		
		registroPageObjects.openAt(url);
		registroPageObjects.getDriver().manage().window().maximize();
	}
	@Step("Ingreso datos en la pagina")
	public void seleccionoLaOpcionRegisterEIngresoLosValores(Map<String, String> data) {
		registroPageObjects.irPaginaRegister();
		Serenity.takeScreenshot();
		registroPageObjects.ingresarTxtUserName(data.get("userName"));
		Serenity.takeScreenshot();
		registroPageObjects.ingresarTxtPassword(data.get("password"));
		Serenity.takeScreenshot();
		registroPageObjects.ingresarTxtRepeatPassword(data.get("repeatPassword"));
		Serenity.takeScreenshot();
		registroPageObjects.seleccinarRdbGender(data.get("gender"));
		Serenity.takeScreenshot();
		registroPageObjects.ingresarTxtAddress(data.get("address"));
		Serenity.takeScreenshot();
		registroPageObjects.ingresarTxtBillingAddress(data.get("billingAddress"));
		Serenity.takeScreenshot();
		registroPageObjects.ingresarcmbState(data.get("state"));
		Serenity.takeScreenshot();
		registroPageObjects.seleccionaChkAggre();
		Serenity.takeScreenshot();
		
		
	}
	@Step("Valido mensaje registro exitoso")
	public void validoMensajeDeRegistroExitoso() throws IOException {
		assertThat(registroPageObjects.validoMensajeRegistro(), containsString("Registered Successfully"));
		registroPageObjects.volverLogin();
		//Serenity.takeScreenshot();
		assertTrue(true);
		
	}

}
