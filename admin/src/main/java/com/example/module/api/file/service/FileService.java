package com.example.module.api.file.service;

import com.example.module.api.file.dto.request.RequestFileIdDto;
import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.dto.FileCategoryRolePK;
import com.example.module.entity.FileCategory;
import com.example.module.repository.file.FileCategoryRoleRepository;
import com.example.module.repository.file.FileRepository;
import com.example.module.repository.file.FileCategoryRepository;
import com.example.module.util.CommonException;
import com.example.module.util.SecurityContextHelper;
import com.example.module.util._Enum.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {
    private final FileRepository fileRepository;
    private final FileCategoryRepository fileCategoryRepository;
    @Value("${spring.servlet.multipart.location}")
    private String FILE_PATH;
    private final FileCategoryRoleRepository fileCategoryRoleRepository;

    public Page<ResponseFileDto> getFileList(HashMap<String, Object> filters, Pageable pageable) {
        return fileRepository.getFileList(filters, pageable);
    }

    @Transactional
    public ResponseEntity<List<Long>> fileUpload(String fileCategoryStr, List<MultipartFile> files,String description) {

        // 파일 카테고리 체크
        FileCategory fileCategory = fileCategoryRepository
                .findByNameAndDeletedFalse(fileCategoryStr)
                .orElseThrow(() -> new CommonException(ErrorCode.FILE_CATEGORY_NOT_EXISTS));

        // 파일 카테고리 권한 체크(일반유저)
        if (!SecurityContextHelper.isAdmin()) {
            fileCategoryRoleRepository
                    .findById(new FileCategoryRolePK(SecurityContextHelper.getPrincipal(), fileCategory))
                    .orElseThrow(() -> new CommonException(ErrorCode.FILE_CATEGORY_ROLE_NOT_EXISTS));
        }

        // 파일이있는지 체크
        if (files.isEmpty()) {
            throw new CommonException(ErrorCode.FILE_EMPTY);
        }

        validationFile(files);

        return ResponseEntity.ok().body(upload(fileCategory, files,description));
    }

    private void validationFile(List<MultipartFile> files) {
        // 1. 파일 최대 용량 체크
        long maxSize;
        maxSize = 1024 * 1024 * 1024;
        for (MultipartFile file : files) {
            if (file.getSize() > maxSize) {
                throw new CommonException(ErrorCode.FILE_SIZE_EXCEEDED);
            }

//            // 2-2 파일 형식자가 svg일때는 에러 처리
//            if ("svg".equals(FilenameUtils.getExtension(file.getOriginalFilename()))) {
//                throw new CommonException(ErrorCode.FILE_EXTENSION_INVALID);
//            }
        }
    }

    @Transactional
    public List<Long> upload(FileCategory fileCategory, List<MultipartFile> files,String description) {
        String dateDirectory = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        File fileDirectory = new File(FILE_PATH + File.separator + dateDirectory);

        List<Long> resultFileId = new ArrayList<>();

        files.forEach(file -> {
            String mimeType = file.getContentType();


            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            File newFileName = new File(fileDirectory.getPath(), UUID.randomUUID() + "-" + file.getOriginalFilename());

            try {
                if (fileDirectory.mkdirs()) {
                    file.transferTo(newFileName);
                    resultFileId.add(
                            fileRepository.save(
                                    com.example.module.entity.File.builder()
                                            .storedName(newFileName.getName())
                                            .originName(file.getOriginalFilename())
                                            .mime(mimeType)
                                            .path(fileDirectory.getPath())
                                            .extension(extension)
                                            .size(file.getSize())
                                            .fileCategory(fileCategory)
                                            .description(description)
                                            .build()
                            ).getId()
                    );
                } else {
                    file.transferTo(newFileName);
                    resultFileId.add(
                            fileRepository.save(
                                    com.example.module.entity.File.builder()
                                            .storedName(newFileName.getName())
                                            .originName(file.getOriginalFilename())
                                            .mime(mimeType)
                                            .path(fileDirectory.getPath())
                                            .extension(extension)
                                            .size(file.getSize())
                                            .fileCategory(fileCategory)
                                            .description(description)
                                            .build()
                            ).getId()
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return resultFileId;
    }

    public ResponseEntity<Resource> fileDownload(com.example.module.entity.File file) {
        try {
            // 파일 경로 지정
            Path filePath = Paths.get(file.getPath()).resolve(file.getStoredName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginName() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Transactional
    public void fileDelete(List<RequestFileIdDto> fileIdList) {
        List<com.example.module.entity.File> files = fileRepository
                .findByIdIn(fileIdList.stream()
                        .map(RequestFileIdDto::getId)
                        .collect(Collectors.toList()));

        for (com.example.module.entity.File file : files) {
            File fileDirectory = new File(file.getPath() + File.separator + file.getStoredName());
            if (fileDirectory.exists()) {
                boolean isSuccess = fileDirectory.delete();

                if (isSuccess) {
                    file.setDeleted(true);
                    fileRepository.save(file);
                }
            }
        }
    }
}
