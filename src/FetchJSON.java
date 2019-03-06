import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class FetchJSON {
	
	public void parseJSON(String file){
        JSONParser jsonParser = new JSONParser();
        
        try(FileReader reader = new FileReader(file)){
        	//Read json file
        	Object obj = jsonParser.parse(reader);
        	        	

        	readJSONImages(obj);
        	
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Read content from the array images
	private void readJSONImages(Object obj) {
		//Create jsonobject
		
		ArrayList<String> file_name = new ArrayList<>();
		ArrayList<Long> id = new ArrayList<>();
		
		JSONObject imageObj = (JSONObject) obj;

		JSONArray imageArray = (JSONArray) imageObj.get("images");
		
		for(int i = 0; i < imageArray.size() ; i++) {
			JSONObject jsonObj = (JSONObject)imageArray.get(i);
			
			
			file_name.add((String)jsonObj.get("file_name"));
			id.add((Long) jsonObj.get("id"));
		}
		
		createJSONImages(file_name, id, obj);
		
	}
	
	//Create array and store the corresponding objects
	private void createJSONImages(ArrayList<String> file_name, ArrayList<Long> id, Object obj) {
		
		JSONArray imageArray = new JSONArray();
		
		for(int i = 0; i < file_name.size(); i++) {
			JSONObject content = new JSONObject();
			
			content.put("file_name",file_name.get(i));
			content.put("id",(Long)id.get(i));
			
			imageArray.add(content); 
			
		}
		
				
		JSONObject imgesObject = new JSONObject();
		imgesObject.put("images", imageArray);
		
		readJSONAnnotations(obj, imgesObject);
	}
	
	//Read content from the array annotations
	private void readJSONAnnotations(Object obj, JSONObject imgesObject) {

		
		ArrayList<Long> image_id = new ArrayList<>();
		ArrayList<String> caption = new ArrayList<>();
		
		JSONObject imageObj = (JSONObject) obj;

		JSONArray annotationArray = (JSONArray) imageObj.get("annotations");
		
		for(int i = 0; i < annotationArray.size(); i++) {
			JSONObject jsonObj = (JSONObject)annotationArray.get(i);
					
			image_id.add((Long)jsonObj.get("image_id"));
			caption.add((String) jsonObj.get("caption"));
		}
		
		createJSONAnnotations(image_id, caption, imgesObject);
		}
	
	
	//Create array and store the corresponding objects
	private void createJSONAnnotations(ArrayList<Long> image_id, ArrayList<String> caption, JSONObject imgesObject) {
		
		JSONArray annotationArray = new JSONArray();
		
		for(int i = 0; i < image_id.size(); i++) {
			JSONObject content = new JSONObject();
			
			content.put("caption", caption.get(i));
			content.put("image_id", (Long)image_id.get(i));
		
			annotationArray.add(content); 
		
		}

		JSONObject annotationsObject = new JSONObject();
		annotationsObject.put("annotations", annotationArray);
						 
		//Contac annotaionsObject with imagesObject
		imgesObject.putAll(annotationsObject);
		
	
		try (FileWriter file = new FileWriter("cap_train2014.json")){
			//file.write(images.toJSONString());
			file.write(imgesObject.toJSONString());
			file.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}











