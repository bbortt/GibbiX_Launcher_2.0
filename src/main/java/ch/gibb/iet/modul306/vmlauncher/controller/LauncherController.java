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
	
	private String VM_PATH = "D:/1_work";
	
	public Map<String, Map<String, String>> virtualMachines;
	
	public LauncherController() {
		//TODO: Eingabe eines Paths, falls Default Pfad nicht vorhanden!
		String path="D:/1_work";
		File f = new File(path);
		if (!(f.exists() && f.isDirectory())) {
		   path = "C:/Users/vmadmin/workspace/1_work";
		}
		this.VM_PATH = path;
		this.virtualMachines = getData();
	}
	
	private Map<String, Map<String,String>> getData(){
		try {
			Map<String, Map<String,String>> vmMap = new HashMap<String, Map<String,String>>();
			File[] maschines = getVirtualMaschineDirectories();
			
			for(File a : maschines){
				if(a.isDirectory()){
					vmMap.put(a.getName(), getVirtualMachine(a));
				}
			}
			
			return vmMap;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Map<String, String> getVirtualMachine(File direcory) throws IOException {
		
		
		String lineInfo;
		Map<String, String> map = new HashMap<String, String>();
		
		BufferedReader br = new BufferedReader(new FileReader(VM_PATH+"/"+direcory.getName()+"/"+direcory.getName()+".vmx"));
	    
    	while ((lineInfo = br.readLine()) != null) {
    		
    		if(!(lineInfo.contains("#")) && !(lineInfo.isEmpty())){
    			String[] val = lineInfo.split(" = ");
    			String[] val2 = val[1].split("\"");
    			if(val2.length == 2){
    				map.put(val[0], val2[1]);
    			}
    		}
		}
		return map;	
	}
	
	public File[] getVirtualMaschineDirectories(){
	    return new File(VM_PATH).listFiles();
	}
}
