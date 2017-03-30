package eu.periclesproject.pet2lrm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Environment;
import model.Event;
import model.ExtractionResultCollection;
import model.Part;
import model.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import controller.ProfileController;
import controller.StorageController;

/**
 * This class parses the PET output.
 */
public class ParserPET {
	private StorageController storageController;
	private ProfileController profileController;
	public ParsedInformation parsedInformation = new ParsedInformation();

	public ParsedInformation parse(String storage, String destination) {
		storageController = new StorageController(storage);
		profileController = new ProfileController(destination);
		parsedInformation.parsedProfiles = parseProfiles();
		parsedInformation.parsedEvents = parseEvents();
		return parsedInformation;
	}

	/**
	 * The results are organised by profiles. Each Profile has a set of Part
	 * (file) information and environment information.
	 * 
	 * @return parsed profiles
	 */
	private List<ParsedProfile> parseProfiles() {
		List<ParsedProfile> profiles = new ArrayList<ParsedProfile>();
		for (Profile profile : profileController.getProfiles()) {
			ParsedProfile parsedProfile = new ParsedProfile();
			parsedProfile.profile = profile;
			parsedProfile.environment = parseProfileEnvironment(profile);
			parsedProfile.parts = parseProfileParts(profile);
			profiles.add(parsedProfile);
		}
		return profiles;
	}

	/**
	 * Parses an environment which belongs to a Profile. The environment is a
	 * collection of results from the different modules at a specific time
	 * 
	 * @param profile
	 */
	private Environment parseProfileEnvironment(Profile profile) {
		@SuppressWarnings("static-access")
		ExtractionResultCollection[] env = storageController.storage
				.getResults(profile.getEnvironment());
		for (ExtractionResultCollection erc : env) {
			Environment environment = (Environment) erc;
			return environment;
		}
		return null;
	}

	/**
	 * Parses the parts belonging to a Profile. Each of the parts will have
	 * multiple 'parts' with module results.
	 * 
	 * @param profile
	 */
	private List<Part> parseProfileParts(Profile profile) {
		List<Part> parsedParts = new ArrayList<Part>();
		for (Part part : profile.getParts()) {
			@SuppressWarnings("static-access")
			ExtractionResultCollection[] parts = storageController.storage
					.getResults(part);
			for (ExtractionResultCollection erc : parts) {
				Part parsedPart = (Part) erc;
				parsedParts.add(parsedPart);
			}
		}
		return parsedParts;
	}

	/**
	 * Events are stored independently of the Profiles.
	 * 
	 * @return parsed events
	 */
	private List<Event> parseEvents() {
		@SuppressWarnings("static-access")
		List<String> eventRaw = storageController.storage.readEventData();
		ObjectMapper mapper = initMapper();
		List<Event> events = new ArrayList<Event>();
		if (eventRaw != null) {
			for (String s : eventRaw) {
				try {
					Event event = mapper.readValue(s, Event.class);
					events.add(event);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return events;
	}

	private static ObjectMapper initMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.enable(SerializationFeature.CLOSE_CLOSEABLE);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper;
	}
}
