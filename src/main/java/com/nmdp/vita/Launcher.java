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
    private static final String POS = "-pos";
    private static final String HLA_DIVIDER = ",";
    private static final String SHOW_VERSION = "-v";
    private static final String VERSION = "1.0";

    private static boolean showHelp;
    private static boolean showVersion;
    private static String helpInfo = "Program: ViTA (variant to antigen)\n" +
            "is a Java-based workflow for predicting the immunogenicity of HLA-epitopes\n" +
            "by simulating gene-edited peptide(s) and their binding to patient HLA(s)\n" +
            "Before running this program, make sure you have CDS.db downloaded on your working directory\n" +
            "Usage: java -jar vita.jar <command> [option]\n" +
            "A typical invovation would be:" +
            "   java -jar vita.jar -i /path/to/myVariants.vcf -hla A02:01,B07:02,C04:02 -w -t /path/to/workingDirectory/Tools -o /path/to/output\n" +
            "Commands:\n" +
            "   -i,     Multiple or single variant input file\n" +
            "           input file should be a tab-delimited format, regardless of the file extensions.\n\n" +
            "   -pos,   Position of variants at chromosome coordinate\n" +
            "           simply input the chromosome coordinate. e.g. -pos chr11:5227002\n" +
            "           Be cautious, the position format have to be 1-based\n" +
            "           Please follow the instruction of UCSC genome browser for details of coordinate system: \n" +
            "               http://genome.ucsc.edu/blog/the-ucsc-genome-browser-coordinate-counting-systems/\n" +
            "           It can only take either -i or -pos as input for each process\n\n" +
            "   -hla,   List of HLA alleles you wish to access immunogenicities\n" +
            "           HLA allele should be 4-digit resolution with one upper-case allele type followed by 4 numbers.\n" +
            "               e.g. A01:01\n" +
            "           Multiple allele input are allowed by comma-delimited format\n" +
            "               e.g. A0101,A,2001,A0301,B0702\n" +
            "           Depends on the number of CPUs and disk writing speed, multiple allele input could significantly reduce the predictions\n" +
            "   -t,     Tell the program where are the dependency tools\n" +
            "           Set the path to netChop and netMHCpan\n" +
            "           You might also need to set the PATH for the dependencies:\n" +
            "               export TMPDIR=/path/to/Tools/netMHCpan-3.0/tmp\n" +
            "               export TMPDIR=/path/to/Tools/netchop-3.1/tmp\n" +
            "               export NETCHOP=/path/to/Tools/netchop-3.1/\n" +
            "               export NETMHCpan=/path/to/Tools/netMHCpan-3.0\n\n" +
            "   -w,    \n" +
            "           To activate the slidingWindow function for chopping out all possible variant-containing 8- to 11- mer\n" +
            "           Otherwise the program goes netChop as default\n" +
            "   -o,     Set a directory to store the output\n" +
            "              e.g. /path/to/test/output\n" +
            "           You don't have to make an exact directory for the program unless you need the output be somewhere specifically\n" +
            "           Otherwise it will automatically generate one for you\n\n" +
            "Miscellaneous:\n" +
            "   -v,     Print current version information and exit\n" +
            "   -h,     Print this help and exit\n\n" +
            "Please check out latest update from our Github repo:\n" +
            "https://github.com/wwang-nmdp/ViTA\n\n" +
            "PROBLEMS:\n" +
            "\n" +
            "Leave the comments or report the issues via Github.\n" +
            "   https://github.com/wwang-nmdp/ViTA/issues\n" +
            "Contact wwang@nmdp.org in case of problems.\n" +
            "\n";
    
   
    private static HashMap<String, String> paramMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        
        try{
            getParameters(args);
        }catch (IndexOutOfBoundsException e){
            System.out.println("Parameter is missing. program stopped");
            return;
        }
        //check if show help information or not
        if(showHelp){
        	System.out.println(helpInfo);
        	return;
        }

        if(showVersion){
            System.out.println("The version is "+ VERSION);
            return;
        }
        
        try{
            if(paramMap.containsKey(INPUT)){
                Configure.input = paramMap.get(INPUT);
            }else if(paramMap.containsKey(POS)){
                String pos = paramMap.get(POS);
                String[] data = pos.split(":");
                checkIllegalState(data.length != 2, "The format of position is not right");
                checkIllegalState(data[0].startsWith("chr"), "The chrome should start with chr");
                String chromeNumber = data[0].substring(3);
                //Throw number format exception if it's not a number
                Integer.parseInt(chromeNumber);
                Configure.chrome = paramMap.get(chromeNumber);
                Configure.location = paramMap.get(data[1]);
            }


            if(paramMap.containsKey(TOOL_FOLDER)){
                Configure.tool = paramMap.get(TOOL_FOLDER);
            }
            checkIllegalState(paramMap.containsKey(OUTPUT), "The output folder is not set");
            Configure.setOutputFolder(paramMap.get(OUTPUT));
            checkIllegalState(paramMap.containsKey(HLA), "The HLA is not set");
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
        }catch (IllegalStateException exp){
            System.out.println(exp.getMessage());
        }

       
    }


    private static void checkIllegalState(boolean goodState, String message){
        if(!goodState){
            throw new IllegalStateException(message);
        }
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

        //get version option
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(SHOW_VERSION)) {
                showVersion = true;
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

        //get pos of chrome
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(POS)) {
                paramMap.put(POS, args[i+1]);
                break;
            }
        }

        //get hla strings
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(HLA)) {
                paramMap.put(HLA, args[i+1]);
                break;
            }
        }
    }

}
