package ch.gibb.iet.modul306.vmlauncher.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ch.gibb.iet.modul306.vmlauncher.app.Application;
import ch.gibb.iet.modul306.vmlauncher.model.vmModel;

public class LauncherController extends AbstractController {
	
	private static final Logger LOGGER = LogManager.getLogger(Application.class);
	
	public static final String VM_PATH = "C:/Users/vmadmin/workspace/1_work";
	
	public List<vmModel> vmModels;
	
	public LauncherController() {
		try {
			List<vmModel> models = new ArrayList<vmModel>();
			File[] maschines = getVirtualMaschineDirectories();
			
			for(File a : maschines){
				if(a.isDirectory()){
					vmModel vmModel = getVirtualMachine(a);
					models.add(vmModel);
				}
			}
			
			this.vmModels = models;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private vmModel getVirtualMachine(File direcory) throws IOException {
		
		
		String lineInfo;
		Map<String, String> map = new HashMap<String, String>();
		
		BufferedReader br = new BufferedReader(new FileReader(VM_PATH+"/"+direcory.getName()+"/"+direcory.getName()+".vmx"));
	    
    	while ((lineInfo = br.readLine()) != null) {
    		
    		if(!(lineInfo.contains("#")) && !(lineInfo.isEmpty())){
    			String[] val = lineInfo.split("=");
    			map.put(val[0], val[1]);
    		}
		}
    	
    	LOGGER.info("IN: "+ map.size());
		
		return null;	
	}
	
	public File[] getVirtualMaschineDirectories(){
	    return new File(VM_PATH).listFiles();
	}
}
