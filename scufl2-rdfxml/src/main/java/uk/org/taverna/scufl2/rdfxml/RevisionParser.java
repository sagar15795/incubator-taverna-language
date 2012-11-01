package uk.org.taverna.scufl2.rdfxml;

import java.io.InputStream;
import java.net.URI;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.purl.wf4ever.roevo.jaxb.RoEvoDocument;
import org.purl.wf4ever.roevo.jaxb.VersionableResource;

import uk.org.taverna.scufl2.api.annotation.Revision;
import uk.org.taverna.scufl2.api.io.ReaderException;

public class RevisionParser {

	private JAXBContext jaxbContext;

	protected JAXBContext getJaxbContext() throws JAXBException {
		if (jaxbContext == null) {
			Class<?>[] packages = { 
					org.purl.wf4ever.roevo.jaxb.ObjectFactory.class,
					org.w3.prov.jaxb.ObjectFactory.class,
					org.w3._1999._02._22_rdf_syntax_ns.ObjectFactory.class,
					org.w3._2000._01.rdf_schema.ObjectFactory.class };
			jaxbContext = JAXBContext.newInstance(packages);
		}
		return jaxbContext;
	}

	@SuppressWarnings({ "unchecked", "unchecked" })
	public Revision readRevisionChain(InputStream revisionDocumentStream, URI base) throws ReaderException {
		JAXBElement<RoEvoDocument> roEvoDoc;
		try {
			Unmarshaller unmarshaller = getJaxbContext().createUnmarshaller();
			roEvoDoc = (JAXBElement<RoEvoDocument>) unmarshaller.unmarshal(revisionDocumentStream);
		} catch (JAXBException e) {
			throw new ReaderException(e);
		}

		RoEvoDocument document = roEvoDoc.getValue();
		if (document.getBase() != null) {
			base = base.resolve(document.getBase());
		}
		VersionableResource verResource = (VersionableResource) document.getAny().get(0);
		return parse(base, verResource);
	}

	private Revision parse(URI base, VersionableResource verResource) {
		Revision revision = new Revision();
		revision.setIdentifier(base.resolve(verResource.getAbout()));		
		return revision;
	}
}