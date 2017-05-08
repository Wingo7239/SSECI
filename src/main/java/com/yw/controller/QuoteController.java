package com.yw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yw.model.Quote;
import com.yw.repository.QuoteRepository;



@Controller
public class QuoteController {

	@Autowired
	private QuoteRepository quoteRepository;
	
	@RequestMapping(value="/create",method = RequestMethod.POST)
	@ResponseBody
	public String addQuote(@RequestParam("list") String jsonList){
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			HashMap<String, Object> tmp =  mapper.readValue(jsonList, HashMap.class);
			List<List<String>> quoteJsonList = (List<List<String>>) tmp.get("hq");
			List<Quote> quoteList = new ArrayList<Quote>();
			for(List<String> list : quoteJsonList){
				Quote quote = new Quote(Integer.parseInt(list.get(0)), Double.parseDouble(list.get(1)),Double.parseDouble(list.get(2)),Double.parseDouble(list.get(6)), Double.parseDouble(list.get(5)),Double.parseDouble(list.get(7)),Double.parseDouble(list.get(8)));
				quoteList.add(quote);
			}
			quoteRepository.save(quoteList);
			quoteRepository.flush();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Success";
	}
	
	
	
}
