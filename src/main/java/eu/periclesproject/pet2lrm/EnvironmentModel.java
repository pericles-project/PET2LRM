package eu.periclesproject.pet2lrm;

import LRMv2.LRM_dynamic_schema;
import LRMv2.LRM_static_schema;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * This class provides everything necessary to create the LRM based ontology
 * snippets for PET output including specific resources and properties.
 */
public class EnvironmentModel {
	public static final String PET2LRM_NS = "http://www.pericles-project.eu/ns/PET2LRM#";
	public final OntModel model = ModelFactory
			.createOntologyModel(OntModelSpec.OWL_MEM);

	/**
	 * Abstract resources and properties are used by the {@link MapperLRM} to
	 * create concrete entities.
	 */
	public OntClass petClass;
	public OntClass abstractProfile;
	public OntClass abstractPart;
	public OntClass abstractEnvironment;
	public OntClass abstractEvent;
	public OntClass monitoringEvent;
	public OntClass abstractModule;
	public OntClass abstractExtractionResult;

	public OntProperty hasPETVersion;

	public OntProperty hasEnvironment;
	public OntProperty hasFile;

	public OntProperty hasFileName;
	public OntProperty hasFilePath;

	public OntProperty hasGeneralInformation;

	public OntProperty hasClassRepresentation;
	public OntProperty hasDisplayName;
	public OntProperty hasVersion;
	public OntProperty hasConfiguration;
	public OntProperty hasExtractionDate;

	public OntProperty isExtractedBy;

	public EnvironmentModel() {
		setNamespace();
		setImports();
		createAbstractEntities();
		createProperties();
	}

	private void setNamespace() {
		PrefixMapping prefixes = new PrefixMappingImpl();
		prefixes.setNsPrefix("lrm", LRM_static_schema.NS);
		prefixes.setNsPrefix("time", "http://xrce.xerox.com/TIME#");
		prefixes.setNsPrefix("pet2lrm", PET2LRM_NS);
		model.setNsPrefixes(prefixes);
	}

	private void setImports() {
		Ontology ontology = model.createOntology(PET2LRM_NS);
		ontology.addImport(model
				.createResource("http://xrce.xerox.com/LRM#LRM-dynamic"));
		ontology.addImport(model
				.createResource("http://xrce.xerox.com/LRM#LRM-static"));
		ontology.addImport(model
				.createResource("http://xrce.xerox.com/TIME#TIME"));
	}

	private void createAbstractEntities() {
		createPETClass();
		createAbstractProfile();
		createAbstractPart();
		createAbstractEnvironment();
		createAbstractEvent();
		createMonitoringEvent();
		
		createAbstractModule();
		createAbstractExtractionResult();
	}

	private void createMonitoringEvent() {
		monitoringEvent = model.createClass(PET2LRM_NS + "MonitoringEvent");
		monitoringEvent.addProperty(RDFS.subClassOf, abstractEvent);
		monitoringEvent
				.addProperty(
						RDFS.comment,
						model.createLiteral(
								"This is an abstract class for an environment monitoring event captured by PET.",
								"en"));
	}

	private void createProperties() {
		createIsExtractedBy();
	}

	private void createPETClass() {
		petClass = model.createClass(PET2LRM_NS + "PETClass");
		petClass.addProperty(RDFS.subClassOf,
				LRM_static_schema.AggregatedResource);
		petClass.addProperty(RDFS.subClassOf,
				LRM_static_schema.AbstractResource);
		petClass.addProperty(
				RDFS.comment,
				model.createLiteral(
						"An abstract base class for resources that describe PET classes.",
						"en"));
	}

	private void createAbstractProfile() {
		abstractProfile = model.createClass(PET2LRM_NS + "AbstractProfile");
		abstractProfile.addSuperClass(petClass);
		abstractProfile
				.addProperty(
						RDFS.comment,
						model.createLiteral(
								"A profile is a data structure that combines an environment and a set of parts.",
								"en"));
		abstractProfile.addSuperClass(LRM_static_schema.AggregatedResource);

		hasPETVersion = model.createOntProperty(PET2LRM_NS + "hasPETVersion");
		hasPETVersion.addProperty(RDFS.comment,
				model.createLiteral("The used version of PET.", "en"));
		hasPETVersion.addDomain(abstractProfile);
	}

	private void createAbstractPart() {
		abstractPart = model.createClass(PET2LRM_NS + "AbstractPart");
		abstractPart.addSubClass(petClass);
		abstractPart
				.addProperty(
						RDFS.comment,
						model.createLiteral(
								"A part is a data structure for an important file and file dependent information.",
								"en"));
		hasFile = model.createOntProperty(PET2LRM_NS + "hasFile");
		hasFile.addProperty(RDFS.comment, model.createLiteral(
				"Parts are file representations belonging to a profile.", "en"));
		hasFile.addSuperProperty(LRM_static_schema.hasPart);
		hasFile.addDomain(abstractProfile);
		hasFile.addRange(abstractPart);

		hasFileName = model.createOntProperty(PET2LRM_NS + "hasFileName");
		hasFileName.addProperty(RDFS.comment,
				model.createLiteral("A file has a file name.", "en"));
		hasFileName.addDomain(abstractPart);

		hasFilePath = model.createOntProperty(PET2LRM_NS + "hasFilePath");
		hasFilePath.addProperty(RDFS.comment,
				model.createLiteral("A file has a file path.", "en"));
	}

	private void createAbstractEnvironment() {
		abstractEnvironment = model.createClass(PET2LRM_NS
				+ "AbstractEnvironment");
		abstractEnvironment.addSubClass(petClass);
		abstractEnvironment
				.addProperty(
						RDFS.comment,
						model.createLiteral(
								"An environment is a data structure that saves file independent information.",
								"en"));

		hasEnvironment = model.createOntProperty(PET2LRM_NS + "hasEnvironment");
		hasEnvironment.addProperty(RDFS.comment, model.createLiteral(
				"Each profile has an associated environment.", "en"));
		hasEnvironment.addSuperProperty(LRM_static_schema.hasPart);
		hasEnvironment.addDomain(abstractProfile);
		hasEnvironment.addRange(abstractEnvironment);
	}

	private void createAbstractEvent() {
		abstractEvent = model.createClass(PET2LRM_NS + "AbstractEvent");
		//abstractEvent.addSubClass(petClass);
		abstractEvent.addProperty(RDFS.subClassOf, LRM_dynamic_schema.Event);
		abstractEvent
				.addProperty(
						RDFS.comment,
						model.createLiteral(
								"This is an abstract class for an environment event captured by PET.",
								"en"));
	}

	private void createAbstractModule() {
		abstractModule = model.createClass(PET2LRM_NS + "AbstractModule");
		abstractModule.addSuperClass(petClass);
		abstractModule
				.addProperty(
						RDFS.comment,
						model.createLiteral(
								"This is an abstract class for an extraction module data structure of PET.",

								"en"));
		hasClassRepresentation = model.createOntProperty(PET2LRM_NS
				+ "hasClassRepresentation");
		hasClassRepresentation
				.addProperty(
						RDFS.comment,
						model.createLiteral(
								"The java class representation of the extraction module.",
								"en"));
		hasDisplayName = model.createOntProperty(PET2LRM_NS + "hasDisplayName");
		hasDisplayName
				.addProperty(
						RDFS.comment,
						model.createLiteral(
								"The name of the configured extraction module given by the user.",
								"en"));
		hasVersion = model.createOntProperty(PET2LRM_NS + "hasVersion");
		hasVersion.addProperty(RDFS.comment, model.createLiteral(
				"The version of the extraction module.", "en"));
		hasConfiguration = model.createOntProperty(PET2LRM_NS
				+ "hasConfiguration");
		hasConfiguration.addProperty(RDFS.comment, model.createLiteral(
				"The configuration of an extraction module.", "en"));

		hasExtractionDate = model.createOntProperty(PET2LRM_NS
				+ "hasExtractionDate");
		hasExtractionDate.addProperty(RDFS.comment, model.createLiteral(
				"The extraction date of an extraction result.", "en"));
	}

	private void createAbstractExtractionResult() {
		abstractExtractionResult = model.createClass(PET2LRM_NS
				+ "AbstractExtractionResult");
		abstractExtractionResult.addSuperClass(petClass);
		abstractExtractionResult
				.addProperty(
						RDFS.comment,
						model.createLiteral(
								"This is an abstract class for information extracted by a PET extraction module.",
								"en"));

		hasGeneralInformation = model.createOntProperty(PET2LRM_NS
				+ "hasGeneralInformation");
		hasGeneralInformation.addProperty(RDFS.comment, model.createLiteral(
				"The general information of an extraction result.", "en"));
		hasGeneralInformation.addDomain(abstractExtractionResult);
	}

	private void createIsExtractedBy() {
		isExtractedBy = model.createOntProperty(PET2LRM_NS + "isExtractedBy");
		isExtractedBy
				.addProperty(
						RDFS.comment,
						model.createLiteral(
								"An extracted information result is extracted by an extraction module.",
								"en"));
	}
}
