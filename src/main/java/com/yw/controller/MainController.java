package com.yw.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
final static Logger logger = Logger.getLogger(MainController.class);
  @RequestMapping("/")
  @ResponseBody
  public String index() {
	  logger.info("Inside Main controller");
    return "Proudly handcrafted by " +
        "<a href='http://yingguowu.com'>YingguoWu</a> :)";
  }

}