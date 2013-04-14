package com.example.beatmakingapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.content.Context;

public class ProjectManager {

	String name;
	String createdBy;
	Date dt;
	Track track;
	
	public ProjectManager() {
		name = new String();
		createdBy = new String();
		dt = Calendar.getInstance().getTime();
		track = new Track();
	}

	public ProjectManager(String name, String createdBy, Date dt, Track track) {
		this.name = name;
		this.createdBy = createdBy;
		this.dt = dt;
		this.track = track;
	}

	public void createProject(Context ctxt){
		
		try {

			File projectDir = ctxt.getDir("Project1",Context.MODE_PRIVATE);
			File myFile = new File(projectDir, "ProjectDesc");
			File patternDir = new File(projectDir, "Patterns");
			patternDir.mkdir();
			File trackDir = new File(projectDir, "Track");
			trackDir.mkdir();
			
			
			writeProjectFileContents(myFile);
			
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	private void writeProjectFileContents(File fptr ) {
		StreamResult result = null;

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Project");
			doc.appendChild(rootElement);
			
			Element name = doc.createElement("Name");
			name.appendChild(doc.createTextNode(this.name));
			rootElement.appendChild(name);

			Element createdBy = doc.createElement("CreatedBy");
			createdBy.appendChild(doc.createTextNode(this.createdBy));
			rootElement.appendChild(createdBy);

			Element date = doc.createElement("Date");
			Date todaysDate = Calendar.getInstance().getTime();
			date.appendChild(doc.createTextNode(todaysDate.toString()));
			rootElement.appendChild(date);
			
			
			
			ArrayList<Pattern> patternNames = track.getPatterns();
			
			Element patterns = doc.createElement("Patterns");
			
			Element tempPattern = null;
			for(int i=0;i<patternNames.size();i++){
				tempPattern = doc.createElement("Pattern");
				
				Element patternName =  doc.createElement("Name");
				patternName.appendChild(doc.createTextNode(patternNames.get(i).getPatternName()));
				tempPattern.appendChild(patternName);
				
				Element patternTempo =  doc.createElement("Tempo");
				patternTempo.appendChild(doc.createTextNode(""+patternNames.get(i).getTempo()));
				tempPattern.appendChild(patternTempo);
				
				Element patternBars =  doc.createElement("Bars");
				patternBars.appendChild(doc.createTextNode(""+patternNames.get(i).getBars()));
				tempPattern.appendChild(patternBars);
				
				
				
				patterns.appendChild(tempPattern);
			
			}

			Element track = doc.createElement("Track");
			//Date todaysDate = Calendar.getInstance().getTime();
			
			
			Element trackName =  doc.createElement("TrackName");
			trackName.appendChild(doc.createTextNode(this.track.getName()));
			
			track.appendChild(trackName);
			Element trackLength =  doc.createElement("TrackLength");
			trackLength.appendChild(doc.createTextNode(""+this.track.getLength()));
			
			track.appendChild(trackLength);
			
			track.appendChild(patterns);
			
			rootElement.appendChild(track);
			//rootElement.appendChild(patterns);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);

			result = new StreamResult(fptr);
			transformer.transform(source, result);
			
			//System.out.println("File saved!");
		} catch (Exception ex) {
			ex.printStackTrace();

		}


	}


}
