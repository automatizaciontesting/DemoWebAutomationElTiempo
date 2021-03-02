package com.eltiempo.utilidades;

import static org.junit.Assert.fail;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import java.util.logging.Logger; 


public class Utilidades extends PageObject {

	public static final String TASKLIST = "tasklist";
	public static final String KILL = "taskkill /F /IM ";
	private static  String rutaEvidencias;
	private static  String tagActual;
	private int contadorLogin=3;	

	public static void setRUTAEVIDENCIAS(String nuevaRuta){ rutaEvidencias = nuevaRuta;}
	public static String getRUTAEVIDENCIAS(){ return rutaEvidencias;}
	public static void setTagActual(String tag){ tagActual = tag;}
	public static String getTagActual(){ return tagActual;} 
	private static final Logger LOGGER = Logger.getLogger(Utilidades.class.getName());
	private static final String BORDE_OBJETO="arguments[0].scrollIntoView(true);arguments[0].style.border='2px dashed blue';";
	public static final String URL_WEB = "urlWeb";
	
    
	/**
	 *Bordea un elemento con un formato de color azul para identificar el objeto espefico donde interactua el robot
	 *
	 * @param webElement elemento de tipo WebElement 
	 */
	public void bordearObjetoW(WebElement webElement) {
		try {
			((JavascriptExecutor) getDriver()).executeScript(BORDE_OBJETO,webElement);
		} catch (Exception e) {
			LOGGER.info("Error, bordearObjetoW"+e.toString());
		}
	}
	/**
	 *Bordea un elemento con un formato de color azul para identificar el objeto espefico donde interactua el robot
	 *
	 * @param webElement elemento de tipo string (solo xpath) 
	 */
	public void bordearObjetoS(String elementString) {
		((JavascriptExecutor) getDriver()).executeScript(BORDE_OBJETO, 
				getDriver().findElement(By.xpath(elementString)));
		waitFor(2000).millisecond();
	}
	/**
	 *Obtener el texto del objeto en el webElement
	 *
	 * @param objeto elemento de tipo WebElement 
	 * @return devuelve el valor del objeto de tipo string
	 */

	public String obtenerTexto(WebElement objeto) {
		try {
			String webElement = waitFor(objeto).getTextValue();
			Serenity.takeScreenshot();
			return webElement;
		} catch (Exception e) {
			this.mensajeCatch("obtenerTexto del objeto: " + objeto);
			return null;
		}
	}
	/**
	 *Obtener en las excepciones el 
	 *
	 * @param objeto elemento de tipo WebElement 
	 * @return devuelve el valor del objeto de tipo string
	 */

	public void mensajeCatch(String accion) {
		Serenity.takeScreenshot();
		fail("Falla en la accion: " + accion + " el objeto no se encontro.");
	}
	/**
	 *Finalizar el proceso actual en windows 
	 *
	 * @param serviceName nombre del proceso 
	 */
	public void killProcess(String process) throws Exception {
		Runtime.getRuntime().exec(KILL + process);

	}
	public void killProcessDrivers() throws Exception {
		Runtime.getRuntime().exec(KILL + "chromedriver83.exe");
		Runtime.getRuntime().exec(KILL + "geckodriver26.exe");
		Runtime.getRuntime().exec(KILL + "MicrosoftWebDriver.exe");
	}
	/**
	 *Retorno de tipo booleano de un proceso en su estado, verdadero si esta activo, falso si no lo encuenta 
	 *
	 * @param serviceName nombre del proceso 
	 */

	public boolean isProcessRunning(String serviceName) throws Exception {

		Process p = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains(serviceName)) {
				return true;
			}
		}

		return false;

	}
	/**
	 *Aceptar una alert, mensajes construidos con javaScript y donde el robot no puede intervenir 
	 *
	 */

	public void alertaAceptar() {
	       try {
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
	       } catch (Exception e) {
	             LOGGER.info(e.getMessage());
	             fail();
	       }
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
            LOGGER.info(e.getMessage());
		}
	}
	/**
	 *Tomar evidencia de la pantalla actual por medio de las librerias robot, abrirla y tomar un segundo pantallazo con serenity.takesreenshot
	 */
	public void imagenPantallaActual() {
       try {
    	   	 String uniqueID = UUID.randomUUID().toString();
             String inicial = getDriver().getWindowHandle();
             tomascreenshot(getRUTAEVIDENCIAS()+"\\"+uniqueID+".png");
             getJavascriptExecutorFacade().executeScript("window.open();");
             getDriver().switchTo().window( new ArrayList<String>(getDriver().getWindowHandles()).get(1));
             getDriver().get(getRUTAEVIDENCIAS()+"\\"+uniqueID+".png");
             Serenity.takeScreenshot();
             getDriver().close();
             getDriver().switchTo().window(inicial);
       } catch (Exception e) {
           	LOGGER.info(e.getMessage());
             fail();
       }
	}
	/**
	 *Busca el elemento en la pagina para posicionarlo y sea visible
	 *@param strWebElement  variable de tipo WebElementFacade
	 */
	public void posicionarElemento(WebElementFacade strWebElement) {
		((JavascriptExecutor) getDriver()).executeScript(BORDE_OBJETO, strWebElement);
	}
	/**
	 *Busca el elemento en la pagina para posicionarlo y sea visible
	 *@param strWebElement  variable de tipo string
	 */

	public void posicionarElementoString(String webElement) {
		((JavascriptExecutor) getDriver()).executeScript(
				"arguments[0].scrollIntoView(true); ",
				getDriver().findElements(By.xpath(webElement)).get(0));
		waitFor(800).milliseconds();
	}
	/**
	 *obtener la ruta de las evidencias configutada en serenity.properties
	 *@param tags  Nombre de caso de prueba
	 */

	public static String obtenerRutaEvidencia(String tags) {
		String rutaCarpetaEvidencias = null;
		File miDir = new File (".");
		try {			
			Date objDate = new Date();
			String strDateFormat = "_dd_MMM_yyyy_hh_mm_ss";
			SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
			rutaCarpetaEvidencias=miDir.getCanonicalPath()+ getSerenityProperties("serenity.outputDirectory").replace("/", "\\")+tags+objSDF.format(objDate);
		}catch(Exception e) {
			LOGGER.info(e.getMessage());
		}
		return rutaCarpetaEvidencias;
	}
	/**
	 *Validar si el objeto se encuentra visible en la pantalla 
	 *@param objeto  Elemento del front-end
	 */

	public boolean disponibleObjetoPantallaWEF(WebElementFacade objeto) {
		try {

			if(objeto.isDisplayed()) {
				return true;	
			}
		}catch(Exception e) {
			return false;
		}
		return false;
	}
	/**
	 *Validar si el objeto se encuentra visible en la pantalla 
	 *@param objeto  Elemento del front-end de tipo string
	 */
	public boolean disponibleObjetoPantallaString(String objeto) {
			WebDriverWait  wait = new WebDriverWait(getDriver(),150);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objeto)));
			waitFor(1500).millisecond();
			return true;		
	}
	/**
	 *Obtener la ruta de la carpeta de descargas y ordenar por descendente los elementos
	 *@return confirma que realizo la actividad de forma correcta
	 */

	public Boolean obtenerCarpetaDescargas(String aplicativo){
		String home = System.getProperty("user.home");
		try {
			File miDir = new File (".");
			home = home + "\\Downloads";
			waitFor(5000).milliseconds();
			File directory = new File(home);
            File[] fList = directory.listFiles();
            Arrays.sort(fList,LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            ProcessBuilder pb = new ProcessBuilder(aplicativo, fList[0].getAbsolutePath());
            pb.start().isAlive();	
            waitFor(8000).milliseconds();
	        tomascreenshot(getRUTAEVIDENCIAS()+"\\evidencia.png");
	        waitFor(2000).milliseconds();
	        Runtime.getRuntime().exec("taskkill /IM "+aplicativo);
	        String inicial = getDriver().getWindowHandle();     
            getJavascriptExecutorFacade().executeScript("window.open()");
            ArrayList<String> tabs = new ArrayList<> (getDriver().getWindowHandles());
            Serenity.getWebdriverManager().getWebdriver().switchTo().window(tabs.get(1)); //switches to new tab
            Serenity.getWebdriverManager().getWebdriver().get("file://"+miDir.getCanonicalPath()+"\\src\\test\\resources\\demoEvidencia\\demoEvidencia.html");
            waitFor(3000).milliseconds();
            Serenity.getWebdriverManager().getWebdriver().get(getRUTAEVIDENCIAS() + "\\evidencia.png");          
            waitFor(3000).milliseconds();
            Serenity.takeScreenshot();
            Serenity.recordReportData().withTitle("Log cargue usuario").andContents("Ver ");  
            Serenity.getWebdriverManager().getWebdriver().close();
            Serenity.getWebdriverManager().getWebdriver().switchTo().window(inicial);         
          
	        return true;
		}catch(Exception e) {
			LOGGER.info("Utilidades, error obtenerCarpetaDescargas: "+e.getMessage());
			return false;
		}
		
	}
	public void ejecutarProceso(File[] fList, String aplicativo) {
	try {
			Arrays.sort(fList,LastModifiedFileComparator.LASTMODIFIED_REVERSE);
			Runtime.getRuntime().exec(new String[] {"cmd", "/C", "start", aplicativo, fList[0].getAbsolutePath()}).waitFor();
		}catch(Exception e) {
			mensajeCatch("ejecutarProceso,Runtime.getRuntime() ");
		}
		
		
		
	}
	/**
	 *Validar si se encuentra visible objeto div de carga de archivo de clientes
	 *@return confirma que realizo la actividad de forma correcta
	 */

	public void esperaCargarObjetoLoading(String divLoading) {
		try {
			int intentos=0;
			while(find(By.xpath(divLoading)).getCssValue("display").contains("block")) {
				waitFor(300);
				if(intentos>900) {
					getJavascriptExecutorFacade().executeScript("window.scrollTo(0, -document.body.scrollHeight);");
					mensajeCatch("esperaCargarObjectoLoading ");
				}
			}	
		}catch(Exception e) {
			LOGGER.info(e.getMessage());
		}

	}
	/**
	 *Remover duplicados de un array
	 *@param warniningCargue variable con valores de tipo ArrayList
	 *@return devolver la lista sin valores duplicados
	 */
    public List<String> removeDuplicates(List<String> warniningCargue) 
    { 
    	List<String> newList=new ArrayList<>();
        for (String element : warniningCargue) { 
            if (!newList.contains(element)) {  
                newList.add(element); 
            } 
        } 
        return newList; 
    } 
	/**
	 *Validar si el dato de tipo string esta vacio o contiene algun dato
	 *@param dato variable de tipo String
	 *@return devolver la lista sin valores duplicados
	 */

    public boolean datoActualVariable(String dato) {
    	try {
    		if(dato.isEmpty()) {
    			return false;
    		}else {
    			return true;
    		}
    			
    	}catch(Exception e) {
    		LOGGER.info("datoActualVariable");
    	}
		return false;
    	
    }
	/**
	 *Obtener el valor de una variable implementada en serenity.properties
	 *@param variable contiene el dato de la llave de configurada
	 *@return devolver el valor de la llave configurada
	 */
    public static String getSerenityProperties(String variable) {
    	EnvironmentVariables environmentVariables=null;
    	String datoSerenity = "datoVaciogetSerenityProperties";
    	try {
        	environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        	datoSerenity=environmentVariables.getProperty(variable);
    	}catch(Exception e) {
    		LOGGER.info("getSerenityProperties");	
    	}
    	return	datoSerenity;
		     	
    }
	/**
	 *Abrir la url parametrizada desde el dataDriven 
	 *@param url contiene el valor de la url del navegador
	 *@param navegador contiene el tipo de navegador con el cual se debe ejecutar la prueba
	 */
	public void abrirUrl(String url, String navegador) {
		try {
			
			if(Utilidades.getSerenityProperties("webdriver.base.url") == null) {
				Serenity.setSessionVariable(URL_WEB).to(url);
			}else {
				Serenity.setSessionVariable(URL_WEB).to(Utilidades.getSerenityProperties("webdriver.base.url"));	
			}
			killProcessDrivers();	
			Serenity.getWebdriverManager().getWebdriver().manage().deleteAllCookies();
			openAt(Serenity.sessionVariableCalled(URL_WEB));
			Serenity.getWebdriverManager().getWebdriver().manage().window().maximize();
			
	    } catch (Exception e) {
	    	if(contadorLogin<=0) {
	    		LOGGER.info("Utilidades, abrirUrl "+ e.getMessage());
		        fail();
	    	}else {
	    		waitFor(600).milliseconds();
	    		contadorLogin=contadorLogin-1;
	    		abrirUrl(url,navegador);
	    	}
	    }
	}

	/**
	 *Transformar el numero de un mes por  
	 *@param replace contiene el valor del mes
	 *@return devuelve el valor transformado del mes
	 */
	public String transformarMesEspanol(String valorReemplazar) {
		
		switch(valorReemplazar) {
		case "01": valorReemplazar= "Ene"; 
		break ;
		case "02": valorReemplazar= "Feb"; 
		break ; 
		case "03": valorReemplazar= "Mar"; 
		break ; 
		case "11": valorReemplazar= "Nov";
		break ; 
		case "05": valorReemplazar= "May"; 
		break ;
		case "08": valorReemplazar= "Ago"; 
		break ; 
		case "10": valorReemplazar= "Oct"; 
		break ; 		
		case "12": valorReemplazar= "Dic"; 
		break ; 
		case "04": valorReemplazar= "Abr"; 
		break ; 
		case "06": valorReemplazar= "Jun"; 
		break ; 
		case "09": valorReemplazar= "Sep"; 
		break ; 
		case "07": valorReemplazar= "Jul"; 
		break ; 		
			default: 
				valorReemplazar="nulo";
				break;
		}
		return valorReemplazar;
		
	}

	public void menuLateralPortalNatural(String titulo) {
		try {
			find(By.xpath("//a[contains(.,'"+titulo+"')]")).click();
			Serenity.takeScreenshot();
		}catch(Exception e) {
    		LOGGER.info("Utilidades, menuLateralPortalNatural "+ e.getMessage());
	        fail();
		}
		
	}
	public void defaultContent() {
		getDriver().switchTo().defaultContent();
	}
	public void abrirConsolaChrome() {
		try {
			Serenity.takeScreenshot();
			waitFor(3000).milliseconds();			
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_F12);
			robot.keyRelease(KeyEvent.VK_F12);
			waitFor(3000).milliseconds();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			waitFor(3000).milliseconds();
			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_RIGHT);
			robot.keyRelease(KeyEvent.VK_RIGHT);
			waitFor(3000).milliseconds();
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_L);
			robot.keyRelease(KeyEvent.VK_L);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			getDriver().switchTo().defaultContent();
		}catch(Exception e) {
			LOGGER.info("Utilidades, abrirConsolaChrome "+ e.getMessage());
	        fail();
		}
		
	}
	public void irPaginaAtras() {
		try {		
			getJavascriptExecutorFacade().executeScript("window.history.go(-1)");
		} catch (Exception e) {
			mensajeCatch("irPaginaAtras "+e.getMessage());
		}
		
	}
	
	public void deshabilitarWrnCargueClientes(String string) {
		try {	
			
			int cantidadMensajesAsociarClientes=findAll(By.xpath("//*[@id='toast-container']//div[@role='alertdialog' and contains(.,'"+string+"')]")).size();			
			for(int i=0;i<=cantidadMensajesAsociarClientes-1;i++) {
				Serenity.takeScreenshot();
				findAll(By.xpath("//*[@id='toast-container']//div[@role='alertdialog' and contains(.,'"+string+"')]")).get(i).click();
			}
			
		} catch (Exception e) {
			mensajeCatch("gestor vitrinas, deshabilitarWrnCargueClientes "+e.getMessage());
		}
		
	}

	public void reloadPage() {
		getDriver().navigate().refresh();
		waitFor(1000).milliseconds();
	}

	public void seleccionarFecha(String dia, String mes, String ano, String btnFecha,String textMesActual, String btnFechaBck,String cellDia) {
		 waitFor(1000).milliseconds();
		getDriver().findElement(By.xpath(btnFecha)).click();
		Serenity.takeScreenshot();
		while (!getDriver().findElement(By.xpath(textMesActual))
				.getText().contains(mes)
				|| !getDriver().findElement(By.xpath(textMesActual))
						.getText().contains(ano)) {
			getDriver().findElement(By.xpath(btnFechaBck))
					.click();
		}
		for (int contDias = 0; contDias <= 7; contDias++) {
			if (getDriver().findElement(By.xpath(cellDia + contDias + "']")).getText()
					.equals("1")) {
				while (!getDriver().findElement(By.xpath(cellDia + contDias + "']"))
						.getText().equals(dia)) {
					LOGGER.info("Utilidades, seleccionarFecha sin valor ");
				}
			}

		}
	}
	
	public void posicionPagina(String posicion) {
		try {
				
		switch(posicion.toLowerCase()) {
			case "abajo":case "bajo":
				getJavascriptExecutorFacade().executeScript("window.scrollTo(0,document.body.scrollHeight);");
				waitFor(2000).millisecond();
				break;
			case "medio":case "mitad":
				int altoPantalla = getDriver().manage().window().getSize().getHeight();
				altoPantalla=altoPantalla/2;
				getJavascriptExecutorFacade().executeScript("window.scrollTo(0,"+altoPantalla+");");
				break;
			case "arriba":
				getJavascriptExecutorFacade().executeScript("window.scrollTo(0, -document.body.scrollHeight);");
				waitFor(2000).millisecond();
				break;
				default:
					break;
			}
		}catch(Exception e) {
			posicionPagina(posicion);
		}
	}
	
	public void validarDescarga() {
		String txtAlerta = "//*[@id='popup_box']/div/div[2]/div[3]";
		waitFor(3000).milliseconds();
		Serenity.takeScreenshot();
		if (disponibleObjetoPantallaString(txtAlerta))
			fail("Error al generar el archivo, el mensaje encontrado es: "
					+ getDriver().findElement(By.xpath(txtAlerta)).getText());
		else {
			obtenerCarpetaDescargas("chrome");
		}
	}
	public void eventoRobot(String evento) throws AWTException {
		Robot robot = new Robot();
		switch(evento) {
		case "Esc":case "Escape":
			waitFor(3000);
			robot.keyPress(KeyEvent.VK_ESCAPE);
			break;
			default:
				break;
		
		}
	}
	public void evidencia() {
		waitFor(3000);
		Serenity.takeScreenshot();
		
	}

	public void iframeBannerPortal() {
		getDriver().switchTo().defaultContent();
		getDriver().switchTo().parentFrame();
		getDriver().switchTo().frame("imgBanner1");	
		find(By.xpath("//*[@id='myCarousel']")).isEnabled();
		getDriver().switchTo().defaultContent();
	}

	
}



