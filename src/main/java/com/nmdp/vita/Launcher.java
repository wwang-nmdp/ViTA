package com.nmdp.vita;




import com.nmdp.vita.command.*;
import com.nmdp.vita.util.FileHelp;

import java.util.HashMap;

public class Launcher {
    private static final String INPUT = "-i";
    private static final String HLA ="-hla";
    private static final String TOOL_FOLDER = "-t";
    private static final String OUTPUT= "-o";
    private static final String HELP = "-h";
    private static final String WINDOW = "-w";
    private static final String CHROME = "-c";
    private static final String LOCATION = "-l";
    private static final String HLA_DIVIDER = ",";
    
    private static boolean showHelp;
    private static String helpInfo = "Note: TBD\n";
    
   
    private static HashMap<String, String> paramMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        
        try{
            getParameters(args);
        }catch (IndexOutOfBoundsException e){
            System.out.println("parameter is missing. program stopped");
            return;
        }
        //check if show help information or not
        if(showHelp){
        	System.out.println(helpInfo);
        	return;
        }
        
        
        if(paramMap.containsKey(INPUT)){
            Configure.input = paramMap.get(INPUT);
        }else {
            Configure.chrome = paramMap.get(CHROME);
            Configure.chrome = paramMap.get(LOCATION);
        }


        if(paramMap.containsKey(TOOL_FOLDER)){
        	 Configure.tool = paramMap.get(TOOL_FOLDER);
        }
        Configure.setOutputFolder(paramMap.get(OUTPUT));
        Configure.setHLA(paramMap.get(HLA).split(HLA_DIVIDER));

        FileHelp.makeFolders();

        new SnpEff().run();
        new Filter().run();
        new TranslateToProtein().run();
       if(Configure.windowSlide){
            new WindowSlider().run();
        }else{
            new Cleavage().run();
            new Chop().run();
        }
        new NetMHCpan().run();
        new launchAff().run();
       
    }



    private static void getParameters(String[] args) {
    	//get window slide option
    	for (int i = 0; i < args.length; i++) {
            if (args[i].equals(WINDOW)) {
                Configure.setWindowSlide();
                break;
            }
        }
    	//get help option
    	for (int i = 0; i < args.length; i++) {
            if (args[i].equals(HELP)) {
                showHelp = true;
                break;
            }
        }
        //get input d
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(INPUT)) {
                paramMap.put(INPUT, args[i+1]);
                break;
            }
        }
        
      //get hla
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(HLA)) {
                paramMap.put(HLA, args[i+1]);
                break;
            }
        }
        
      //get tool folder 
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(TOOL_FOLDER)) {
                paramMap.put(TOOL_FOLDER, args[i+1]);
                break;
            }
        }
        
      //get output folder
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(OUTPUT)) {
                paramMap.put(OUTPUT, args[i+1]);
                break;
            }
        }

        //get chrome
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(CHROME)) {
                paramMap.put(CHROME, args[i+1]);
                break;
            }
        }

        //get location of chrome
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(LOCATION)) {
                paramMap.put(LOCATION, args[i+1]);
                break;
            }
        }

        //get hla strings
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(HLA)) {
                paramMap.put(LOCATION, args[i+1]);
                break;
            }
        }
    }

}
