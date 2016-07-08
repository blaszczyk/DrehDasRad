package com.drehdasrad;

import java.io.*;
import java.util.List;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;


public class DrehDasRad {

	private static void clickRepeatedly(HtmlButtonInput button) throws Exception
	{
		String value = "nix";
		char c = 'S';
		int counter = 0;
		long lastTime = System.currentTimeMillis();
		long thisTime;
		while( "SV. ".contains( "" + c ) )
		{
			value = button.getValueAttribute();
			c = value.charAt(0);
			if( !button.isDisabled() )
			{
				counter++;
				thisTime = System.currentTimeMillis();
				System.out.println("" + counter + ": " + value + "\t\t" + (thisTime-lastTime) );
				lastTime = thisTime;
				if( c == 'S' || c == 'V'  )
					button.click();
			}
			int waitTime = 500;
			if( c == 'S' )
				waitTime += (int)(Math.random() * 5000);
			synchronized(value){
				value.wait(waitTime);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		System.setErr( new PrintStream( new OutputStream( ) {
			@Override
			public void write(int b) throws IOException {}
		}));
		
        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage("http://em2016.tippspielwelt.de/");
        
        HtmlInput user = page.getElementByName("user");
        HtmlInput pass = page.getElementByName("pass");
        
        user.setValueAttribute("username");// Benutzername Eingeben
        pass.setValueAttribute("password"); //Password Eingeben
        
        List<DomElement> loginelements = page.getElementsByName("login");
        HtmlSubmitInput login = (HtmlSubmitInput) loginelements.get(1);
        page = login.click();
        
        page = webClient.getPage("http://em2016.tippspielwelt.de/index.php?page=rangliste&");

        List<DomElement> buttons = page.getElementsByIdAndOrName("control");        
        HtmlButtonInput spin = (HtmlButtonInput) buttons.get(0);

        clickRepeatedly(spin);
            
        webClient.close();
	}
}