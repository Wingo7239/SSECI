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
import com.yw.exception.InvalidDateException;
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
	@ResponseBody
	public void getQuote(HttpServletRequest request, HttpServletResponse response) {
		int start = 0;
		int end = 0;

		try {
			start = Integer.parseInt(request.getParameter("start"));
			end = Integer.parseInt(request.getParameter("end"));
			if (start < 19901220 || start > 20170505 || end < 19901220 || end > 20170505)
				throw new InvalidDateException("Please enter a valid date");
		} catch (NumberFormatException e) {
			throw new InvalidDateException("Invalid Date Format");
		}

		List<Quote> quoteList = quoteRepository.findByDateInterval(start, end);
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + start + "-" + end + ".xls");

		try {
			WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet sheet = book.createSheet("Sheet1", 0);

			for (int i = 0; i < TABLE_HEADER.length; i++) {
				sheet.addCell(new Label(i, 0, TABLE_HEADER[i]));
			}
			int row = 1;
			for (Quote quote : quoteList) {
				sheet.addCell(new Label(0, row, quote.getDate() + ""));
				sheet.addCell(new Label(1, row, round2Decimal(quote.getOpen())));
				sheet.addCell(new Label(2, row, round2Decimal(quote.getClose())));
				sheet.addCell(new Label(3, row, round2Decimal(quote.getHigh())));
				sheet.addCell(new Label(4, row, round2Decimal(quote.getLow())));
				sheet.addCell(new Label(5, row, round2Decimal(quote.getVolume())));
				sheet.addCell(new Label(6, row, round2Decimal(quote.getTurnover())));
				row++;
			}

			book.write();
			book.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String round2Decimal(Double num) {
		return num + "";
	}

}
