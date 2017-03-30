package eu.periclesproject.pet2lrm;

import static configuration.Log.EXCEPTION_LOGGER;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;

import storage.GeneralStorage;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import controller.StorageController;

/**
 * The main class handles the user input and starts the PET output parser and
 * the LRM mapper.
 */
public class Main {
	public CliParameters options;
	public JCommander jc;

	/**
	 * User commands to be specified at command line at start.
	 */
	public static class CliParameters {
		@Parameter(names = { "-h", "--help" }, description = "print this message", help = true)
		public boolean help;
		@Parameter(names = { "-s", "--storage" }, description = "Storage system for the extraction results. Default:fileInterface ")
		public String out = "FileStorageInterface";
		@Parameter(names = { "-o", "--out" }, description = "output file. Default:out.owl ")
		public String storage = "FileStorageInterface";
		@Parameter(names = { "-d", "--data" }, description = "Location of the main PET folder ", required = true)
		public String destination = null;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Main(args);
	}

	/**
	 * Constructor gets the unmodified user args from the tool start command
	 * line.
	 * 
	 * 1. The PET output is parsed
	 * 
	 * 2. An ontology snippet is created
	 * 
	 * 3. The parsed information is mapped into the ontology snippet
	 * 
	 * 4. The ontology snippet is saved to a file and/or printed to std. out.
	 * 
	 * @param args
	 */
	public Main(String args[]) {
		handleUserInput(args);
		ParserPET parser = new ParserPET();
		ParsedInformation information = parser.parse(options.storage,
				options.destination);
		EnvironmentModel ontology = new EnvironmentModel();
		MapperLRM lrmMapper = new MapperLRM(ontology);
		lrmMapper.map(information);
		//ontology.model.write(System.out, "RDF/XML-ABBREV");

		File outputFile = new File(new File(options.destination),"sei_model.owl");
		try {
			ontology.model.write(new FileOutputStream(outputFile), "RDF/XML");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * User input voodoo.
	 * 
	 * @param args
	 */
	private void handleUserInput(String[] args) {
		options = new CliParameters();
		jc = new JCommander(options);
		jc.setProgramName("PET to LRM Export tool");
		try {
			jc.parse(args);
		} catch (ParameterException e) {
			jc.usage();
			System.out.print("\nStorage interfaces: \n");
			for (Class<? extends GeneralStorage> c : StorageController.storageSystems) {
				System.out.print(" " + c.getSimpleName() + "\n\n"); // +"\t\t[");
			}
			System.out.println("Wrong arguments, can not start. Try --help");
			EXCEPTION_LOGGER
					.log(Level.SEVERE, "Wrong command line argument", e);
			System.exit(-1);
		}
	}

	
}
