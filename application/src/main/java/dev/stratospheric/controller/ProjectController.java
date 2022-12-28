package dev.stratospheric.controller;

import dev.stratospheric.dto.FileDto;
import dev.stratospheric.dto.ProjectDTO;
import dev.stratospheric.dto.ResponseDTO;
import dev.stratospheric.dto.TodoDto;
import dev.stratospheric.entity.Member;
import dev.stratospheric.entity.Project;
import dev.stratospheric.entity.Todo;
import dev.stratospheric.persistence.ProjectRepository;
import dev.stratospheric.service.FileStorageService;
import dev.stratospheric.service.MemberService;
import dev.stratospheric.service.PostService;
import dev.stratospheric.service.TodoService;
import dev.stratospheric.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

  private final PostService postService;
  private final MemberService memberService;
  private final FileStorageService storageService;
  private final TodoService todoService;
  private final ProjectRepository projectRepository;




  // 프로젝트 리스트 받기
  @GetMapping("/list")
  @Transactional
  public ResponseEntity<?> getProjects() {
    // Entities 받은 후 이를 바로 클라이언트로 반환 X
    // Entity를 DTO로 변환하여 보내야할 정보만 반환한다.
    List<Project> projectEntities = postService.getProjectList();
    List<ProjectDTO> projectList = projectEntities.stream().map(ProjectDTO::new).collect(Collectors.toList());

    // Response를 위한 DTO의 data에 담아서 전달
    ResponseDTO<ProjectDTO> response = ResponseDTO.<ProjectDTO>builder().data(projectList).build();

    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/{pid}")
  public ResponseEntity<?> getProject(@PathVariable Long pid){
    Project project = postService.getProject(pid);
    ProjectDTO projectDTO = new ProjectDTO(project);

    ResponseDTO<ProjectDTO> response = ResponseDTO.<ProjectDTO>builder().data(projectDTO).build();

    return ResponseEntity.ok().body(response);

  }

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> createPost(@RequestBody ProjectDTO dto) {
    String memberEmail = SecurityUtil.getCurrentMemberEmail();
    Member loginedMember = memberService.getMemberByEmail(memberEmail);
    try {

      Project project = new Project(dto);
      project.addHost(loginedMember);
      project.addMember(loginedMember);

      Project createProject = postService.createProject(project);
      ProjectDTO createProjectDTO = new ProjectDTO(createProject);

      ResponseDTO<ProjectDTO> response = ResponseDTO.<ProjectDTO>builder().data(createProjectDTO).build();
      return ResponseEntity.ok().body(response);

    } catch (Exception e) {
      String err = e.getMessage();
      ResponseDTO<ProjectDTO> response = ResponseDTO.<ProjectDTO>builder().error(err).build();

      return ResponseEntity.badRequest().body(response);
    }
  }

  @PutMapping
  @PreAuthorize("hasRole('HOST')")
  public ResponseEntity<?> updatePost(@RequestBody ProjectDTO dto) {

    try {
      Project project = new Project(dto);

      Project updatedProject = postService.updateProject(project);
      ProjectDTO updatedProjectDTO = new ProjectDTO(updatedProject);

      ResponseDTO<ProjectDTO> response = ResponseDTO.<ProjectDTO>builder().data(updatedProjectDTO).build();
      return ResponseEntity.ok().body(response);

    } catch (Exception e) {
      String err = e.getMessage();
      ResponseDTO<ProjectDTO> response = ResponseDTO.<ProjectDTO>builder().error(err).build();

      return ResponseEntity.badRequest().body(response);
    }
  }

  @DeleteMapping
  @PreAuthorize("hasRole('HOST')")
  public ResponseEntity<?> deletePost (@RequestBody ProjectDTO dto){
    try {
      Project projectEntity = new Project(dto);
      List<Project> entities = postService.deleteProject(projectEntity);
      List<ProjectDTO> projectList = entities.stream().map(ProjectDTO::new).collect(Collectors.toList());

      ResponseDTO<ProjectDTO> response = ResponseDTO.<ProjectDTO>builder().data(projectList).build();

      return ResponseEntity.ok().body(response);

    } catch (Exception e){
      String err = e.getMessage();
      ResponseDTO<ProjectDTO> response = ResponseDTO.<ProjectDTO>builder().error(err).build();

      return ResponseEntity.badRequest().body(response);
    }

  }
  //--------------------------- 프로젝트 파일 처리 ----------------------------//

  private List<FileDto> getProjectFileList(Project project) {
    List<FileDto> files = storageService.getAllFiles(project).map(dbFile -> {
      String fileDownloadUri = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/files/")
        .path(dbFile.getId())
        .toUriString();

      return new FileDto(
        dbFile.getName(),
        fileDownloadUri,
        dbFile.getType(),
        dbFile.getData().length);
    }).collect(Collectors.toList());
    return files;
  }


  @PostMapping("/{pid}/files/upload")
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("pid") Long pid) {
    String message = "";

    try {
      log.info("uploadFile");
      storageService.store(file, pid);

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.builder().message(message).build());
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseDTO.builder().message(message).build());
    }
  }


  @GetMapping("/{pid}/files")
  public ResponseEntity<List<FileDto>> getListDBFiles(@PathVariable("pid") Long pid) {
    Project project = projectRepository.findById(pid).get();
    List<FileDto> files = getProjectFileList(project);

    return ResponseEntity.status(HttpStatus.OK).body(files);
  }

  @DeleteMapping("/{pid}/files")
  public ResponseEntity<List<FileDto>> deleteFile(@PathVariable Long pid, @RequestParam("fileId") String fileId){
    Project project = projectRepository.findById(pid).get();

    storageService.deleteFile(fileId);
    List<FileDto> files = getProjectFileList(project);

    return ResponseEntity.status(HttpStatus.OK).body(files);

  }


  //--------------------------- 프로젝트 파일 처리 END ----------------------------//



  //--------------------------- 프로젝트 TODOLIST 처리 ----------------------------//

  @GetMapping("/{pid}/todos")
  public ResponseEntity<?> retrieveTodoList(@PathVariable Long pid) {
    Project temp = projectRepository.findById(pid).get();

    List<Todo> entities = todoService.retrieve(temp);
    List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

    ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(dtos).build();

    return ResponseEntity.ok(response);
  }


  @PostMapping("/{pid}/todos")
  public ResponseEntity<?> createTodo(@RequestBody TodoDto dto, @PathVariable Long pid) {
    Project temp = projectRepository.findById(pid).get();
    Member creator = memberService.getMemberByEmail(SecurityUtil.getCurrentMemberEmail());
    String creatorNickname = creator.getNickname();

    try {

      Todo entity = TodoDto.toEntity(dto);
      entity.setCreator(creatorNickname);
      temp.addTodo(entity);

      List<Todo> entities = todoService.create(entity);

      List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

      ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(dtos).build();

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      String err = e.getMessage();
      ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().error(err).build();

      return ResponseEntity.badRequest().body(response);
    }
  }

  @PutMapping("/{pid}/todos")
  public ResponseEntity<?> updateTodo(@RequestBody TodoDto reqDto,  @PathVariable Long pid){
    Todo todoEntity = TodoDto.toEntity(reqDto);

    try{
      todoEntity.setProject(projectRepository.findById(pid).get());
      List<Todo> entities = todoService.update(todoEntity);
      List<TodoDto> resDtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

      ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(resDtos).build();

      return ResponseEntity.ok().body(response);
    } catch (Exception e){
      String error = e.getMessage();
      log.error(error);
      ResponseDTO response = ResponseDTO.builder().error(error).build();

      return ResponseEntity.badRequest().body(response);
    }
  }


  @DeleteMapping("/{pid}/todos")
  public ResponseEntity<?> deleteTodo(@RequestBody TodoDto reqDto, @PathVariable Long pid){
    try{
      Todo todoEntity = TodoDto.toEntity(reqDto);
      todoEntity.setProject(projectRepository.findById(pid).get());

      List<Todo> entities = todoService.delete(todoEntity);
      List<TodoDto> resDtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

      ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(resDtos).build();

      return ResponseEntity.ok().body(response);
    } catch (Exception e){
      String error = e.getMessage();
      ResponseDTO response = ResponseDTO.builder().error(error).build();
      return ResponseEntity.badRequest().body(response);
    }
  }
  //--------------------------- 프로젝트 TODOLIST 처리 END ----------------------------//


}
