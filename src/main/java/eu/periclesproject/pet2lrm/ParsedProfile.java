package eu.periclesproject.pet2lrm;

import java.util.List;

import model.Environment;
import model.Part;
import model.Profile;

/**
 * Data structure to store a parsed profile.
 * 
 * Note: This class is used instead of the original Profile class (which also
 * consists of Parts and an Environment) to circumvent that this Profile tries
 * to inform other PET components about its state changes.
 */
public class ParsedProfile {
	public Profile profile;
	public Environment environment;
	public List<Part> parts;
}
