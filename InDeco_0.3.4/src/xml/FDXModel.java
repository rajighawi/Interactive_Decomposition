package xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import fd.Attribute;
import fd.AttributeSet;
import fd.Decomposition;
import fd.FD;
import fd.FDSet;
import fd.Relation;
import oracle.xml.parser.v2.XMLDocument;

public class FDXModel {

	XMLDocument doc;
	Element root;
		
	public FDXModel(Relation r){
		doc = new XMLDocument();
		doc.setEncoding("UTF-8");
		root = addXMLElement(doc, "relation");		
		root.setAttribute("name", r.getName());
		Element attEl = addXMLElement(root, "attributes");
		Element fdSetsEl = addXMLElement(root, "fdsets");
		Element decosEl = addXMLElement(root, "decompositions");
		
		AttributeSet as = r.getAttributes();
		addAttSet(attEl, as);
		
		ArrayList<FDSet> fdSets = r.getFdSets();
		
		addFDSets(fdSetsEl, fdSets);
		
		ArrayList<Decomposition> decos = r.getDecompositions();
		for (int i = 0; i < decos.size(); i++) {
			Decomposition deco = decos.get(i);
			Element decoEl = addXMLElement(decosEl, "decomposition");
			decoEl.setAttribute("name", deco.getName());
			System.out.println("Writing Decomposition `"+deco.getName()+"`");
			FDSet decoFDSet = deco.getFDSet();
			decoEl.setAttribute("fdset", decoFDSet.getName());
			System.out.println("  its fdset is `"+decoFDSet.getName()+"`");
			
			ArrayList<Relation> subrelations = deco.getSubrelations();
			for (int j = 0; j < subrelations.size(); j++) {
				Relation sub = subrelations.get(j);
				AttributeSet atts = sub.getAttributes();
				Element relEl = addXMLElement(decoEl, "subrelation");
				relEl.setAttribute("name", sub.getName());
				
				Element subAttsEl   = addXMLElement(relEl, "attributes");				
				addAttSet(subAttsEl, atts);
				
				Element subFDSetsEl = addXMLElement(relEl, "fdsets");				
				addFDSets(subFDSetsEl, sub.getFdSets());				
			}
		}
	}
	
	public void addFDSets(Node parent, ArrayList<FDSet> fdSets){	
		for (int i = 0; i < fdSets.size(); i++) {
			FDSet fdset = fdSets.get(i);
			addFDSet(parent, fdset);
		}
	}
	
	public void addFDSet(Node parent, FDSet fdset){
		Element fdsEl = addXMLElement(parent, "fdset");
		fdsEl.setAttribute("name", fdset.getName());
		
		for(FD fd:fdset){
			Element fdEl = addXMLElement(fdsEl, "fd");
			Element lhsEl = addXMLElement(fdEl, "lhs");
			Element rhsEl = addXMLElement(fdEl, "rhs");
			addAttSet(lhsEl, fd.getLHS());
			addAttSet(rhsEl, fd.getRHS());
		}
	}
	
	public void addAttSet(Node parent, AttributeSet as){
		Element el = addXMLElement(parent, "attset");
		for(Attribute a:as){
			Element e = addXMLElement(el, "att");
			e.setAttribute("name", a.getName());
		}
	}
	
	//-------------------------------------------------------------------
	
	public Element addXMLElement(Node parent, String tagname){
		Element el = doc.createElement(tagname);// new XMLElement(tagname);
		parent.appendChild(el);
		return el;
	}
	
	public void addCDATA(Node parent, String content){
		CDATASection el = doc.createCDATASection(content); 
		parent.appendChild(el);
	}
	
	public void addXMLText(Node parent,String value){
		Text te = doc.createTextNode(value);//new XMLText(value);
		parent.appendChild(te);
	}
	
	public String toString(){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try{
			doc.print(pw);
		} catch(IOException e){
			
		}
		return sw.toString();		
	}
	
	public void toFile(File file){		
		try {
			FileOutputStream out = new FileOutputStream(file);
			doc.print(out, "UTF-8");
			out.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	//---------------------------------------------------------
	
}

