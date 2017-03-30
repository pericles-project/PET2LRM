package eu.periclesproject.pet2lrm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import model.Environment;
import model.ExtractionResult;
import model.KeyValueResult;
import modules.AbstractModule;
import modules.GeneralNativeCommandModuleEnv;
import modules.GraphicCardInformationModule;
import modules.GraphicProperiesModule;
import modules.GraphicProperiesModule.GraphicProperies;
import modules.InstalledSoftwareModule;
import modules.JavaInstallationInformationModule;
import modules.ProcessExtractionModules.ProcessParameterExtractor;
import modules.ScreenshotModule;
import modules.ScreenshotModule.ScreenShot;
import modules.SigarEnvironmentModules.SigarCpuInfo;
import modules.SigarEnvironmentModules.SigarCpuModule;
import modules.SigarEnvironmentModules.SigarFQDN;
import modules.SigarEnvironmentModules.SigarFileSystem;
import modules.SigarEnvironmentModules.SigarMemoryInfo;
import modules.SigarEnvironmentModules.SigarNetworkConfig;
import modules.SigarEnvironmentModules.SigarNetworkInterfaces;
import modules.SigarEnvironmentModules.SigarProcStat;
import modules.SigarEnvironmentModules.SigarSwap;
import modules.SigarEnvironmentModules.SigarSystemResources;
import modules.SigarEnvironmentModules.SigarTcp;
import modules.SigarEnvironmentModules.SigarUptime;
import modules.SigarEnvironmentModules.SigarWho;
import modules.SimpleLogGrep;
import modules.SimpleLogGrep.GrepMatch;
import modules.SimpleXPath;
import modules.SimpleXPath.QueryMatch;
import modules.SystemProperiesModule;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.ProcStat;
import org.hyperic.sigar.ResourceLimit;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Tcp;
import org.hyperic.sigar.Uptime;
import org.hyperic.sigar.Who;

import LRMv2.LRM_static_schema;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

/**
 * This class detects, if the extraction results originate from a known PET
 * environment ExtractionModule. If so, it is possible to add further important
 * information to the ontology.
 */
public class EnvironmentModuleMapper {
	private final EnvironmentModel ontology;

	public EnvironmentModuleMapper(EnvironmentModel ontology) {
		this.ontology = ontology;
	}

	public void map(Individual environmentIndividual, Environment environment) {
		for (ExtractionResult result : environment.extractionResults) {
			map(environmentIndividual, result);
		}
	}

	public void map(Individual environment, ExtractionResult extractionResult) {
		try {
			getModuleTypeAndMap(environment, extractionResult);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void getModuleTypeAndMap(Individual environment,
			ExtractionResult extractionResult) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		@SuppressWarnings("rawtypes")
		Class moduleClass = Class.forName(extractionResult.moduleClass);
		AbstractModule module = (AbstractModule) moduleClass.newInstance();
		Individual generalResult = mapGeneralResultInformation(environment,
				extractionResult);
		if (module instanceof GraphicCardInformationModule) {
			mapGraphicCardInformationModule(generalResult, environment,
					(GraphicCardInformationModule) module, extractionResult);
		} 
	else if (module instanceof GraphicProperiesModule) {
			mapGraphicProperiesModule(generalResult, environment,
					(GraphicProperiesModule) module, extractionResult);
		} else if (module instanceof InstalledSoftwareModule) {
			mapInstalledSoftwareModule(generalResult, environment,
					(InstalledSoftwareModule) module, extractionResult);
		} else if (module instanceof JavaInstallationInformationModule) {
			mapJavaInstallationInformationModule(generalResult, environment,
					(JavaInstallationInformationModule) module,
					extractionResult);
//		} else if (module instanceof ProcessParameterExtractor) {
//			mapProcessParameterExtractor(generalResult, environment,
//					(ProcessParameterExtractor) module, extractionResult);
		} else if (module instanceof ScreenshotModule) {
			mapScreenshotModule(generalResult, environment,
					(ScreenshotModule) module, extractionResult);
		} else if (module instanceof SimpleLogGrep) {
			mapSimpleLogGrep(generalResult, environment,
					(SimpleLogGrep) module, extractionResult);
		} else if (module instanceof SimpleXPath) {
			mapSimpleXPath(generalResult, environment, (SimpleXPath) module,
					extractionResult);
		} else if (module instanceof SystemProperiesModule) {
			mapSystemProperiesModule(generalResult, environment,
					(SystemProperiesModule) module, extractionResult);
//		} else if (module instanceof SigarCpuModule) {
//			mapSigarCpuModule(generalResult, environment,
//					(SigarCpuModule) module, extractionResult);
		} else if (module instanceof SigarCpuInfo) {
			mapSigarCpuInfo(generalResult, environment, (SigarCpuInfo) module,
					extractionResult);
//		} else if (module instanceof SigarFileSystem) {
//			mapSigarFileSystem(generalResult, environment,
//					(SigarFileSystem) module, extractionResult);
		} else if (module instanceof SigarMemoryInfo) {
			mapSigarMemoryInfo(generalResult, environment,
					(SigarMemoryInfo) module, extractionResult);
//		} else if (module instanceof SigarNetworkConfig) {
//			mapSigarNetworkConfig(generalResult, environment,
//					(SigarNetworkConfig) module, extractionResult);
		} else if (module instanceof SigarNetworkInterfaces) {
			mapSigarNetworkInterfaces(generalResult, environment,
					(SigarNetworkInterfaces) module, extractionResult);
		} else if (module instanceof SigarProcStat) {
			mapSigarProcStat(generalResult, environment,
					(SigarProcStat) module, extractionResult);
		} else if (module instanceof SigarSystemResources) {
			mapSigarSystemResources(generalResult, environment,
					(SigarSystemResources) module, extractionResult);
		} else if (module instanceof SigarSwap) {
			mapSigarSwap(generalResult, environment, (SigarSwap) module,
					extractionResult);
		} else if (module instanceof SigarTcp) {
			mapSigarTcp(generalResult, environment, (SigarTcp) module,
					extractionResult);
		} else if (module instanceof SigarUptime) {
			mapSigarUptime(generalResult, environment, (SigarUptime) module,
					extractionResult);
		} else if (module instanceof SigarWho) {
			mapSigarWho(generalResult, environment, (SigarWho) module,
					extractionResult);
		} else if (module instanceof SigarFQDN) {
			mapSigarFQDN(generalResult, environment, (SigarFQDN) module,
					extractionResult);
		} else if (module instanceof GeneralNativeCommandModuleEnv) {
			mapGeneralNativeCommandModuleEnv(generalResult, environment,
					(GeneralNativeCommandModuleEnv) module, extractionResult);
		} else if (extractionResult.results instanceof KeyValueResult) {

			KeyValueResult keyValueResult = (KeyValueResult) extractionResult.results;

			for (Entry<String,String>r: keyValueResult.results.entrySet()) {

				if (r.getValue()==null)
					continue;

				OntProperty p = createProperty(
						ontology.abstractExtractionResult, r.getKey().toLowerCase());
				// 3. Map the results to general result:
				generalResult.addProperty(p,
						r.getValue());

			} }
		else {System.out.println("Module " + module.moduleName + " not supported");

		}
	}

	private void mapGraphicProperiesModule(Individual generalResult, Individual environment,
			GraphicProperiesModule module, ExtractionResult extractionResult) {
			// 1. System out + casting of the results:
			//GraphicProperies gp  = (GraphicProperies) extractionResult.results;
		// TODO: add gp to the ontology
			
		}
		
	

	/**
	 * Add general extraction module result information to the ontology. This is
	 * also done for modules which are unknown.
	 * 
	 * @param environment
	 * @param extractionResult
	 * @return An ontology class that keeps basic information about the
	 *         extraction result. This class is used later to store additional
	 *         information about the extraction result.
	 */
	private Individual mapGeneralResultInformation(Individual environment,
			ExtractionResult extractionResult) {
		// Create specific module class, or get it if it already exists:
		OntClass specificModule = ontology.model
				.createClass(ontology.PET2LRM_NS + extractionResult.moduleName);
		specificModule.addSuperClass(ontology.abstractModule);
		specificModule.addProperty(ontology.hasClassRepresentation,
				extractionResult.moduleClass); // java module class
		specificModule.addProperty(ontology.hasVersion,
				extractionResult.moduleVersion);
		// Configured specific module is an instance:
		Individual module = createIndividual(specificModule,
				extractionResult.configurationHash);
		module.addProperty(RDFS.label, extractionResult.moduleName);
		module.addProperty(ontology.hasDisplayName,
				extractionResult.moduleDisplayName);
		module.addProperty(ontology.hasConfiguration,
				extractionResult.configurationHash);
		// The instance extracts an instance of extraction result (information):
		Individual result = createIndividual(ontology.abstractExtractionResult,
				"" + extractionResult.hashCode());
		result.addProperty(ontology.isExtractedBy, module);
		result.addProperty(ontology.hasExtractionDate,
				extractionResult.extractionDate.toString());
		// The extracted environment information belongs to an environment
		// individual:
		result.addProperty(LRM_static_schema.partOf, environment);
		return result;
	}

	private void mapJavaInstallationInformationModule(Individual generalResult,
			Individual environment, JavaInstallationInformationModule module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("JavaInstallationInformationModule module detected");
		KeyValueResult keyValueResult = (KeyValueResult) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntProperty hasJavaInstallation = createProperty(
				ontology.abstractEnvironment,
				ontology.abstractExtractionResult, "hasJavaInstallation");
		OntProperty javaClassPath = createProperty(
				ontology.abstractExtractionResult, "javaClassPath");
		OntProperty javaHome = createProperty(
				ontology.abstractExtractionResult, "javaHome");
		OntProperty version = createProperty(ontology.abstractExtractionResult,
				"version");
		OntProperty hasJavaVendor = createProperty(
				ontology.abstractExtractionResult, "javaVendor");
		//OntClass javaVendor = createClass("JavaVendor");
		//Individual vendor = createIndividual(javaVendor,
				//keyValueResult.results.get("java_vendor"));
//		javaVendor.addProperty(LRM_static_schema.identification,
//				keyValueResult.results.get("java_vendorUrl"));
		// 3. Map the results to general result:
		generalResult.addProperty(javaClassPath,
				keyValueResult.results.get("java_class_path"));
		generalResult.addProperty(javaHome,
				keyValueResult.results.get("java_home"));
		generalResult.addProperty(hasJavaVendor, keyValueResult.results.get("java_vendor"));
		generalResult.addProperty(version,
				keyValueResult.results.get("java_version"));
		// 4. Map the general result to environment:
		environment.addProperty(hasJavaInstallation, generalResult);
	}

	private void mapGraphicCardInformationModule(Individual generalResult,
			Individual environment, GraphicCardInformationModule module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("GraphicCardInformationModule module detected");
		KeyValueResult keyValueResult = (KeyValueResult) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntClass graphicCard = createClass("GraphicCard");
		OntProperty hasSpecification = createProperty(graphicCard,
				"hasSpecification");
		Individual thisGraphicCard = createIndividual(graphicCard,
				"SystemGraphicCard");
		thisGraphicCard.addProperty(hasSpecification,
				keyValueResult.results.get("fullOutput"));
		// 3. Map the results to general result:
		results.add(thisGraphicCard);
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results,
				"extractedGraphicCard", "hasGraphicCard");
	}

//	private void mapGraphicProperiesModule(Individual generalResult,
//			Individual environment, GraphicProperiesModule module,
//			ExtractionResult result) {
//		// // 1. System out + casting of the results:
//		// System.out.println("GraphicProperiesModule module detected");
//		// GraphicProperies graphicProperties = (GraphicProperies)
//		// result.results;
//		// List<Individual> results = new ArrayList<Individual>();
//		// // 2. Mapping of the results to specific OntClasses, OntProperties,
//		// and
//		// // Individuals:
//		// OntProperty hasGraphicProperties = createProperty(
//		// ontology.abstractEnvironment,
//		// ontology.abstractExtractionResult, "hasGraphicProperties");
//		// OntProperty hasScreenResolution = createProperty(
//		// ontology.abstractExtractionResult, "hasScreenResolution");
//		// // 2a. Mapping of the font results to specific OntClasses,
//		// // OntProperties, and Individuals:
//		// OntClass installedFont = createClass("InstalledFont");
//		// OntProperty installedFontResult = createProperty(
//		// ontology.abstractExtractionResult, "installedFont");
//		// for (String font_family : graphicProperties.font_family_names) {
//		// Individual font = createIndividual(installedFont, font_family);
//		// // 3a. Map the font to general result:
//		// generalResult.addProperty(installedFontResult, font);
//		// }
//		// // 2b. Mapping of the display results to specific OntClasses,
//		// // OntProperties, and Individuals:
//		// OntClass installedDisplay = createClass("InstalledDisplay");
//		// OntProperty isDefaultDisplay = createProperty(installedDisplay,
//		// "isDefaultDisplay");
//		// OntProperty hasInstalledDisplay = createProperty(
//		// ontology.abstractExtractionResult, "installedDisplay");
//		// OntProperty bitDepth = createProperty(installedDisplay, "bitDepth");
//		// OntProperty refreshRate = createProperty(installedDisplay,
//		// "refreshRate");
//		// OntProperty width = createProperty(installedDisplay, "width");
//		// OntProperty height = createProperty(installedDisplay, "height");
//		// for (DisplayInformation information :
//		// graphicProperties.displayInformation) {
//		// Individual thisDisplay = createIndividual(installedDisplay,
//		// information.idString);
//		// if (information.isDefaultDisplay) {
//		// thisDisplay.addProperty(isDefaultDisplay, "yes");
//		// }
//		// thisDisplay.addProperty(bitDepth, "" + information.bitDepth);
//		// thisDisplay.addProperty(refreshRate, "" + information.refreshRate);
//		// thisDisplay.addProperty(width, "" + information.width);
//		// thisDisplay.addProperty(height, "" + information.height);
//		// // 3b. Map the display to general result:
//		// generalResult.addProperty(hasInstalledDisplay, thisDisplay);
//		// }
//		// // 3. Map the results to general result:
//		// generalResult.addProperty(hasScreenResolution, ""
//		// + graphicProperties.screenResolution);
//		// // 4. Map the general result to environment:
//		// environment.addProperty(hasGraphicProperties, generalResult);
//	}

	private void mapInstalledSoftwareModule(Individual generalResult,
			Individual environment, InstalledSoftwareModule module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("InstalledSoftwareModule module detected");
		KeyValueResult keyValueResult = (KeyValueResult) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntProperty hasSoftwareList = createProperty(
				ontology.abstractExtractionResult, "installedSoftwareList");
		hasSoftwareList.addDomain(ontology.abstractEnvironment);
		// 3. Map the results to general result:
		generalResult.addProperty(hasSoftwareList,
				keyValueResult.results.get("fullOutput"));
		// 4. Map the general result to environment:
		environment.addProperty(hasSoftwareList, generalResult);
	}
//
//	private void mapProcessParameterExtractor(Individual generalResult,
//			Individual environment, ProcessParameterExtractor module,
//			ExtractionResult result) {
//		// // 1. System out + casting of the results:
//		// System.out.println("ProcessParameterExtractor module detected");
//		// LinkedList<ProcessInformation> processInformationList =
//		// (LinkedList<ProcessInformation>) result.results;
//		// List<Individual> results = new ArrayList<Individual>();
//		// // 2. Mapping of the results to specific OntClasses, OntProperties,
//		// and
//		// // Individuals:
//		// OntClass systemProcess = createClass("SystemProcess");
//		// systemProcess.addSuperClass(LRM_dynamic_schema.Action);
//		// OntProperty hasPID = createProperty(systemProcess, "hasPID");
//		// OntProperty hasPath = createProperty(systemProcess, "hasPath");
//		// OntProperty hasCWD = createProperty(systemProcess, "hasCWD");
//		// OntProperty hasName = createProperty(systemProcess, "hasName");
//		// OntClass systemUser = createClass("SystemUser");
//		// systemUser.addSuperClass(LRM_dynamic_schema.Agent);
//		// // 3. Map the results to general result:
//		// for (int i = 0; i < processInformationList.size(); i++) {
//		// ProcessInformation information = processInformationList
//		// .get(i);
//		// Individual runningProcess = createIndividual(systemProcess, ""
//		// + information.pid);
//		// runningProcess.addProperty(hasPID, "" + information.pid);
//		// runningProcess.addProperty(hasName, information.processName);
//		// runningProcess.addProperty(hasPath, information.processFullPath);
//		// runningProcess.addProperty(hasCWD, information.cwd);
//		// Individual executingUser = createIndividual(systemUser,
//		// information.user);
//		// runningProcess.addProperty(LRM_dynamic_schema.executedBy,
//		// executingUser);
//		// results.add(runningProcess);
//		// }
//		// // 4. Map the general result to environment:
//		// mapToEnvironment(generalResult, environment, results,
//		// "systemProcess",
//		// "hasRunningProcesses");
//	}

	private void mapScreenshotModule(Individual generalResult,
			Individual environment, ScreenshotModule module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("ScreenshotModule module detected");
		ScreenShot[] screenshots = (ScreenShot[]) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntClass screenshot = createClass("Screenshot");
		OntProperty hasFormat = createProperty(screenshot, "hasFormat");
		OntProperty hasScreenName = createProperty(screenshot, "screenName");
		for (ScreenShot shot : screenshots) {
			Individual capturedScreenshot = createIndividual(screenshot,
					shot.imageContentsId);
			capturedScreenshot.addProperty(hasFormat, shot.format);
			capturedScreenshot.addProperty(hasScreenName, shot.screenName);
			// 3. Map the results to general result:
			results.add(capturedScreenshot);
		}
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results, "captured",
				"capturedScreenshots");
	}

	private void mapSimpleLogGrep(Individual generalResult,
			Individual environment, SimpleLogGrep module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SimpleLogGrep module detected");
		ArrayList<GrepMatch> matches = (ArrayList<GrepMatch>) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntClass logMatch = createClass("LogMatch");
		OntProperty usedRegex = createProperty(logMatch, "usedRegex");
		OntProperty hasLineNumber = createProperty(logMatch, "hasLineNumber");
		OntProperty hasLine = createProperty(logMatch, "hasLine");
		OntProperty hasFileName = createProperty(logMatch, "hasFileName");
		for (GrepMatch match : matches) {
			Individual thisMatch = createIndividual(logMatch, match.getClass()
					.getName());
			thisMatch.addProperty(usedRegex, match.regularExpression);
			thisMatch.addProperty(hasLineNumber, "" + match.lineNumber);
			thisMatch.addProperty(hasLine, match.line);
			thisMatch.addProperty(hasFileName, match.fileName);
			// 3. Map the results to general result:
			results.add(thisMatch);
		}
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results, "logEntry",
				"hasLogs");
	}

	private void mapSimpleXPath(Individual generalResult,
			Individual environment, SimpleXPath module, ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SimpleXPath module detected");
		ArrayList<QueryMatch> matches = (ArrayList<QueryMatch>) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntClass xpathMatch = createClass("XPathMatch");
		OntProperty usedQuery = createProperty(xpathMatch, "usedQuery");
		OntProperty hasResult = createProperty(xpathMatch, "hasResult");
		OntProperty hasFileName = createProperty(xpathMatch, "hasFileName");
		for (QueryMatch match : matches) {
			Individual thisMatch = createIndividual(xpathMatch, match
					.getClass().getName());
			thisMatch.addProperty(usedQuery, match.query);
			thisMatch.addProperty(hasResult, match.result);
			thisMatch.addProperty(hasFileName, match.fileName);
			// 3. Map the results to general result:
			results.add(thisMatch);
		}
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results, "XPathEntry",
				"hasXPathMatches");
	}

	private void mapSystemProperiesModule(Individual generalResult,
			Individual environment, SystemProperiesModule module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SystemProperiesModule module detected");
		KeyValueResult resultList = (KeyValueResult) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntClass operatingSystem = createClass("OperatingSystem");
		operatingSystem.addSuperClass(LRM_static_schema.SoftwareAgent);
		Individual thisOS = createIndividual(operatingSystem,
				resultList.results.get("os_name"));
		addProperty(thisOS, operatingSystem, "os_name",
				resultList.results.get("os_name"));
		addProperty(thisOS, operatingSystem, "hasVersion",
				resultList.results.get("os_version"));
		addProperty(thisOS, operatingSystem, "usesFileSeperator",
				resultList.results.get("file_seperator"));
		addProperty(thisOS, operatingSystem, "userLanguage",
				resultList.results.get("user_language"));
		addProperty(thisOS, operatingSystem, "userName",
				resultList.results.get("user_name"));
		addProperty(thisOS, operatingSystem, "usesTimezone",
				resultList.results.get("user_timezone"));
		addProperty(thisOS, operatingSystem, "usesHome",
				resultList.results.get("user_home"));
		// 3. Map the results to general result:
		results.add(thisOS);
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results,
				"operatingSystem", "hasOperatingSystem");
	}

//	private void mapSigarCpuModule(Individual generalResult,
//			Individual environment, SigarCpuModule module,
//			ExtractionResult result) {
		// // 1. System out + casting of the results:
		// System.out.println("SigarCpuModule module detected");
		// Map cpuMap = (Map) result.results;
		// List<Individual> results = new ArrayList<Individual>();
		// // 2. Mapping of the results to specific OntClasses, OntProperties,
		// and
		// // Individuals:
		// OntClass cpu = createClass("CPU");
		// Individual thisCPU = createIndividual(cpu, "SystemCPU");
		// addProperty(thisCPU, cpu, "user", "" + (long) cpuMap.get("User"));
		// addProperty(thisCPU, cpu, "sys", "" + (long) cpuMap.get("Sys"));
		// addProperty(thisCPU, cpu, "nice", "" + (long) cpuMap.get("Nice"));
		// addProperty(thisCPU, cpu, "idle", "" + (long) cpuMap.get("Idle"));
		// addProperty(thisCPU, cpu, "wait", "" + (long) cpuMap.get("Wait"));
		// addProperty(thisCPU, cpu, "irq", "" + (long) cpuMap.get("Irq"));
		// addProperty(thisCPU, cpu, "softIrq", "" + (long)
		// cpuMap.get("SoftIrq"));
		// addProperty(thisCPU, cpu, "stolen", "" + (long)
		// cpuMap.get("Stolen"));
		// addProperty(thisCPU, cpu, "total", "" + (long) cpuMap.get("Total"));
		// // 3. Map the results to general result:
		// results.add(thisCPU);
		// // 4. Map the general result to environment:
		// mapToEnvironment(generalResult, environment, results, "CPU",
		// "hasCPU");
//	}

	private void mapSigarCpuInfo(Individual generalResult,
			Individual environment, SigarCpuInfo module, ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SigarCpuInfo module detected");
		CpuInfo[] infoList = (CpuInfo[]) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntClass cpu = createClass("CPU");
		OntProperty vendor = createProperty(cpu, "vendor");
		OntProperty model = createProperty(cpu, "model");
		OntProperty mhz = createProperty(cpu, "mhz");
		OntProperty cacheSize = createProperty(cpu, "cacheSize");
		OntProperty totalCores = createProperty(cpu, "totalCores");
		OntProperty totalSockets = createProperty(cpu, "totalSockets");
		OntProperty coresPerSocket = createProperty(cpu, "coresPerSocket");
		for (int i = 0; i < infoList.length; i++) {
			CpuInfo cpuInfo = infoList[i];
			Individual thisCPU = createIndividual(cpu, "SystemCPU-" + i);
			thisCPU.addProperty(vendor, cpuInfo.getVendor());
			thisCPU.addProperty(model, cpuInfo.getModel());
			thisCPU.addProperty(mhz, "" + cpuInfo.getMhz());
			thisCPU.addProperty(cacheSize, "" + cpuInfo.getCacheSize());
			thisCPU.addProperty(totalCores, "" + cpuInfo.getTotalCores());
			thisCPU.addProperty(totalSockets, "" + cpuInfo.getTotalSockets());
			thisCPU.addProperty(coresPerSocket,
					"" + cpuInfo.getCoresPerSocket());
			// 3. Map the results to general result:
			results.add(thisCPU);
		}
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results, "CPU", "hasCPU");
	}

//	private void mapSigarFileSystem(Individual generalResult,
//			Individual environment, SigarFileSystem module,
//			ExtractionResult result) {
//		// // 1. System out + casting of the results:
//		// System.out.println("SigarFileSystem module detected");
//		// LinkedList<FileSystem> fileSystems = (LinkedList<FileSystem>)
//		// result.results;
//		// List<Individual> results = new ArrayList<Individual>();
//		// // 2. Mapping of the results to specific OntClasses, OntProperties,
//		// and
//		// // Individuals:
//		// OntClass fileSystem = createClass("FileSystem");
//		// OntProperty dirName = createProperty(fileSystem, "dirName");
//		// OntProperty devName = createProperty(fileSystem, "devName");
//		// OntProperty typeName = createProperty(fileSystem, "typeName");
//		// OntProperty sysTypeName = createProperty(fileSystem, "sysTypeName");
//		// OntProperty options = createProperty(fileSystem, "options");
//		// OntProperty type = createProperty(fileSystem, "type");
//		// OntProperty flags = createProperty(fileSystem, "flags");
//		// for (int i = 0; i < fileSystems.size(); i++) {
//		// FileSystem FS = fileSystems.get(i);
//		// Individual thisFS = createIndividual(fileSystem, "FileSystem-" + i);
//		// thisFS.addProperty(dirName, FS.getDirName());
//		// thisFS.addProperty(devName, FS.getDevName());
//		// thisFS.addProperty(typeName, FS.getTypeName());
//		// thisFS.addProperty(sysTypeName, FS.getSysTypeName());
//		// thisFS.addProperty(options, FS.getOptions());
//		// thisFS.addProperty(type, FS.getTypeName());
//		// thisFS.addProperty(flags, "" + FS.getFlags());
//		// // 3. Map the results to general result:
//		// results.add(thisFS);
//		// }
//		// // 4. Map the general result to environment:
//		// mapToEnvironment(generalResult, environment, results, "fileSystem",
//		// "hasFileSystems");
//	}

	private void mapSigarMemoryInfo(Individual generalResult,
			Individual environment, SigarMemoryInfo module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SigarMemoryInfo module detected");
		Mem memory = (Mem) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntClass mem = createClass("Memory");
		Individual thisMemory = createIndividual(mem, "SystemMemory");
		addProperty(thisMemory, mem, "total", "" + memory.getTotal());
		addProperty(thisMemory, mem, "ram", "" + memory.getRam());
		addProperty(thisMemory, mem, "used", "" + memory.getUsed());
		addProperty(thisMemory, mem, "free", "" + memory.getFree());
		addProperty(thisMemory, mem, "actualUsed", "" + memory.getActualUsed());
		addProperty(thisMemory, mem, "actualFree", "" + memory.getActualFree());
		addProperty(thisMemory, mem, "usedPercent",
				"" + memory.getUsedPercent());
		addProperty(thisMemory, mem, "freePercent",
				"" + memory.getFreePercent());
		// 3. Map the results to general result:
		results.add(thisMemory);
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results, "memory",
				"hasMemory");
	}

//	private void mapSigarNetworkConfig(Individual generalResult,
//			Individual environment, SigarNetworkConfig module,
//			ExtractionResult result) {
//
//		// TODO: this can only be casted correctly, if NetworkInformation class
//		// of PET is public
//
//		// // 1. System out + casting of the results:
//		// System.out.println("SigarNetworkConfig module detected");
//		// NetworkInformation netInfos = (NetworkInformation) result.results;
//		// List<Individual> results = new ArrayList<Individual>();
//		// NetInfo netInfo = netInfos.netInfo;
//		// NetInterfaceConfig netConf = netInfos.netConfig;
//		// // 2. Mapping of the results to specific OntClasses, OntProperties,
//		// // and Individuals:
//		// OntClass info = createClass("NetworkInformation");
//		// Individual thisNet = createIndividual(info, "Network");
//		// addProperty(thisNet, info, "defaultGateway",
//		// netInfo.getDefaultGateway());
//		// addProperty(thisNet, info, "hostName", netInfo.getHostName());
//		// addProperty(thisNet, info, "domainName", netInfo.getDomainName());
//		// addProperty(thisNet, info, "primaryDns", netInfo.getPrimaryDns());
//		// addProperty(thisNet, info, "secondaryDns",
//		// netInfo.getSecondaryDns());
//		// addProperty(thisNet, info, "name", netConf.getName());
//		// addProperty(thisNet, info, "hwaddr", netConf.getHwaddr());
//		// addProperty(thisNet, info, "type", netConf.getType());
//		// addProperty(thisNet, info, "description", netConf.getDescription());
//		// addProperty(thisNet, info, "address", netConf.getAddress());
//		// addProperty(thisNet, info, "destination", netConf.getDestination());
//		// addProperty(thisNet, info, "broadcast", netConf.getBroadcast());
//		// addProperty(thisNet, info, "netmask", netConf.getNetmask());
//		// addProperty(thisNet, info, "flags", "" + netConf.getFlags());
//		// addProperty(thisNet, info, "mtu", "" + netConf.getMtu());
//		// addProperty(thisNet, info, "metric", "" + netConf.getMetric());
//		// // 3. Map the results to general result:
//		// results.add(thisNet);
//		// // 4. Map the general result to environment:
//		// mapToEnvironment(generalResult, environment, results,
//		// "networkInformation", "hasNetwork");
//	}

	private void mapSigarNetworkInterfaces(Individual generalResult,
			Individual environment, SigarNetworkInterfaces module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SigarNetworkInterfaces module detected");
		String[] netInterfaceList = (String[]) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties,
		// and Individuals:
		OntClass networkInterface = createClass("NetworkInterface");
		for (String netInterface : netInterfaceList) {
			Individual thisIF = createIndividual(networkInterface, netInterface);
			// 3. Map the results to general result:
			results.add(thisIF);
		}// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results,
				"networkInterface", "hasNetworkInterfaces");
	}

	private void mapSigarProcStat(Individual generalResult,
			Individual environment, SigarProcStat module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SigarProcStat module detected");
		ProcStat procStat = (ProcStat) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties,
		// and Individuals:
		OntClass sysProcs = createClass("SystemProcesses");
		Individual thisProcs = createIndividual(sysProcs,
				"CurrentProcessInformation");
		addProperty(thisProcs, sysProcs, "total", "" + procStat.getTotal());
		addProperty(thisProcs, sysProcs, "idle", "" + procStat.getIdle());
		addProperty(thisProcs, sysProcs, "running", "" + procStat.getRunning());
		addProperty(thisProcs, sysProcs, "sleeping",
				"" + procStat.getSleeping());
		addProperty(thisProcs, sysProcs, "stopped", "" + procStat.getStopped());
		addProperty(thisProcs, sysProcs, "zombie", "" + procStat.getZombie());
		addProperty(thisProcs, sysProcs, "threads", "" + procStat.getThreads());
		// 3. Map the results to general result:
		results.add(thisProcs);
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results,
				"processInformation", "currentProcessInformation");
	}

	private void mapSigarSystemResources(Individual generalResult,
			Individual environment, SigarSystemResources module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SigarSystemResources module detected");
		ResourceLimit res = (ResourceLimit) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties,
		// and Individuals:
		OntClass sysRes = createClass("systemResourses");
		Individual thisRes = createIndividual(sysRes, "CurrentResources");
		addProperty(thisRes, sysRes, "cpuCur", "" + res.getCpuCur());
		addProperty(thisRes, sysRes, "cpuMax", "" + res.getCpuCur());
		addProperty(thisRes, sysRes, "fileSizeCur", "" + res.getFileSizeCur());
		addProperty(thisRes, sysRes, "fileSizeMax", "" + res.getFileSizeMax());
		addProperty(thisRes, sysRes, "pipeSizeMax", "" + res.getPipeSizeMax());
		addProperty(thisRes, sysRes, "pipeSizeCur", "" + res.getPipeSizeCur());
		addProperty(thisRes, sysRes, "dataCur", "" + res.getDataCur());
		addProperty(thisRes, sysRes, "dataMax", "" + res.getDataMax());
		addProperty(thisRes, sysRes, "stackCur", "" + res.getStackCur());
		addProperty(thisRes, sysRes, "stackMax", "" + res.getStackMax());
		addProperty(thisRes, sysRes, "coreCur", "" + res.getCoreCur());
		addProperty(thisRes, sysRes, "coreMax", "" + res.getCoreMax());
		addProperty(thisRes, sysRes, "memoryCur", "" + res.getMemoryCur());
		addProperty(thisRes, sysRes, "memoryMax", "" + res.getMemoryMax());
		addProperty(thisRes, sysRes, "processesCur", "" + res.getProcessesCur());
		addProperty(thisRes, sysRes, "processesMax", "" + res.getProcessesMax());
		addProperty(thisRes, sysRes, "openFilesCur", "" + res.getOpenFilesCur());
		addProperty(thisRes, sysRes, "openFilesMax", "" + res.getOpenFilesMax());
		addProperty(thisRes, sysRes, "virtualMemoryCur",
				"" + res.getVirtualMemoryCur());
		addProperty(thisRes, sysRes, "virtualMemoryMax",
				"" + res.getVirtualMemoryMax());
		// 3. Map the results to general result:
		results.add(thisRes);
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results,
				"systemResourceInformation", "currentResourceInformation");
	}

	private void mapSigarSwap(Individual generalResult, Individual environment,
			SigarSwap module, ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SigarSwap module detected");
		Swap swap = (Swap) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties,
		// and Individuals:
		OntClass sysSwap = createClass("SystemSwap");
		Individual thisSwap = createIndividual(sysSwap, "CurrentSwap");
		addProperty(thisSwap, sysSwap, "total", "" + swap.getTotal());
		addProperty(thisSwap, sysSwap, "used", "" + swap.getUsed());
		addProperty(thisSwap, sysSwap, "free", "" + swap.getFree());
		addProperty(thisSwap, sysSwap, "pageIn", "" + swap.getPageIn());
		addProperty(thisSwap, sysSwap, "pageOut", "" + swap.getPageOut());
		// 3. Map the results to general result:
		results.add(thisSwap);
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results, "systemSwap",
				"currentSwapInformation");
	}

	private void mapSigarTcp(Individual generalResult, Individual environment,
			SigarTcp module, ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SigarTcp module detected");
		Tcp tcp = (Tcp) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties,
		// and Individuals:
		OntClass tcpClass = createClass("TCP");
		Individual thisTCP = createIndividual(tcpClass, "CurrentTCP");
		addProperty(thisTCP, tcpClass, "activeOpens", "" + tcp.getActiveOpens());
		addProperty(thisTCP, tcpClass, "passiveOpens",
				"" + tcp.getPassiveOpens());
		addProperty(thisTCP, tcpClass, "attemptFails",
				"" + tcp.getAttemptFails());
		addProperty(thisTCP, tcpClass, "estabResets", "" + tcp.getEstabResets());
		addProperty(thisTCP, tcpClass, "currEstab", "" + tcp.getCurrEstab());
		addProperty(thisTCP, tcpClass, "inSegs", "" + tcp.getInSegs());
		addProperty(thisTCP, tcpClass, "outSegs", "" + tcp.getOutSegs());
		addProperty(thisTCP, tcpClass, "retransSegs", "" + tcp.getRetransSegs());
		addProperty(thisTCP, tcpClass, "inErrs", "" + tcp.getInErrs());
		addProperty(thisTCP, tcpClass, "outRsts", "" + tcp.getOutRsts());
		// 3. Map the results to general result:
		results.add(thisTCP);
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results, "TCPInformation",
				"currentTCPInformation");
	}

	private void mapSigarUptime(Individual generalResult,
			Individual environment, SigarUptime module, ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SigarUptime module detected");
		Uptime uptime = (Uptime) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties,
		// and Individuals:
		OntClass uptimeClass = createClass("SystemUptime");
		Individual thisUptime = createIndividual(uptimeClass, "CurrentUptime");
		addProperty(thisUptime, uptimeClass, "uptime", "" + uptime.getUptime());
		// 3. Map the results to general result:
		results.add(thisUptime);
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results, "uptime",
				"uptime");
	}

	private void mapSigarWho(Individual generalResult, Individual environment,
			SigarWho module, ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SigarWho module detected");
		Who[] whoList = (Who[]) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties,
		// and Individuals:
		OntClass whoClass = createClass("SystemUser");
		for (Who who : whoList) {
			Individual thisWho = createIndividual(whoClass, who.getUser());
			addProperty(thisWho, whoClass, "device", who.getDevice());
			addProperty(thisWho, whoClass, "host", who.getHost());
			addProperty(thisWho, whoClass, "time", "" + who.getTime());
			// 3. Map the results to general result:
			results.add(thisWho);
		}
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results, "whoList",
				"CurrentUsers");
	}

	private void mapSigarFQDN(Individual generalResult, Individual environment,
			SigarFQDN module, ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("SigarFQDN module detected");
		KeyValueResult keyValueResult = (KeyValueResult) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntClass sysFQDN = createClass("SystemFQDN");
		Individual thisFQDN = createIndividual(sysFQDN, "fqdn");
		addProperty(thisFQDN, sysFQDN, "fqdn",
				keyValueResult.results.get("SigarFQDN"));
		// 3. Map the results to general result:
		results.add(thisFQDN);
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results,
				"fqdnInformation", "systemFQDN");
	}

	private void mapGeneralNativeCommandModuleEnv(Individual generalResult,
			Individual environment, GeneralNativeCommandModuleEnv module,
			ExtractionResult result) {
		// 1. System out + casting of the results:
		System.out.println("GeneralNativeCommandModuleEnv module detected");
		KeyValueResult keyValueResult = (KeyValueResult) result.results;
		List<Individual> results = new ArrayList<Individual>();
		// 2. Mapping of the results to specific OntClasses, OntProperties, and
		// Individuals:
		OntClass uEnvInfo = createClass("UnknownEnvironmentInformation");
		Individual uInd = uEnvInfo.createIndividual();
		addProperty(uInd, uEnvInfo, "environmentInformation",
				keyValueResult.results.get("fullOutput"));
		// 3. Map the results to general result:
		results.add(uInd);
		// 4. Map the general result to environment:
		mapToEnvironment(generalResult, environment, results,
				"environmentInformation", "information");
	}

	private OntClass createClass(String name) {
		return ontology.model.createClass(ontology.PET2LRM_NS + name);
	}

	private OntProperty createProperty(Resource domain, String name) {
		OntProperty property = ontology.model
				.createOntProperty(ontology.PET2LRM_NS + name);
		property.addDomain(domain);
		property.addRange(XSD.xstring);
		return property;
	}

	private OntProperty createProperty(Resource domain, Resource range,
			String name) {
		OntProperty property = ontology.model
				.createOntProperty(ontology.PET2LRM_NS + norm(name));
		property.addDomain(domain);
		property.addRange(range);
		property.addRange(XSD.xstring);
		return property;
	}

	private Individual createIndividual(OntClass resource, String name) {
		Individual individual = resource.createIndividual(ontology.PET2LRM_NS
				+ norm(name));
		individual.addProperty(LRM_static_schema.realizes, resource);
		return individual;
	}
	public static String norm(String name)
	{ 	
		if (org.apache.xerces.util.XMLChar.isValidName(name))
			return name;
		
		name = name.replaceAll(" ", "_");
		name = name.replaceAll("/", "_");
		name = name.replaceAll("\\\\", "_");
		name = name.replaceAll("\\(", "_");
		name = name.replaceAll("\\)", "_");
		//		name.replaceAll(":", "-");
		//		name.replaceAll("-", "_");
		if (!org.apache.xerces.util.XMLChar.isValidName(name))
			System.out.println("name:'"+name+"' is not valid XML name... fixme please");
		return name;
	}
	
	
	private void addProperty(Individual individual, OntClass ontClass,
			String property, String value) {
		if (value != null && !value.equals("")) {
			OntProperty ontProperty = createProperty(ontClass, property);
			individual.addProperty(ontProperty, value);
		}
	}

	private void mapToEnvironment(Individual generalResult,
			Individual environment, List<Individual> results,
			String resultRelation, String environmentRelation) {
		OntProperty resultProperty = createProperty(
				ontology.abstractExtractionResult, resultRelation);
		OntProperty environmentProperty = createProperty(
				ontology.abstractEnvironment,
				ontology.abstractExtractionResult, environmentRelation);
		for (Individual result : results) {
			generalResult.addProperty(resultProperty, result);
		}
		environment.addProperty(environmentProperty, generalResult);
	}
}
