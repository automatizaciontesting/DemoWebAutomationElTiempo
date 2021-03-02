package com.eltiempo.pageobjects;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import com.eltiempo.utilidades.*;

public class LoginPageObjects extends PageObject {

	/*Declarar varibles de mapeo de objetos*/ 
	Utilidades util;
	
	@FindBy(xpath = "//*[@name='user']")
	private WebElementFacade txtUsername;
	
	@FindBy(xpath = "//*[@name='password']")
	private WebElementFacade txtUPassword;
	
	@FindBy(xpath = "//*[@value='Login']")
	private WebElementFacade btnLogin;
	
	@FindBy(xpath = "//*[@id='available']//h2")
	private WebElementFacade lblAvalibleBooks;
	
	public void ingresoTxtUserName(String user) {
		util.bordearObjetoW(txtUsername);
		txtUsername.typeAndTab(user);
		
	}

	public void ingresoTxtPassword(String pass) {
		util.bordearObjetoW(txtUPassword);
		txtUPassword.typeAndTab(pass);
		
	}

	public void btnLogin() {
		util.bordearObjetoW(btnLogin);
		btnLogin.click();
		
	}

	public boolean validoMensajeDeRegistroExitoso() {
		util.bordearObjetoW(lblAvalibleBooks);
		return lblAvalibleBooks.isPresent();
		
	}

}
