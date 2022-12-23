package dev.stratospheric;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestTestController {

  @GetMapping
  public ResponseEntity<?> test(){
    String message = "RestTestController - test()";
    return ResponseEntity.ok().body(message);
  }


}
