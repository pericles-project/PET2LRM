package eu.periclesproject.pet2lrm;

import java.util.List;

import model.Environment;
import model.Event;
import model.Part;
import model.Profile;
import LRMv2.LRM_static_schema;

import com.hp.hpl.jena.ontology.Individual;

/**
 * This mapper is responsible to map the parsed content into ontology snippets.
 */
public class MapperLRM {

	private final EnvironmentModel ontology;
	private final DaemonModuleMapper daemonModuleMapper;
	private final EnvironmentModuleMapper environmentModuleMapper;
	private final FileModuleMapper fileModuleMapper;

	public MapperLRM(EnvironmentModel ontology) {
		this.ontology = ontology;
		daemonModuleMapper = new DaemonModuleMapper(ontology);
		environmentModuleMapper = new EnvironmentModuleMapper(ontology);
		fileModuleMapper = new FileModuleMapper(ontology);
	}

	/**
	 * Once the PET information is parsed, this method will initiate its mapping
	 * into the ontology. The PET data structure classes are mapped as well as
	 * the extraction results.
	 * 
	 * @param information
	 */
	public void map(ParsedInformation information) {
		mapProfiles(information.parsedProfiles);
		mapEvents(information.parsedEvents);
	}

	/**
	 * Maps all parsed Profiles into the ontology.
	 * 
	 * @param parsedProfiles
	 */
	private void mapProfiles(List<ParsedProfile> parsedProfiles) {
		for (ParsedProfile profile : parsedProfiles) {
			mapProfile(profile);
		}
	}

	/**
	 * Maps one parsed Profile into the ontology.
	 * 
	 * @param parsedProfile
	 */
	public void mapProfile(ParsedProfile parsedProfile) {
		Individual profile = createProfileResource(parsedProfile.profile);
		if (parsedProfile.environment!=null) {

			profile.addProperty(ontology.hasPETVersion,
					parsedProfile.environment.petVersion);
			Individual environment = createEnvironmentResource(parsedProfile.environment);
			profile.addProperty(ontology.hasEnvironment, environment);
		}
		for (Part part : parsedProfile.parts) {
			Individual file = createPartResource(part);
			profile.addProperty(ontology.hasFile, file);
		}

	}

	/**
	 * Creates an ontology resource for an instance of the PET Profile Class.
	 * 
	 * @param profile
	 * @return
	 */
	private Individual createProfileResource(Profile profile) {
		Individual profileIndividual = ontology.abstractProfile
				.createIndividual(ontology.PET2LRM_NS + profile.getUUID());
		profileIndividual.addProperty(LRM_static_schema.realizes,
				ontology.abstractProfile);
		profileIndividual.addProperty(ontology.hasDisplayName,
				profile.getName());
		return profileIndividual;
	}

	/**
	 * Creates an ontology resource for an instance of the PET Environment
	 * Class. There is one Environment for each Profile.
	 * 
	 * @param environment
	 * @return
	 */
	private Individual createEnvironmentResource(Environment environment) {
		Individual environmentIndividual = ontology.abstractEnvironment
				.createIndividual(ontology.PET2LRM_NS + environment.profileUUID);
		environmentIndividual.addProperty(LRM_static_schema.realizes,
				ontology.abstractEnvironment);
		environmentModuleMapper.map(environmentIndividual, environment);
		return environmentIndividual;
	}

	/**
	 * Creates an ontology resource for an instance of the PET Part Class. There
	 * are several Parts for each Profile. A Part represents a file which is/was
	 * added to PET.
	 * 
	 * @param part
	 * @return the created resource
	 */
	private Individual createPartResource(Part part) {
		Individual file = ontology.abstractPart
				.createIndividual(ontology.PET2LRM_NS + part.profileUUID + "-"
						+ EnvironmentModuleMapper.norm(part.fileName));
		file.addProperty(LRM_static_schema.realizes, ontology.abstractPart);
		file.addProperty(ontology.hasFileName, part.fileName);
		if (part.getFile()!=null)
			file.addProperty(ontology.hasFilePath, part.getFile().toAbsolutePath().toString());
		fileModuleMapper.map(file, part);
		return file;
	}

	/**
	 * Maps the parsed Events into the ontology.
	 * 
	 * @param parsedEvents
	 */
	private void mapEvents(List<Event> parsedEvents) {
		for (Event event : parsedEvents) {
			createEventResource(event);
			// TODO: check if the event needs to be added to the ontology///
		}
	}

	/**
	 * Creates an ontology resource for an instance of the PET Environment
	 * Class.
	 * 
	 * @param parsedEvent
	 */
	private Individual createEventResource(Event parsedEvent) {
		Individual event = ontology.monitoringEvent
				.createIndividual(ontology.PET2LRM_NS + parsedEvent.timestamp
						+ "-" + EnvironmentModuleMapper.norm(parsedEvent.reporterName));
		event.addProperty(LRM_static_schema.realizes, ontology.monitoringEvent);
		daemonModuleMapper.map(event, parsedEvent);
		
		return event;
	}
}
