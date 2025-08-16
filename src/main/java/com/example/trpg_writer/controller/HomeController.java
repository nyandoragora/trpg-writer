package com.example.trpg_writer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public final class HomeController {

  @GetMapping("/")
  public String index(){
    return "index";
  }

}
