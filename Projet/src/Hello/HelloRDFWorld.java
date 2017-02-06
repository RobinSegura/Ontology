package Hello;
import java.io.InputStream;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;

public class HelloRDFWorld {
	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		String folderLocation = "C:/Users/Robin/OneDrive/Ecole/Emacs/M2/Ontologies";
		String owlLocation = folderLocation+"/SportEquipesInfered.owl";
		String data_file = folderLocation+"/data2.nt";
		
		String ontoAddress = "PREFIX sportEquipes: <http://www.ema.org/ontologies/SportEquipes#> PREFIX sportEquipesInfered: <http://www.ema.org/ontologies/SportEquipesInfered#>";
		String reqHead = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>";
		reqHead += ontoAddress;
		
		model = importData(data_file, owlLocation);

		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		InfModel in = ModelFactory.createInfModel(reasoner, model);
		
        String queryB = "SELECT ?Player ?Sport WHERE { ?Player sportEquipes:hasSport  ?Sport }";
        String queryA = "SELECT ?Player ?Sport WHERE { ?Sport sportEquipes:isSportOf ?Player }";
        String queryC = "SELECT ?subject ?object WHERE { ?subject rdfs:subClassOf ?object }";
        
		performSPARQLQuery(in, reqHead+queryB);
	}	
	
	private static Model importData(String data_file, String uri) {
		Model model = RDFDataMgr.loadModel(uri);
        RDFDataMgr.read(model, data_file);
		return model;
	}
	
	public static void performSPARQLQuery(Model model, String queryString){
		System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        }
	}

	public static OntModel getOntologyModel(String ontoFile){   
	    OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
	    try {
	        InputStream in = FileManager.get().open(ontoFile);
	        try {
	            ontoModel.read(in, null);
	        } 
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        System.out.println("LOG : Ontology " + ontoFile + " loaded.");
	    } 
	    catch (JenaException jenaException) {
	        jenaException.printStackTrace();
	        System.exit(0);
	    }
	    return ontoModel;
	}
	
	
}