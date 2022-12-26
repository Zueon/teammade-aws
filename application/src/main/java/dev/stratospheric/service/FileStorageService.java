package dev.stratospheric.service;

import dev.stratospheric.dto.ResumeDto;
import dev.stratospheric.entity.File;
import dev.stratospheric.entity.Member;
import dev.stratospheric.entity.Project;
import dev.stratospheric.entity.Resume;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
     void init();

     void save(MultipartFile file);

     Resource load(String filename);

     void deleteAll();

     Stream<Path> loadAll();

     Resume storeResume(MultipartFile file, String email) throws IOException;
     ResumeDto getResume(Member member);

     File store(MultipartFile file, Long projectId) throws IOException;
     File getFile(String id);
     Stream<File> getAllFiles(Project project);
     Stream<File> deleteFile(String fileId);

}
