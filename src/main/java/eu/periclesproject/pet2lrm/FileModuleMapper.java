package eu.periclesproject.pet2lrm;

import model.ExtractionResult;
import model.KeyValueResult;
import model.Part;
import modules.AbstractModule;
import modules.ApacheTikaExtractionModule;
import modules.ChecksumFileModule;
import modules.FileIdentificationCommandModule;
import modules.FileStoreModule;
import modules.GeneralNativeCommandModule;
import modules.MediaInfoCommandModule;
import modules.OfficeDDTDependencyExtractorModule;
import modules.PDFFontDependencyExtractorModule;
import modules.PosixModule;
import modules.RegexModule;
import modules.SigarFileInfo;
import modules.SpotlightMetadataModule;
import modules.configuration.ChecksumFileModuleConfig;
import LRMv2.LRM_static_schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

/**
 * This class detects, if the extraction results originate from a known PET file
 * information ExtractionModule. If so, it is possible to add further important
 * information to the ontology.
 */
public class FileModuleMapper {
	private final EnvironmentModel ontology;

	public FileModuleMapper(EnvironmentModel ontology) {
		this.ontology = ontology;
	}

	public void map(Resource resource, Part part) {
		for (ExtractionResult result : part.extractionResults) {
			map(resource, result, part);
		}
	}
	

	public void map(Resource resource, ExtractionResult result, Part part) {
		try {
			@SuppressWarnings("rawtypes")
			Class moduleClass = Class.forName(result.moduleClass);
			AbstractModule module = (AbstractModule) moduleClass.newInstance();
			Resource resultResource = mapGeneralResultInformation(resource,
					result);
			if (result.results instanceof KeyValueResult) {

				KeyValueResult keyValueResult = (KeyValueResult) result.results;

				for (Entry<String,String>r: keyValueResult.results.entrySet()) {

					if (r.getValue()==null)
						continue;

					OntProperty p = createProperty(
							ontology.abstractExtractionResult, r.getKey().toLowerCase());
					// 3. Map the results to general result:
					resultResource.addProperty(p,
							r.getValue());

				} }
			else {System.out.println("Module " + module.moduleName + " not supported");

			}

			//			} else if (module instanceof ApacheTikaExtractionModule) {
			//			}

			//			} else if (module instanceof FileIdentificationCommandModule) {
			//				System.out
			//				.println("FileIdentificationCommandModule module detected");
			//				mapFileIdentificationCommandModule(resultResource,
			//						(FileIdentificationCommandModule) module);
			//			} else
			//
			//				if (module instanceof FileStoreModule) {
			//					System.out.println("FileStoreModule module detected");
			//					mapFileStoreModule(resultResource, (FileStoreModule) module);
			//				} else if (module instanceof MediaInfoCommandModule) {
			//					System.out.println("MediaInfoCommandModule module detected");
			//					mapMediaInfoCommandModule(resultResource,
			//							(MediaInfoCommandModule) module);
			//				} else
			//
			//					if (module instanceof OfficeDDTDependencyExtractorModule) {
			//						System.out
			//						.println("OfficeDDTDependencyExtractorModule module detected");
			//						mapOfficeDDTDependencyExtractorModule(resultResource,
			//								(OfficeDDTDependencyExtractorModule) module);
			//					} else if (module instanceof PDFFontDependencyExtractorModule) {
			//						System.out
			//						.println("PDFFontDependencyExtractorModule module detected");
			//						mapPDFFontDependencyExtractorModule(resultResource,
			//								(PDFFontDependencyExtractorModule) module);
			//					} else if (module instanceof PosixModule) {
			//						System.out.println("PosixModule module detected");
			//						mapPosixModule(resultResource, (PosixModule) module);
			//					} else if (module instanceof RegexModule) {
			//						System.out.println("RegexModule module detected");
			//						mapRegexModule(resultResource, (RegexModule) module);
			//					} else if (module instanceof SigarFileInfo) {
			//						System.out.println("SigarFileInfo module detected");
			//						mapSigarFileInfo(resultResource, (SigarFileInfo) module);
			//					} else if (module instanceof SpotlightMetadataModule) {
			//						System.out.println("SpotlightMetadataModule module detected");
			//						mapSpotlightMetadataModule(resultResource,
			//								(SpotlightMetadataModule) module);
			//					} else if (module instanceof GeneralNativeCommandModule) {
			//						System.out
			//						.println("GeneralNativeCommandModule module detected");
			//						mapGeneralNativeCommandModule(resultResource,
			//								(GeneralNativeCommandModule) module);
			//					} else {
			//						System.err.println("Module unknown!");
			//						// unknown module!
			//						// TODO: for unknown modules add results in default way
			//						// afterwards
			//						// result.results
			//					}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add general extraction module result information to the ontology. This is
	 * also done for modules which are unknown.
	 * 
	 * @param partResource
	 * @param result
	 * @return
	 */
	private Resource mapGeneralResultInformation(Resource partResource,
			ExtractionResult result) {
		Resource resultResource = ontology.model
				.createResource(ontology.PET2LRM_NS + result.hashCode());//
		Resource moduleResource = ontology.model
				.createResource(ontology.PET2LRM_NS + result.configurationHash);
		moduleResource.addProperty(LRM_static_schema.realizes,
				ontology.abstractModule);
		moduleResource.addProperty(RDFS.label, result.moduleName);

		resultResource.addProperty(ontology.hasClassRepresentation,
				result.moduleClass); // java module class
		resultResource.addProperty(ontology.hasVersion,
				result.moduleVersion);
		resultResource.addProperty(ontology.hasDisplayName,
				result.moduleDisplayName);

		resultResource.addProperty(ontology.hasExtractionDate,
				result.extractionDate.toString());
		resultResource.addProperty(ontology.isExtractedBy, moduleResource);

		resultResource.addProperty(ontology.hasConfiguration,
				result.configurationHash);
		resultResource.addProperty(LRM_static_schema.partOf, partResource);
		return resultResource;
	}

//	private void mapApacheTikaExtractionModule(Resource resource,
//			ApacheTikaExtractionModule module) {
//		// TODO Auto-generated method stub
//	}
//	private OntClass createClass(String name) {
//		return ontology.model.createClass(ontology.PET2LRM_NS + name);
//	}
//
//	private void mapChecksumFileModule(Resource resource,
//			ChecksumFileModule module) {
//
//
//	}
//
//	private void mapFileIdentificationCommandModule(Resource resource,
//			FileIdentificationCommandModule module) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void mapFileStoreModule(Resource resource, FileStoreModule module) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void mapMediaInfoCommandModule(Resource resource,
//			MediaInfoCommandModule module) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void mapOfficeDDTDependencyExtractorModule(Resource resource,
//			OfficeDDTDependencyExtractorModule module) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void mapPDFFontDependencyExtractorModule(Resource resource,
//			PDFFontDependencyExtractorModule module) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void mapPosixModule(Resource resource, PosixModule module) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void mapRegexModule(Resource resource, RegexModule module) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void mapSigarFileInfo(Resource resource, SigarFileInfo module) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void mapSpotlightMetadataModule(Resource resource,
//			SpotlightMetadataModule module) {
//		// TODO Auto-generated method stub
//
//	}


	private OntProperty createProperty(Resource domain, String name) {
		
		OntProperty property = ontology.model
				.createOntProperty(ontology.PET2LRM_NS + EnvironmentModuleMapper.norm(name));
		property.addDomain(domain);
		property.addRange(XSD.xstring);

		return property;
	}

	

}
