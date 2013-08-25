polyA
=====

polyA seq tool called polyAextract


To make this program work, you need to create a folder that contains the following files:
 
1. A reference genome file, in the format .fa
2. A copy of the reference genome with the following format .fai
3. A file in the format .bed that conatins information about Poly-A sites.
 
This program takes in 5 inputs.

1. The first parameter is the location of the reference Genome on the hard drive; which should be specfied as a string.
    
   Example:
   
 		"/home/reynaldo/Documents/hg19_ref_genome_nonrandom_sorted.fa"
 
2. The second parameter is the location of the Poly-A sites file on the hard drive; which should be specified as a string.
 	  
    Example:
    
    		"/home/reynaldo/Documents/GSM747470_human_brain.sites.clustered.hg19.bed"

3. The third parameter is an integer that will represent how much bases you want to extract.

4. The forth parameter is the lenth of the polyA tail you would like to append.
   Note: If you don't want a polyA tail, you can make this parameter 0.

5.The fifth parameter is the name and location of the output file; which should be sprecified as a string.
  
  Example:
  
   "/home/reynaldo/Desktop/outputFileName"


FilterTool
=====
