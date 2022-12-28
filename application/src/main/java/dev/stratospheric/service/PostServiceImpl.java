package dev.stratospheric.service;

import dev.stratospheric.entity.Post;
import dev.stratospheric.entity.Project;
import dev.stratospheric.entity.Study;
import dev.stratospheric.persistence.ProjectRepository;
import dev.stratospheric.persistence.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
  private final ProjectRepository projectRepository;
  private final StudyRepository studyRepository;

  @Override
  public Project getProject(Long pid) {
    return projectRepository.findById(pid).orElseThrow(() -> new IllegalArgumentException("invalid project"));
  }

  @Override
  public Project createProject(Project project) {
    validate(project);
    Project saveProject = projectRepository.save(project);
    log.info("Entity ID : {} is saved", project.getId());
    return saveProject;
  }

  @Override
  public List<Project> getProjectList() {
    return projectRepository.findAll();
  }

  @Override
  public Project updateProject(Project project) {
    validate(project);

    Optional<Project> original = projectRepository.findById(project.getId());
    original.ifPresent(proj -> {
      proj.setStartdate(project.getStartdate());
      proj.setEnddate(project.getEnddate());
      proj.setPublic_(project.getPublic_());
      proj.setIntroduction(project.getIntroduction());
      proj.setLocation_(project.getLocation_());
      proj.setCategory_(project.getCategory_());
      proj.setTitle(project.getTitle());

      projectRepository.save(proj);

    });

    return getProject(project.getId());
  }

  @Override
  @Transactional
  public List<Project> deleteProject(Project project) {
    validate(project);
    try {
      project.removeAllMember();
      project.removeTodos();
      projectRepository.delete(project);
    } catch (Exception e) {
      log.error("ERROR DELETING ENTITY" + project.getId(), e);
      throw new RuntimeException("ERROR DELETING ENTITY" + project.getId());
    }

    return getProjectList();
  }


  private void validate(Post post) {
    if (post == null) {
      log.warn("Post Entity cannot be null");
      throw new RuntimeException("Entity cannot be null.");
    }


  }

  @Override
  public Study createStudy(Study study) {
    validate(study);
    Study saveStudy = studyRepository.save(study);
    log.info("Entity ID : {} is saved", study.getId());
    return saveStudy;

  }

  @Override
  public List<Study> getStudyList() {

    return studyRepository.findAll();
  }

  @Override
  public Study updateStudy(Study study) {
    return null;
  }

  @Override
  public List<Study> deleteStudy(Study study) {
    return null;
  }
}
