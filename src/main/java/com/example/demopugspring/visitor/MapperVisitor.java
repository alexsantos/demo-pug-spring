package com.example.demopugspring.visitor;

import java.util.StringTokenizer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.Field;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.MessageVisitor;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Type;


/**
 * This class it's the main class for the visitor implementation, given a path
 * it creates the indexs and it's responsable for the route until reaching the
 * primitive that was suppose to achieve.
 * 
 * While implementing new visitors you can extend this function and overide the
 * desired function for example, if you want to change Primitives you can extend
 * this function and then Override the visit Primitive function to get the
 * specific functionality.
 * 
 */

public class MapperVisitor implements MessageVisitor{

	private boolean subComponent = false;
	private String value;
	private String segment;
	private Integer fieldNumber;
	private Integer fieldRepetition;
	private Integer componentNumber;
	private Integer subComponentNumber;
	private Integer segmentRepetition;
	
	
	public MapperVisitor(String path, String value) throws HL7Exception
	{
		int[] indices = getIndexs(path);
		this.value = value;
		this.segment = path.split("-")[0];
		this.segmentRepetition = indices[0];
		this.fieldRepetition = indices[1];
		this.fieldNumber = indices[2];
		this.componentNumber = indices[3];
		this.subComponentNumber = indices[4];
		
	}
	
	
	@Override
	public boolean start(Message message) throws HL7Exception {
		StringTokenizer tok = new StringTokenizer(this.segment, "/", false);
		Structure[] segments = (Structure[]) message.getAll(tok.nextToken());
		
		if(segmentRepetition >= 0) {
			segments[segmentRepetition].accept(this, Location.UNKNOWN);
		}
		else {
			for(Structure segment : segments) {
				segment.accept(this, Location.UNKNOWN);
			}
		}
		return false;
	}

	@Override
	public boolean end(Message message) throws HL7Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean start(Group group, Location location) throws HL7Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean end(Group group, Location location) throws HL7Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean start(Segment segment, Location location) throws HL7Exception {
		if(fieldNumber > 0) {
			getField(segment, fieldNumber, location);
		}
		else {
			for(int i = 1; i <= segment.numFields(); i++) {
				getField(segment, i, location);
			}
		}
		
		return false;
	}
	
	
	private void getField(Segment segment, int fieldNumber, Location location) throws HL7Exception {
		Type[] fields;
		
		fields = segment.getField(fieldNumber);
		if(fieldRepetition >= 0){
			accessField(fields[fieldRepetition], location);
		}
		else {
			for(Type field : fields){
				accessField(field, location);
			}
		}
	}
	
	private void accessField(Type type, Location location) throws HL7Exception {
		if(type.isEmpty() && type instanceof Composite) {
			((Composite) type).accept(this, location);
		}
		else {
			type.accept(this,location);
		}
	}
	

	@Override
	public boolean end(Segment segment, Location location) throws HL7Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean start(Field field, Location location) throws HL7Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean end(Field field, Location location) throws HL7Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean start(Composite type, Location location) throws HL7Exception {
		Type[] types = type.getComponents(); 
		
		if(subComponent){
			visitComposites(types, subComponentNumber, location);
		}
		else {
			subComponent = true;
			visitComposites(types, componentNumber, location);
			
		}
		return false;
	}
	
	private void visitComposites(Type[] types, Integer componentNumber, Location location) throws HL7Exception
	{
		if(componentNumber >= 1){
			types[componentNumber - 1].accept(this, location);
		}
		else {
			for(Type  componentType : types) {
				componentType.accept(this, location);
			}
		}
	}

	@Override
	public boolean end(Composite type, Location location) throws HL7Exception {
		noSubComponent();
		return false;
	}
	
	/**
	 * This function it's used to check if the access is for a subcomponet or
	 * not. Because it's done only at the end of visiting a Composite it the
	 * false value
	 */
	public void noSubComponent() {
		subComponent = false;
	}
	
	@Override
	public boolean visit(Primitive type, Location location) throws HL7Exception {
		type.setValue(this.getValue());
		return false;
	}

	
	/**
	 * Probably this function could be better, but the mission of this function
	 * it's with a given path retrieve each index of the tree, if the position
	 * has a wildcard (#) it will perform all repetitions of that element,
	 * example PID(#)-3-1 it will go for each PID to the position 3-1
	 * 
	 * @param spec
	 *            - the path for a resource
	 * @return
	 * @throws HL7Exception
	 */
	private  int[] getIndexs(String spec) throws HL7Exception {
		String nextToken ;
		int[] indexs = new int[5];
        StringTokenizer tok = new StringTokenizer(spec, "-", false);
        StringTokenizer specRep = new StringTokenizer(tok.nextToken(), "()", false);
        try {
        	specRep.nextToken();
        	if(specRep.hasMoreTokens())
		    {
        		nextToken = specRep.nextToken();
		    	indexs[0] = (nextToken.equals("#"))? -1 :Integer.parseInt(nextToken);  
		    }else
		    {
		    	indexs[0] = 0;
		    }
        	
		    if (!tok.hasMoreTokens())
		        throw new HL7Exception("Must specify field in spec " + spec);
		    
		    nextToken = tok.nextToken();
            StringTokenizer fieldSpec = new StringTokenizer(nextToken, "()", false);
            
            nextToken = fieldSpec.nextToken();
            indexs[2] = (nextToken.equals("#"))? -1 : Integer.parseInt(nextToken);
            
            if(fieldSpec.hasMoreTokens())
            {
            	nextToken = fieldSpec.nextToken(); 
            	indexs[1] = (nextToken.equals("#"))? -1 :Integer.parseInt(nextToken);  
            }else
            {
            	indexs[1] = 0;
            }
            
                       
            for(int i = 3; i < indexs.length; i++){
            	if(tok.hasMoreTokens()) {
            		nextToken = tok.nextToken();
            		indexs[i] = (nextToken.equals("#"))? -1 : Integer.parseInt(nextToken);
            	}
            	else {
            		indexs[i] = 1;
            	}
            }
            return indexs;
        } catch (NumberFormatException e) {
            throw new HL7Exception("Invalid integer in spec " + spec);
        }
    }


	public String getValue() {
		return value;
	}
}
