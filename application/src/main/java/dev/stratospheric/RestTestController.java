package dev.stratospheric;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestTestController {

  @GetMapping("/get")
  public ResponseEntity<?> test(){
    String message = "RestTestController - test() - cicdtest";
    return ResponseEntity.ok().body(message);
  }


}
