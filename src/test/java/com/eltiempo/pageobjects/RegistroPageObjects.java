package com.eltiempo.pageobjects;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

public class RegistroPageObjects extends PageObject {
	
	/*Declarar varibles de mapeo de objetos*/
	
	@FindBy(xpath = "//*[text()='Register']")
	private WebElementFacade irPaginaRegister;
	
	@FindBy(xpath = "//*[@id='uid']")
	private WebElementFacade txtUsername;
	
	@FindBy(xpath = "//*[@id='pid']")
	private WebElementFacade txtPassword;
	
	@FindBy(xpath = "//*[@id='pid2']")
	private WebElementFacade txtRepeatPassword;
	
	@FindBy(xpath = "//*[@value='M']")
	private WebElementFacade rdbMale;
	
	@FindBy(xpath = "//*[@value='F']")
	private WebElementFacade rdbFemale;
	
	@FindBy(xpath = "//*[@id='taid']")
	private WebElementFacade txtAddress;
	
	@FindBy(xpath = "//*[@id='btaid']")
	private WebElementFacade txtBilling;	
	
	@FindBy(xpath = "//select[@name='state']")
	private WebElementFacade cmbState;	

	@FindBy(xpath = "//*[@name='agree']")
	private WebElementFacade chkAgree;	
	
	@FindBy(xpath = "//*[@value='Register']")
	private WebElementFacade btnRegister;		
	
	@FindBy(xpath = "//*[text()='Go back to login']")
	private WebElementFacade lnkVolverLogin;	
	
	public void irPaginaRegister() {
		irPaginaRegister.click();
		
	}
	
	public void ingresarTxtUserName(String user) {
		txtUsername.typeAndTab(user);
		
	}

	public void ingresarTxtPassword(String pass) {
		txtPassword.typeAndTab(pass);
		
	}

	public void ingresarTxtRepeatPassword(String repeatPass) {
		txtRepeatPassword.typeAndTab(repeatPass);
		
	}

	public void seleccinarRdbGender(String string) {
		rdbMale.click();
		
	}

	public void ingresarTxtAddress(String address) {
		txtAddress.typeAndTab(address);
		
	}

	public void ingresarTxtBillingAddress(String billing) {
		txtBilling.typeAndTab(billing);
		
	}

	public void ingresarcmbState(String state) {
		//cmbState
		WebElement identifier = getDriver().findElement(By.xpath("//select[@name='state']"));
	    Select seleccionDato = new Select(identifier);
	    seleccionDato.selectByVisibleText(state);
		
	}

	public void seleccionaChkAggre() {
		chkAgree.click();
		
	}


	public String validoMensajeRegistro() throws IOException {
		btnRegister.click();
		capturaPantallaPopUp();
        btnRegister.click();
		String valorPopUp=getDriver().switchTo().alert().getText();
		Serenity.takeScreenshot();
		return valorPopUp;
		
	}

	/**
	 *Tomar evidencia de la pantalla actual por medio de las librerias robot 
	 *@param rutaEvidencia  ruta de la evidencia del robot, por lo general en la ruta del proyecto de automatización, donde es alojada
	 */
	public static void tomascreenshot(String rutaEvidencia){
		try {
			Robot robot = new Robot();			
			BufferedImage screenShot = robot
					.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenShot, "PNG", new File(rutaEvidencia));
		}catch(Exception e) {
			System.out.println("tomascreenshot"+e.getMessage());
		}
	}

	public void capturaPantallaPopUp() throws IOException {
        File miDir = new File (".");
        String inicial = getDriver().getWindowHandle();
        waitFor(300).milliseconds();
        tomascreenshot(miDir.getCanonicalPath()+"\\target\\site\\serenity\\evidencia.png");
        getAlert().accept();
        getJavascriptExecutorFacade().executeScript("window.open('https://www.w3schools.com/jsref/met_win_open.asp');");
        getDriver().switchTo().window( new ArrayList<String>(getDriver().getWindowHandles()).get(1));
        getDriver().get(miDir.getCanonicalPath()+"\\target\\site\\serenity\\evidencia.png");
        Serenity.takeScreenshot();
        getDriver().close();
        getDriver().switchTo().window(inicial);
	}

	public void volverLogin() {
		lnkVolverLogin.click();
	}
	
}
