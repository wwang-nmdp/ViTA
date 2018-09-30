package com.nmdp.vita;


import com.nmdp.vita.database.DatabaseUtil;
import com.nmdp.vita.translate.Translator;
import com.nmdp.vita.util.FileHelp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VCFToProtein {
    private Translator translator = new Translator();
    private PrintWriter metaDataWriter;
    private PrintWriter protienWriter;

	public void run(List<Transcript> geneList,  String fileName) {
        try {
            setPrinters();
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        for (Transcript gene : geneList) {
            processGene(gene);
        }
        cleanup();
    }

	private void setPrinters() throws FileNotFoundException, URISyntaxException {
        File metaFile = new File(FileHelp.getMetaData());
        metaDataWriter = new PrintWriter(metaFile);
        
        File proteinFile = new File(FileHelp.getProteinPath());
        protienWriter = new PrintWriter(proteinFile);

    }
	 private void cleanup() {
	        metaDataWriter.close();
	        protienWriter.close();
	        
	    }
	 
	 private void processGene(Transcript gene) {
	    	if(gene == null){
	    		System.out.println("gene is null");
	    		return;
	    	}
	        String dna = DatabaseUtil.getSequence(gene.transcriptID);
	        Iterator<Integer> it = gene.change.keySet().iterator();
	        StringBuilder sbRef = new StringBuilder(dna);
	        StringBuilder sbAlt = new StringBuilder(dna);
	        StringBuilder changePos = new StringBuilder();
	        List<Integer> aachangePos = new ArrayList<>();
	        List<Integer> DNAchangePos = new ArrayList<>();
	        changePos.append(" ,");

	        while (it.hasNext()) {
	            int i = it.next();
	            if(i < dna.length()){
	                changePos.append(i);
	                DNAchangePos.add(i);
	                changePos.append(",");
	                aachangePos.add((i-1)/3);
	            }

	        }

	        StringBuilder fastaHeader = new StringBuilder();
	        fastaHeader.append(gene.getInfo());
	        fastaHeader.append("|" + gene.transcriptID + "|");


	        for(int i = 0; i< aachangePos.size(); i++){
				int start = getStartIndex(aachangePos.get(i));
				int end = getEndIndex(dna.length()/3, aachangePos.get(i));

				sbAlt.replace(i-1, i, "a");
				String altProtein = translator.translate(sbAlt.toString(), 0, aachangePos.get(i));

	            protienWriter.print(fastaHeader);
	            protienWriter.println(aachangePos.get(i)+1);
	            protienWriter.println(altProtein.substring(start, end + 1));

				sbAlt.replace(i-1, i, "t");
				altProtein = translator.translate(sbAlt.toString(), 0, aachangePos.get(i));

				protienWriter.print(fastaHeader);
				protienWriter.println(aachangePos.get(i)+1);
				protienWriter.println(altProtein.substring(start, end + 1));

				sbAlt.replace(i-1, i, "c");
				altProtein = translator.translate(sbAlt.toString(), 0, aachangePos.get(i));

				protienWriter.print(fastaHeader);
				protienWriter.println(aachangePos.get(i)+1);
				protienWriter.println(altProtein.substring(start, end + 1));

				sbAlt.replace(i-1, i, "g");
				altProtein = translator.translate(sbAlt.toString(), 0, aachangePos.get(i));

				protienWriter.print(fastaHeader);
				protienWriter.println(aachangePos.get(i)+1);
				protienWriter.println(altProtein.substring(start, end + 1));

	            //print the meta data
	            metaDataWriter.print(gene.getChrome());
	            metaDataWriter.print(",");
	            metaDataWriter.print(gene.pos);
	            metaDataWriter.print(",");
	            metaDataWriter.print(sbRef.toString().charAt(DNAchangePos.get(i)-1));
	            metaDataWriter.print(",");
	            metaDataWriter.print(sbAlt.toString().charAt(DNAchangePos.get(i)-1));
	            metaDataWriter.print(",");
	            metaDataWriter.print(DNAchangePos.get(i));
	            metaDataWriter.print(",");
	            metaDataWriter.print(gene.transcriptID);
	            metaDataWriter.println(",");

	            if(end - start == 20){
	                metaDataWriter.println(10);
	            }else {
	                if(start == 0){
	                    metaDataWriter.println(aachangePos.get(i));
	                }else {
	                    metaDataWriter.println(aachangePos.get(i)-start);
	                }
	            }

	        }

	    }
	 
	 
	    private int getEndIndex(int proteinLength, int i) {
	        int end = i + 10;
	        if(end >= proteinLength){
	            return proteinLength - 1;
	        }else {
	            return end;
	        }
	    }

	    private int getStartIndex(int i) {
	        int start = i -10;
	        if(start < 0){
	            return 0;
	        }else {
	            return start;
	        }
	    }


	    private String getProtienChange(List<Integer> change){
	        StringBuilder changePos = new StringBuilder();
	        for(int i : change){
	            changePos.append(i+1);
	            changePos.append(",");
	        }
	        if(changePos.length() >1){
	            changePos.deleteCharAt(changePos.length()-1);
	        }

	        return changePos.toString();
	    }
}
