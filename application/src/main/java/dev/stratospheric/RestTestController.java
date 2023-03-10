package dev.stratospheric;

import dev.stratospheric.entity.Test;
import dev.stratospheric.entity.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/auth")
  public ResponseEntity<?> authtest(){
    return ResponseEntity.ok("ADMIN");
  }

 @GetMapping("/test")
  public ResponseEntity<?> addTest(){
    Test sample = new Test();
    sample.setMessage("test sample");

    testRepository.save(sample);

    return ResponseEntity.ok().body("test");

 }

 @GetMapping("/test/get")
  public ResponseEntity<?> testGet(){
    List<Test> list =  testRepository.findAll();
    return ResponseEntity.ok().body(list);

 }

}
