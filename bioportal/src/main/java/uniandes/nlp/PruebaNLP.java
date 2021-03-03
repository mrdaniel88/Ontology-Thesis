package uniandes.nlp;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.Quadruple;

public class PruebaNLP {
	public static void main(String[] args) { 
        // Create a document. No computation is done yet.
        Document doc = new Document("Elderly individuals, particularly those with cognitive impairment, are oftentimes restless during the night, and this increases the distress of relatives, professional carers and themselves. A number of conditions other than dementia need to be considered from nyctophobia and insomnophobia, to pain, specific motor disorders during sleep, parasomnias as REM-sleep behaviour disorder, dipping, hypoglycemia, withdrawal or excessive tea, coffee and alcohol consumption. A clear-cut differential diagnosis between dementia and delirium is not always possible, as dementia is the major risk factor for confusional states decreasing the vulnerability by anticholinergic medication and any other disruptive factor, biological or psychological. Treatment of nocturnal agitation usually requires (1) reassurance and re-orientation; (2a) the discontinuation of anticholinergic substances; (2b) symptomatic psychotropic intervention; and (3) causal treatment of underlying problems. Benzodiazepines should only be used at the lowest necessary dosage for the shortest possible time, particularly in individuals who are already benzodiazepine-dependent. Quetiapine or mirtazapine at low dosages can be employed in patients with psychotic or depressive symptoms. Melatonin and its derivatives hold promise for chronic circadian rhythm disorders. There is no \\\"one fits all\\\" recipe for this notorious problem and each case needs to be examined individually.");
        Document doc2 = new Document("In the attentive waking state, pleasant odours often prolong inhalation while unpleasant odours often shorten the exhalation. It should be checked whether this induced breathing pattern is maintained even during sleep.");
        // doc = doc2;
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            // We're only asking for words -- no need to load any models yet
            //System.out.println("The second word of the sentence '" + sent + "' is " + sent.word(1));
            // When we ask for the lemma, it will load and run the part of speech tagger
            //System.out.println("The third lemma of the sentence '" + sent + "' is " + sent.lemma(2));
            // When we ask for the parse, it will load and run the parser
            //System.out.println("The parse of the sentence '" + sent + "' is " + sent.parse());
            // ...
        	System.out.println(sent.toString());
            for (Quadruple<String, String, String, Double> triple : sent.openie()) {
            	System.out.println(triple.first() + " -> " + triple.second() + " -> " + triple.third()+ " -- Confidence: "+ triple.fourth() );
            }
            
            for (RelationTriple triple : sent.openieTriples()) {
                // Print the triple
                System.out.println(triple.confidence + "\t" +
                    triple.subjectLemmaGloss() + "\t" +
                    triple.relationLemmaGloss() + "\t" +
                    triple.objectLemmaGloss());
              }
            System.out.println("**********************************************************");
        }
    }
}
