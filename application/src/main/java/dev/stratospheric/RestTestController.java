package dev.stratospheric;

import dev.stratospheric.entity.Test;
import dev.stratospheric.entity.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class RestTestController {

  @Autowired
  private TestRepository testRepository;

  @GetMapping("/get3")
  public ResponseEntity<?> test(){
    String message = "RestTestController - test() - cicdtest";
    return ResponseEntity.ok().body(message);
  }

  @GetMapping("/auth")
  public ResponseEntity<?> authtest(){
    return null;
  }

 @PostMapping("/test")
  public ResponseEntity<?> addTest(@RequestBody Test test){
    Test sample = new Test();
    sample.setMessage("test sample");

    testRepository.save(sample);

    return ResponseEntity.ok().body("test");

 }


}
