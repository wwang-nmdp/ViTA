package com.nmdp.vita.command;

import com.nmdp.vita.util.FileHelp;
import com.nmdp.vita.command.Cleavage;

import java.io.File;
import java.io.FileNotFoundException;

public class Chop {
	public void run(){
		  Clevage cl = new Clevage();
	        try {
	            cl.run(new File(FileHelp.getMetaData()), new File(FileHelp.getCleavageFile()));
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
		
	}
}
