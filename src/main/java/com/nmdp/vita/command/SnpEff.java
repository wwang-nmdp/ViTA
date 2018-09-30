package com.nmdp.vita.command;


import com.nmdp.vita.Configure;
import com.nmdp.vita.util.FileHelp;

import java.io.IOException;

public class SnpEff {
	public void run(){
		//create command line
		StringBuilder sb = new StringBuilder();
		//create annotated output fp
		sb.append(String.format("java -jar %s/snpEff/snpEff.jar GRCh38.82 ", Configure.tool));
		// Switch reference version
		// sb.append(String.format("java -jar %s/snpEff/snpEff.jar GRCh37.75 ", Configure.tool));
		sb.append(FileHelp.getInputFile());
		sb.append(" -t -canon -onlyProtein ");
		
		try {
			CommandHelper.runAndSave(sb.toString(), FileHelp.getAnnotateOutput());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}

}
