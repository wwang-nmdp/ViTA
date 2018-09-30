package com.nmdp.vita.util;

import com.nmdp.vita.Configure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by wwang on 10/19/16.
 */
public class FileHelp {
    public static String getOutput() {
        return getRoot() + "/output/";
    }

    public static String getRoot() {
     return Configure.getOutputFolder();
    }

    public static String getMetaFilePath()  {
        return getOutput() + "meta/";
    }

    public static String getChopFilePath() {
        return getRoot() + "Chopped/";
    }

    public static String getDNAfilePath() {
        return getOutput() + "DNA/";
    }

    public static String getProteinPath() {
        return getOutput() + "protein/";
    }
    public static String getYproteinPath() {
        return getOutput() + "Yprotein/";
    }
    
    public static String getYproteinFile(){
    	return getYproteinPath() + Configure.getWorkID()+"yProtein.txt";
    }
    
    public static void makeFolders(){
    	makeFolder(getOutput());
    	makeFolder(getMetaFilePath());
    	makeFolder(getDNAfilePath());
    	makeFolder(getProteinPath());
    	makeFolder(getOutput() + "/predicted");
    	makeFolder(getOutput() + "/annotated");
    	makeFolder(getOutput() + "/compared");
    	makeFolder(getOutput() + "/missense");
    	makeFolder(getOutput() + "/cleavaged");
    	makeFolder(getOutput() + "/Ycleavaged");
    	makeFolder(getOutput() + "/chopMeta");
    	makeFolder(getOutput() + "/affinity");
    	makeFolder(getOutput() + "/chopped");
    }

    private static void makeFolder(String name){
    	File folder = new File(name);
    	folder.mkdirs();
    }

    public static String getInputFile() {
        if(Configure.input != null){
            return Configure.input;
        }else {
            String input = "input.txt";
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new File(input));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            pw.println("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
            pw.println(Configure.chrome + " "+ Configure.location + "\t\tA\tC");
            pw.close();
            return input;
        }
    }
    

    
    public static String getAnnotateOutput(){
    	return getOutput() + "/annotated/" + "_ann.vcf";
    }
    
    public static String getMissense(){
    	return getOutput() + "/missense/" + Configure.getWorkID() + "_nn_msv.vcf";
    }


    
    public static String getProteinSlice(){
    	return FileHelp.getProteinPath() + Configure.getWorkID() + "_protein_slice.pep";
    }
    public static String getProteinSliceMeta(){
    	return FileHelp.getProteinPath() + Configure.getWorkID() + "_protein_slice_meta.fasta";
    }


    
    public static String getMetaData(){
    	return FileHelp.getMetaFilePath() + Configure.getWorkID() + "_meta.txt";
    }
    
    public static String getCleavageFile(){
    	return getOutput() + "cleavaged/" + Configure.getWorkID() + "_cleavaged.txt";
    }
    
    public static String getChopFile(){
    	return getOutput() + "chopped/" + Configure.getWorkID() + "_chopped.pep";
    }
    
    public static String getChopMetaData(){
    	return getOutput() + "chopMeta/" + Configure.getWorkID() + ".txt";
    }
    
    public static String getAffFile(String hla){
    	return getOutput() + "affinity/" + Configure.getWorkID() + "_"+ hla +".txt";
    }


	public static String getPredictFile(String hla) {
		return getOutput() + "predicted/"+ Configure.getWorkID() + "_"+ hla +".txt";
	}

	public static String getProteinFile(){
        return getProteinPath() + Configure.getWorkID() + "_protein.txt";
    }
}
