# ViTA
Targeted genome editing has enormous potential for treating monogenic disorders, especially for those that affect the hematopoietic system. Reducing the immunogenicity of gene-editing components such as CRISPR-Cas9 is critical for ensuring safe treatment. Gene corrections may also result in novel epitopes of human leukocyte antigen (HLA). Here, we present an application, ViTA, that predicts immunogenicity of HLA-epitopes. 
It supports common input formats, including single-variant chromosome coordinates or variant call format (VCF).
## Introduction
CRISPR/Cas9 and other gene-editing technologies have exciting therapeutic potential. Clinical applications include curing monogenetic disorders (29930285) or humanizing animal organs for xenotransplantation (30233645). Considerable effort and progress has been made to reduce the immunogenicity of microbial-derived core editing components (ref) and delivery vectors (Chew, W. L. et al., 2018). However, relatively little attention is given to the possibility that somatic gene edits may lead to immunoreactive antigens introduced ex-post facto (Figure 1A). The effects could be counterproductive if immunocompetent patients reject the therapy or life threatening in cases where human leukocyte antigen (HLA)-bound peptides elicit an alloreactive-like response (ref). Here, we present a strategy paired with an efficient application, variant-to-antigen (ViTA), to simulate gene-edited peptide presentation. ViTA may aid strategies for the development of safe and effective gene therapies.
## Algorithmic Flow Chart  

![AlgorithmicFlowChart](https://github.com/wwang-nmdp/ViTA/blob/master/doc/image/AlgorithmicFlowChart.png)

## Pre-installation

### Install `SnpEff` from http://snpeff.sourceforge.net/index.html  SnpEff is a variant annotation and effect prediction tool. It annotates and predicts the effects of variants on genes (such as amino acid changes).

```unix 
wget http://sourceforge.net/projects/snpeff/files/snpEff_latest_core.zip
unzip snpEff_latest_core.zip
java -jar snpEff.jar
```
#Run 'java -jar snpEff.jar command' for help on each specific command

#Usage: snpEff build [options] genome_version
java -jar snpEff.jar build GRCh38.82
### Install `netChop3.1` from Center For Biological Sequence Analysis, Technical University of Denmark.
   The NetChop 3.1 may be downloaded only by special agreement.  For academic users there is a download site at:http://www.cbs.dtu.dk/cgi-bin/nph-sw_request?netchop. Other users are requested to contact   software@cbs.dtu.dk.   

```unix 
cat netchop-3.1.<unix>.tar.Z | uncompress | tar xvf -
export NETCHOP=/full/path/to/netchop-3.1
export TMPDIR=/full/path/to/netchop-3.1/tmp
```

In the 'netchop-3.1' directory test the software:
bin/netchop test/test.fsa > test.out
  For research purpose, the net Chop3.1 is pre-built in MiHA identification pipeline. Make sure two environment variables, NETCHOPandTMDIR are set properly.Alsomakesurethat TMPDIR has the sticky bit set (the long listing should render 'drwxrwxrwt...'). If not, set it:

```unix 
chmod 1777 $TMPDIR
```
### Install `netMHCpan-3.0`
Like netChop, the netMHCpan-3.0 may only be downloaded only by special agreement as well. In addition, it requests to download the data file (data.tar.gz) separately.

```unix 
cat netMHCpan-3.0.<unix>.tar.gz | uncompress | tar xvf -

cd ./netMHCpan-3.0
wget http://www.cbs.dtu.dk/services/NetMHCpan-3.0/data.tar.gz
tar -xvf data.tar.gz
```




## Set environment variables

```unix 
export TMPDIR=/Path/to/Tools/netMHCpan-3.0/tmp
export NMHOME=/Path/to/Tools/netMHCpan-3.0/bin
export NETMHCpan=/Path/to/Tools/netMHCpan-3.0
```


## Build database of Transcriptome 


cds.db is pre-built hg38 transcriptome and minor allele frequence (version:snp147common) database, please download by using the provided link: https://sourceforge.net/projects/mihaip/files/cds.db.zip/download

unzip the file:
```unix
unzip cds.db.zip
```
Copy the database into your working directory.

# Run ViTA

```unix
java -jar vita.jar -help
#check options of the application and test with sample data
```