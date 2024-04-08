# Ontology-Thesis
Ontology developed for master thesis, in `RDF/XML` to model the influence relationships between sleep disorders and diseases.

Full thesis article in Spanish at: https://repositorio.uniandes.edu.co/entities/publication/d6aab2d7-fb8e-4231-af35-6953a3a51ba8

## Context
The implementation of the new ontology was done in `RDF/XML` using `Protégé 5.5.0`.
The ontology domain refers to sleep disorders. However, this being a domain studied by several disciplines, it was defined as the domain of Medicine, which encompasses not only Sleep Disorders, but also other diseases or factors that can trigger or cause Sleep Disorders, without leaving aside that Sleep Disorders are the central focus of this ontology.
For the creation of the ontology there are a total of:
- 500 classes including the entire class hierarchy all the way up to the superclasses.

On the other hand, in terms of instances there are:
- 437 entities which correspond to the last level of the subclasses. This is due to the fact that each of the subclasses is instantiated with an individual with the same name. It is worth mentioning, as will be shown in the Integration chapter, that there are ontologies that do not handle instances, but instead handle more levels of subclasses. Therefore, as the idea is to expand the ontology, it was decided to reach this level, to handle the relationships between instances and to enrich with another ontology, other types of classes that are not contemplated in the new ontology. In other words, the other ontologies have classes that could be considered.

As for the properties there are a total of:
- 109 Object Properties.
- 109 Domain and Range relationships of the properties.
- 637 connections between instances by means of the aforementioned relationships that are of type Object Properties.
- 486 subclass relationships
- 609 Annotation Properties, where each of the Classes, Entities (with the same name as the last level of the subclasses) and properties have the annotation `rdfs:label`.
All classes, properties and entities in the ontology have a `URI` given as follows, followed by the name of the class/entity or property:
`http://www.semanticweb.org/daniel/ontologies/2020/6/SDCO#Nombre_ent`

The Bioportal folder contains JAVA classes for extracting bioportal classes and properties.

The ontology folder contains three files:
* SDCOv2.owl -> is the ontology development created to model the influence relationships between sleep disorders and diseases.
* sdco_snomed_integrated.owl -> contains the ontology created in SDCOv2.owl and the integration with certain classes extracted from the SCNOMED CT ontology hosted in Bioportal.
* SPARQL_query.rq -> SPARQL queries for ontology information extraction.

The deployment of the network can be performed in GraphDB.

## Ontology visualization
Here you will find different examples of the construction and visualization of the ontology created and the integration with SNOMED CT.
---
### Taxonomy of Sleep disorders
**Taxonomy of Sleep disorders in Protégé**  
![alt text](<png/1 - Sleep disorders texonomy.png>)

**Taxonomy of Sleep disorders visualized**  
![alt text](<png/1.1 - Sleep disorders texonomy.png>)
---
### Taxonomy of Medical
**Taxonomy of Medical classes**  
![alt text](<png/2 - Medical classes taxonomy.png>)

**Example of some medical classes taxonomy**  
![alt text](<png/2.1 - medical classes taxonomy example.png>)

**Examples of some medical classes taxonomy visualized**  
![alt text](<png/2.3 - medical classes taxonomy example.png>)
![alt text](<png/2.4 - medical classes taxonomy example.png>)
---
### Taxonomy of Diseases
**Taxonomy of Disease classes**  
![alt text](<png/3 - diseases classes taxonomy.png>)
![alt text](<png/3.1 - diseases classes taxonomy.png>)

**Taxonomy of Disease classes visualized**  
![alt text](<png/3.2 - diseases classes taxonomy.png>)
---
### Properties
**Properties**  
![alt text](<png/4 - properties.png>)

**Examples of properties**  
![alt text](<png/4.1 - properties.png>)
![alt text](<png/4.2 - properties.png>)
![alt text](<png/4.3 - properties.png>)
---
### Visualization of the new ontology
**Visualization of the new ontology**  
![alt text](<png/5 - new ontology.png>)
![alt text](<png/5.1 - new ontology.png>)
---
### Examples of fragments extracted from SNOMED CT
**Examples of fragments extracted from SNOMED CT via Bioportal**  
![alt text](<png/6 - snomed example.png>)
![alt text](<png/6.1 - snomed example.png>)
![alt text](<png/6.2 - snomed example.png>)
---
<!---**Ontology integration with SNOMED CT fragments using `owl2:sameAs`**  
![alt text](<png/7 - integration.png>)--->
### Integration of ontologies at class, property and instance level
**Ontology integration with SNOMED CT fragments using `owl2:equivalentClass`**  
![alt text](<png/7.1 - integration.png>)
![alt text](<png/7.2 - integration equivalentClass.png>)

![alt text](<png/7.3 - integration equivalentClass.png>)
![alt text](<png/7.4 - integration equivalentClass.png>)

**Ontology integration with SNOMED CT fragments using `owl2:equivalentProperty`**  
![alt text](<png/7.5 - integration equivalentProperty.png>)
![alt text](<png/7.6 - integration equivalentProperty.png>)

**Ontology integration with SNOMED CT fragments using `owl2:sameAs`**  
![alt text](<png/7.7 - integration sameAs.png>)
![alt text](<png/7.8 - integration sameAs.png>)

![alt text](<png/7.9 - integration sameAs.png>)
![alt text](<png/7.10 - integration sameAs.png>)
---
### Deployment using GraphDB
**Visualization in GraphDB**  
![alt text](<png/8 - graphdb.png>)
![alt text](<png/8.1 - graphdb.png>)
