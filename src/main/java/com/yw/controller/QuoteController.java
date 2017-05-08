package com.yw.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Controller
public class QuoteController {

	@Autowired
	private QuoteRepository quoteRepository;

	private static String[] TABLE_HEADER = { "date", "open", "close", "high", "low", "volume", "turnover" };

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public String addQuote(@RequestParam("list") String jsonList) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			HashMap<String, Object> tmp = mapper.readValue(jsonList, HashMap.class);
			List<List<String>> quoteJsonList = (List<List<String>>) tmp.get("hq");
			List<Quote> quoteList = new ArrayList<Quote>();
			for (List<String> list : quoteJsonList) {
				Quote quote = new Quote(Integer.parseInt(list.get(0)), Double.parseDouble(list.get(1)),
						Double.parseDouble(list.get(2)), Double.parseDouble(list.get(6)),
						Double.parseDouble(list.get(5)), Double.parseDouble(list.get(7)),
						Double.parseDouble(list.get(8)));
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

	@RequestMapping(value = "/quote/download", method = RequestMethod.GET)
	public String getQuote(HttpServletRequest request, HttpServletResponse response) {
		List<Quote> quoteList = quoteRepository.findByDateInterval(Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("end")));
		
		try {
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet sheet = book.createSheet("Sheet1", 0);

			for (int i = 0; i < TABLE_HEADER.length; i++) {
				sheet.addCell(new Label(i, 0, TABLE_HEADER[i]));
			}
			for (Quote quote : quoteList) {
				int row = 1;
				for (int i = 0; i < TABLE_HEADER.length; i++) {
					sheet.addCell(new Label(i, row, quote.getDate() + ""));
					sheet.addCell(new Label(i, row, round2Decimal(quote.getOpen())));
					sheet.addCell(new Label(i, row, round2Decimal(quote.getClose())));
					sheet.addCell(new Label(i, row, round2Decimal(quote.getHigh())));
					sheet.addCell(new Label(i, row, round2Decimal(quote.getLow())));
					sheet.addCell(new Label(i, row, round2Decimal(quote.getVolume())));
					sheet.addCell(new Label(i, row, round2Decimal(quote.getTurnover())));
				}
			}
			
			book.write();

		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return "success";
	}
	
	private static String round2Decimal(Double num){
		return String.format("%.02f", num);
	}

}
