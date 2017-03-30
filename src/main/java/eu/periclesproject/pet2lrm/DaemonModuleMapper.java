package eu.periclesproject.pet2lrm;

import model.Event;
import model.ExtractionResult;
import modules.AbstractModule;
import modules.ChromeCLIMonitorModule;
import modules.DirectoryMonitorModule;
import modules.HandleMonitorModule;
import modules.LSOFMonitoringDaemon;
import modules.LSOFMonitoringDaemon.LSOFout;
import modules.NativeProcessIterativeDaemonModule;
import modules.NativeProcessMonitoringDaemonModule;
import LRMv2.LRM_dynamic_schema;
import LRMv2.LRM_static_schema;
import LRMv2.Time_schema;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.formula.functions.Even;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * This class detects, if events originate from a known PET Daemon module. If
 * so, it is possible to add further important information to the ontology.
 */
public class DaemonModuleMapper {
	private final EnvironmentModel ontology;
	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
	
	public DaemonModuleMapper(EnvironmentModel ontology) {
		this.ontology = ontology;
	}

	public void map(Resource resource, Event result) {
			
			Resource resultResource = mapGeneralResultInformation(resource,
					result);
			//			if (module instanceof ChromeCLIMonitorModule) {
//				System.out.println("ChromeCLIMonitorModule module detected");
//				mapChromeCLIMonitorModule(resultResource,
//						(ChromeCLIMonitorModule) module);
//			} else if (module instanceof DirectoryMonitorModule) {
//				System.out.println("DirectoryMonitorModule module detected");
//				mapDirectoryMonitorModule(resultResource,
//						(DirectoryMonitorModule) module);
//			} else if (module instanceof HandleMonitorModule) {
//				System.out.println("HandleMonitorModule module detected");
//				mapHandleMonitorModule(resultResource,
//						(HandleMonitorModule) module);
//			} else if (module instanceof LSOFMonitoringDaemon) {
//				System.out.println("LSOFMonitoringDaemon module detected");
//				mapLSOFMonitoringDaemon(resultResource,
//						(LSOFMonitoringDaemon) module);
//			} else if (module instanceof NativeProcessMonitoringDaemonModule) {
//				System.out
//						.println("NativeProcessMonitoringDaemonModule module detected");
//				mapNativeProcessMonitoringDaemonModule(resultResource,
//						(NativeProcessMonitoringDaemonModule) module);
//			} else if (module instanceof NativeProcessIterativeDaemonModule) {
//				System.out
//						.println("NativeProcessIterativeDaemonModule module detected");
//				mapNativeProcessIterativeDaemonModule(resultResource,
//						(NativeProcessIterativeDaemonModule) module);
//			}
		
	}

	/**
	 * Add general extraction module result information to the ontology. This is
	 * also done for modules which are unknown.
	 * 
	 * @param resource
	 * @param result
	 * @return
	 */
	private Resource mapGeneralResultInformation(Resource resource,
			Event result) {
					// parsedEvent.timestamp;

		
		resource.addProperty(ontology.isExtractedBy, result.reporterName);
		resource.addProperty(ontology.hasFileName, result.fileName);
		resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "type"),result.type);
		resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "timestamp"),""+result.timestamp);
		resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "dateTime"), ResourceFactory.createTypedLiteral(fmt.format(new Date(result.timestamp)),XSDDatatype.XSDdateTime));
		
		//		resource.addProperty(LRM_dynamic_schema.triggeredBy,
//				moduleResource);
		if (result.data instanceof LSOFout) {
			LSOFout d = (LSOFout) result.data;
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "command"),""+d.command);
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "device"),""+d.device);
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "fullProcessName"),""+d.fullProcessName);
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "fullProcessPath"),""+d.fullProcesspath);
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "processArguments"),""+d.processArguments);
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "size"),""+d.size);
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "pid"),""+d.pid);
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "fd"),""+d.FD);
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "node"),""+d.node);
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "type"),""+d.type);
			resource.addProperty(ontology.model.createOntProperty(EnvironmentModel.PET2LRM_NS + "user"),""+d.user);
			
		}
		return resource;
	}

	private void mapChromeCLIMonitorModule(Resource resource,
			ChromeCLIMonitorModule module) {
		// TODO Auto-generated method stub

	}

	private void mapDirectoryMonitorModule(Resource resource,
			DirectoryMonitorModule module) {
		// TODO Auto-generated method stub

	}

	private void mapHandleMonitorModule(Resource resource,
			HandleMonitorModule module) {
		// TODO Auto-generated method stub

	}

	private void mapLSOFMonitoringDaemon(Resource resource,
			LSOFMonitoringDaemon module) {
		// TODO Auto-generated method stub

	}

	private void mapNativeProcessMonitoringDaemonModule(Resource resource,
			NativeProcessMonitoringDaemonModule module) {
		// TODO Auto-generated method stub

	}

	private void mapNativeProcessIterativeDaemonModule(Resource resource,
			NativeProcessIterativeDaemonModule module) {
		// TODO Auto-generated method stub

	}
}
