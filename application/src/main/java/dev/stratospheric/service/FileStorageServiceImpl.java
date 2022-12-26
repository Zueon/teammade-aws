package dev.stratospheric.service;


import dev.stratospheric.dto.ResumeDto;
import dev.stratospheric.entity.File;
import dev.stratospheric.entity.Member;
import dev.stratospheric.entity.Project;
import dev.stratospheric.entity.Resume;
import dev.stratospheric.persistence.FileRepository;
import dev.stratospheric.persistence.MemberRepository;
import dev.stratospheric.persistence.ProjectRepository;
import dev.stratospheric.persistence.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileStorageServiceImpl implements FileStorageService {
    private final FileRepository fileRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ResumeRepository resumeRepository;
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    // ---------------------------파일 업로드 to DB -------------------------//
    @Override
    public File store(MultipartFile file, Long projectId) throws IOException {
        log.info("store");
        Project temp = projectRepository.findById(projectId).get();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("filename" + fileName);
        File File = new File(fileName, file.getContentType(), file.getBytes(), temp);
        log.info(File);

        return fileRepository.save(File);
    }

    @Override
    @Transactional
    public Resume storeResume(MultipartFile file, String email) throws IOException {
        log.info("store");
        Member creator = memberRepository.findByEmail(email).get();
        log.info(creator);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info("filename" + fileName);
        Resume resume = new Resume(fileName, file.getContentType(), file.getBytes(), creator);
        creator.setResume(resume);


        return resumeRepository.save(resume);
    }

    @Override
    public File getFile(String id) {
        return fileRepository.findById(id).get();
    }

    @Override
    public ResumeDto getResume(Member member) {
        Resume file = resumeRepository.findByCreator(member);
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/")
                .path(file.getRid())
                .toUriString();

        return ResumeDto.builder()
                .createdAt(file.getCreatedAt())
                .name(file.getName())
                .url(fileDownloadUri)
                .build();
    }

    @Override
    public Stream<File> getAllFiles(Project project) {
        return fileRepository.findByProject(project).stream();
    }

    @Override
    public Stream<File> deleteFile(String fileId) {
        Project project = fileRepository.findById(fileId).get().getProject();
        try{
            fileRepository.deleteById(fileId);
        } catch (Exception e){
            log.error(e);
            throw new RuntimeException("Error deleting Entity");

        }

        return getAllFiles(project);

    }
}
