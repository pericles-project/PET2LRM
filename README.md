# PET2LRM

This is an extension to the [PERICLES Extraction tool](https://github.com/pericles-project/pet). It converts the JSON output from PET into LRM (Linked Resource Model) ontology snippets. The LRM is an abstract ontology developed by the PERICLES project. It is meant as an intermediate tool for integrating PET into a scenario where an partial automated updated of OWL ontologies is necessary (e.g. in connection with the [PERSIsT-API](https://github.com/pericles-project/PERSIsT-API) and the entity registry [ERMR](https://github.com/pericles-project/ERMR). 

Note: this is a prototype tool and does not map all output modules from PET into LRM snippets. 

## Usage 
Make sure that you have installed a recent Java runtime. 
Download the file PET2LRM.jar from the releases tab. Open a command line, navigate to the folder where you have stored PET2LRM.jar:
`java -jar PET2LRM.jar -d /path/to/PET/Output/Directory -o lrmsnippets.owl
`
where /path/to/PET/Output/Directory is the path to the PET output directory  and -o specifies the path to the OWL snippets output file.

```
Usage: PET to LRM Export tool [options]
  Options:
  * -d, --data
       Location of the main PET folder
    -h, --help
       print this message
       Default: false
    -o, --out
       output file. Default:out.owl
       Default: FileStorageInterface
    -s, --storage
       Storage system for the extraction results. Default:fileInterface
       Default: FileStorageInterface
```       

## Build
There is a Maven file for building this tool. Note that it is dependant onthe PET Tool which is available [here](https://github.com/pericles-project/pet). 

# Credits

 _This project has received funding from the European Union’s Seventh Framework Programme for research, technological development and demonstration under grant agreement no FP7- 601138 PERICLES._   
 
 <a href="http://ec.europa.eu/research/fp7"><img src="https://github.com/pericles-project/pet/blob/master/wiki-images/LogoEU.png" width="110"/></a>
 <a href="http://www.pericles-project.eu/"> <img src="https://github.com/pericles-project/pet/blob/master/wiki-images/PERICLES%20logo_black.jpg" width="200" align="right"/> </a>

<a href="http://www.liv.ac.uk/"> <img src="https://github.com/pericles-project/pet/blob/master/wiki-images/liverpool_logo.png" width="300"/></a>

<a href="http://www.sub.uni-goettingen.de/"><img src="https://github.com/pericles-project/pet/blob/master/wiki-images/sub-logo.jpg" width="300"/></a> 
