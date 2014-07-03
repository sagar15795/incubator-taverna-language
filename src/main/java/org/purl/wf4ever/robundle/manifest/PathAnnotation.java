package org.purl.wf4ever.robundle.manifest;

import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = { "annotation", "about", "content" })
public class PathAnnotation {
    private List<URI> about = new ArrayList<>();
    private URI annotation;
    private URI content;

    @JsonIgnore
    public URI getAbout() {
        if (about.isEmpty()) {
        	return null;        
        } else { 
        	return about.get(0);
        }
    }

    @JsonIgnore
    public List<URI> getAboutList() {
    	return about;
    }
    
    @JsonProperty("about")
    public Object getAboutObject() {
    	if (about.isEmpty()) { 
    		return null;
    	}
    	if (about.size() == 1) { 
    		return about.get(0);
    	} else {
    		return about;
    	}
    }
    
    public URI getAnnotation() {
        return annotation;
    }

    public URI getContent() {
        return content;
    }

    public void setAbout(URI about) {
    	this.about.clear();
    	if (about != null) { 
    		this.about.add(about);
    	}
    }

    public void setAbout(List<URI> about) {
    	if (about == null) {
    		throw new NullPointerException("about list can't be null");
    	}
    	this.about = about;
    }
    
    public void setAnnotation(URI annotation) {
        this.annotation = annotation;
    }

    public void setContent(URI content) {
        this.content = content;
    }

    public void generateAnnotationId() {
        setAnnotation(URI.create("urn:uuid:" + UUID.randomUUID()));
    }

	public void setAbout(Path path) {
		setAbout(relativizePath(path));
	}

	private URI relativizePath(Path path) {		
		return URI.create("/.ro/").relativize(URI.create(path.toUri().getRawPath()));
	}

	public void setContent(Path path) {
		this.content = relativizePath(path);
		
	}
}
 