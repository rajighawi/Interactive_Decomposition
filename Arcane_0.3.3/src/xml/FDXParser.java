package xml;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import fd.Attribute;
import fd.AttributeSet;
import fd.Decomposition;
import fd.FD;
import fd.FDSet;
import fd.Relation;

public class FDXParser {

	@SuppressWarnings("unchecked")
	public static Relation parse(File xmlFile){
		//File xmlFile = new File(filename);
		try { 
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(xmlFile);				    
			Element root = document.getRootElement();
			String relname = root.getAttributeValue("name");
			Element attsElem = root.getChild("attributes");
			Element fdssElem = root.getChild("fdsets");
			Element decosElem = root.getChild("decompositions");

			AttributeSet attributes = getAttSet(attsElem);
			Relation r = new Relation(relname, attributes); 
			ArrayList<FDSet> fdSets = getFDSets(fdssElem); //new ArrayList<FDSet>();
			r.setFdSets(fdSets);
			Map<String, FDSet> map = new HashMap<String, FDSet>();
			for (int i = 0; i < fdSets.size(); i++) {
				FDSet f = fdSets.get(i);
				map.put(f.getName(), f);
			}
			if(decosElem!=null){
				List<Element> chldrn = decosElem.getChildren("decomposition");
				for (int i = 0; i < chldrn.size(); i++) {
					Element decoElem  = (Element) chldrn.get(i);
					String decoName  = decoElem.getAttributeValue("name");
					String decoFDSetName = decoElem.getAttributeValue("fdset");
					if(decoFDSetName == null){
						throw new Exception("Error with decomposition: `"+decoName+"`: FDSet is null!");
					} else if(!map.containsKey(decoFDSetName)){
						throw new Exception("Error with decomposition: `"+decoName+"`: FDSet name `"+decoFDSetName+"` does no exist!");
					} else {
						FDSet decoFDSet = map.get(decoFDSetName);		    	
						Decomposition deco = new Decomposition(r, decoFDSet, decoName);	
						System.out.println("Decomposition `"+deco.getName()+
								"` is added to relation `"+deco.getRelation().getName()+"`, whose FDSet is `"+deco.getFDSet().getName()+"`." );
						List<Element> subsEls = decoElem.getChildren("subrelation");
						for (int j = 0; j < subsEls.size(); j++) {
							Element subElem  = (Element) subsEls.get(j);
							String subname = subElem.getAttributeValue("name");
							Element subAttsElem = subElem.getChild("attributes");
							Element subFdssElem = subElem.getChild("fdsets");				    
							AttributeSet subAttributes = getAttSet(subAttsElem);
							Relation sub = new Relation(subname, subAttributes); 
							ArrayList<FDSet> subFdSets = getFDSets(subFdssElem); 
							sub.setFdSets(subFdSets);				    
							deco.add(sub);
						}
						r.addDecomposition(deco);
					} 							    	
				}
			}
			return r;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (JDOMException je) {
			je.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public static ArrayList<FDSet> getFDSets(Element parent){
		ArrayList<FDSet> fdSets = new ArrayList<FDSet>();
		List<Element> chldrn = parent.getChildren("fdset");
		for (int i = 0; i < chldrn.size(); i++) {
			Element fdsetElem  = (Element) chldrn.get(i);
			String fdsetName = fdsetElem.getAttributeValue("name");
			FDSet fdSet = new FDSet(fdsetName);
			List<Element> fdChildren = fdsetElem.getChildren("fd");
			for (int j = 0; j < fdChildren.size(); j++) {
				Element fdE = fdChildren.get(j);
				Element lhsElem = fdE.getChild("lhs");
				AttributeSet lhs = getAttSet(lhsElem);
				Element rhsElem = fdE.getChild("rhs");
				AttributeSet rhs = getAttSet(rhsElem);
				FD fd = new FD(lhs, rhs); 
				fdSet.add(fd);
			}
			fdSets.add(fdSet);		    	
		}
		return fdSets;
	}

	@SuppressWarnings("unchecked")
	public static AttributeSet getAttSet(Element parent){
		Element attSetEl = parent.getChild("attset");
		AttributeSet as = new AttributeSet();
		List<Element> chldrn = attSetEl.getChildren("att");
		for (int i = 0; i < chldrn.size(); i++) {
			Element attElem = (Element) chldrn.get(i);
			String  attName = attElem.getAttributeValue("name");
			Attribute a = new Attribute(attName);
			as.add(a);
		}
		return as;
	}

}


