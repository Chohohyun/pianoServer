package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;


import com.example.score.PlayScore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.score.PlayMusic;
import com.example.score.PlayRecord;

import org.json.*;

@RestController
public class MyController {

	@RequestMapping(value ="/piano/score/a", method=RequestMethod.POST)
	public String score(@RequestBody String map) {
		JSONObject obj = new JSONObject(map);
		List<Double> playList = new ArrayList<>();
		List<Double> originList = new ArrayList<>();
		String pageName = null;
		String originData = null;
		JSONObject playMusic = (JSONObject) obj.get("music");
		//JSONObject originalMusic = (JSONObject) obj.get("music2");
		ObjectMapper om = new ObjectMapper();
		/*for(int i = 1 ; i < playMusic.length()+1;  i++) {
			if (i<10) {
				//pageName = obj.getJSONObject("music").getString("0:00:0"+i);
				//originData= obj.getJSONObject("music2").getString("0:00:0"+i);
				pageName=playMusic.getString("0:00:0"+i);
				originData=originalMusic.getString("0:00:0"+i);
			}
			else {
				pageName = playMusic.getString("0:00:"+i);
				originData = originalMusic.getString("0:00:"+i);
			}
			if (pageName == null) {
				break;
			}
			else {
				arrayList.add(Integer.parseInt(pageName));            
			}
			if (originData == null) {
				break;
			}
			else {
				originList.add(Integer.parseInt(originData));            
			}
		}*/
		/*for(int i = 1 ; i < originalMusic.length()+1;  i++) {
			if (i<10) {
				originData=originalMusic.getString("0:00:"+i);
			}
			else if(i<100){
				originData = originalMusic.getString("0:0"+i/10+":"+i%10);
			}
			else if(i<600) {
				originData = originalMusic.getString("0:"+i/10+":"+i%10);
			}
			if (originData == null) {
				break;
			}
			else {
				originList.add(Integer.parseInt(originData));            
			}
		}*/
		for(int i = 1 ; i < playMusic.length()+1;  i++) {
			/*if (i<10) {
				pageName=playMusic.getString("0:00:"+i);
			}
			else if(i<100){

			pageName = playMusic.getString("0:0"+i/10+":"+i%10);
			}
			else if(i<600) {
				pageName = playMusic.getString("0:"+i/10+":"+i%10);
			}*/
			pageName=playMusic.getString(i+"");
			if (pageName == null) {
				break;
			}
			else {
				playList.add(Double.parseDouble(pageName));     
				originList.add(1.0);
			}
		}

		float total=0;
		float success=0;
		int fast=0;
		int slow=0;
		System.out.println(playList);

		System.out.println(originList);


		try{for (int i = 0; i < originList.size(); i++) {
			if(originList.get(i)!=0) {
				total+=1;
				if(originList.get(i)==playList.get(i)) {
					success+=1;
				}
				else if(originList.get(i)==playList.get(i)||originList.get(i)==playList.get(i-1)||originList.get(i)==playList.get(i-2)||originList.get(i)==playList.get(i-3)) {
					success+=0.8;
					fast+=1;
				}

				else if(originList.get(i)==playList.get(i)||originList.get(i)==playList.get(i+1)||originList.get(i)==playList.get(i+2)||originList.get(i)==playList.get(i+3)) {
					success+=0.8;
					slow+=1;
				}
				else if(originList.get(i)==playList.get(i)||originList.get(i)==playList.get(i-4)||originList.get(i)==playList.get(i-5)) {
					success+=0.5;
					fast+=1.3;
				}
				else if(originList.get(i)==playList.get(i)||originList.get(i)==playList.get(i+4)||originList.get(i)==playList.get(i+5)) {
					success+=0.5;
					slow+=1.3;
				}
			}
		}
		}catch (Exception e) {
			// TODO: handle exception
		}

		String result=null;
		if(fast>slow) {
			result = "박자가 전반적으로 빠릅니다.";
		}
		else if(fast<slow){
			result = "박자가 전반적으로 느립니다.";
			
		}
		else {
			if((success/total)*100==100) {
				result="박자가 다 맞습니다.";
			}
			else{
				result = "박자가 고르게 틀리고 있습니다.";
			}
		}
		String output= null;
		try {
			//output = om.writeValueAsString(resultMap);
			output = om.writeValueAsString(result+(success/total)*100 + "%");
		} catch (Exception e) {

		}
		return output;
	}

	@RequestMapping(value="/piano/play", method=RequestMethod.POST)
	public String paly(@RequestBody Map<String, Object> map) {
		PlayMusic pm = new PlayMusic();
		return pm.play(map);
	}

	@RequestMapping(value="/piano/record", method=RequestMethod.POST)
	public String record(@RequestBody Map<String, Object> map) {
		PlayRecord pr = new PlayRecord();
		return pr.record(map);
	}
	
	@RequestMapping(value="/piano/test", method=RequestMethod.GET)
	public String test() {
		return "hello";
	}
}
