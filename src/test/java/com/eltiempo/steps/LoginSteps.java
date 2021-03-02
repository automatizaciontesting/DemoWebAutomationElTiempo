package com.eltiempo.steps;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import com.eltiempo.pageobjects.LoginPageObjects;

import net.thucydides.core.annotations.Step;

public class LoginSteps {
	
	LoginPageObjects loginPageObjects;

	@Step("Ingreso Credenciales Acceso")
	public void ingresoCredencialesAcceso(Map<String, String> data) {
		loginPageObjects.ingresoTxtUserName(data.get("userName"));
		loginPageObjects.ingresoTxtPassword(data.get("password"));
		loginPageObjects.btnLogin();
	}
	@Step("Valido mensaje de ingreso exitoso")
	public void validoMensajeDeRegistroExitoso() {
		assertTrue(loginPageObjects.validoMensajeDeRegistroExitoso());
		assertTrue(true);
		
	}

}
