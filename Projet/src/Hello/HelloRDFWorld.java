package Hello;
import java.io.InputStream;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;

public class HelloRDFWorld {
	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		String fileLocation = "http://www.ema.org/ontologies/SportEquipesInfered";
		getOntologyModel("C:/Users/Robin/OneDrive/Ecole/Emacs/M2/Ontologies/SportEquipesInfered.owl");
		
		Resource ressource = model.createResource(fileLocation + "_res");
		Property property = model.createProperty(fileLocation + "_prop");
		
		ressource.addProperty(property, "Hello world", XSDDatatype.XSDstring);
		
		model.write(System.out, "Turtle");
		
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		InfModel in = ModelFactory.createInfModel(reasoner, model);
		

        String queryB = "SELECT ?player ?team WHERE { ?player <http://www.ema.org/ontologies/player#isPlayerOf>  ?team  }";
        String queryA = "SELECT ?player ?team WHERE { ?team <http://www.ema.org/ontologies/authors#hasPlayer>  ?player  }";
        
		performSPARQLQuery(model, queryA);
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
	        System.err.println("ERROR" + jenaException.getMessage());
	        jenaException.printStackTrace();
	        System.exit(0);
	    }
	    return ontoModel;
	}
	
	
}