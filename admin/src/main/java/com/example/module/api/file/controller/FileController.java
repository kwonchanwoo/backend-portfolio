package com.example.module.api.file.controller;

import com.example.module.api.file.dto.request.RequestFileIdDto;
import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.api.file.service.FileService;
import com.example.module.entity.File;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("files")
public class FileController {
    private final FileService fileService;

    @GetMapping
    public Page<ResponseFileDto> getFileList(@RequestParam(required = false) HashMap<String,Object> filters, Pageable pageable){
        return fileService.getFileList(filters, pageable);
    }

    @PostMapping("/upload")
    public ResponseEntity<List<Long>> fileUpload(
            @RequestParam(name = "file_category") String fileCategoryStr,
            @RequestParam(name = "files") List<MultipartFile> files,
            @RequestParam(name = "description",required = false) String description
    ) {
        return fileService.fileUpload(fileCategoryStr, files,description);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> fileDownload(@RequestParam(name = "file") File file){
        return fileService.fileDownload(file);
    }

    @DeleteMapping
    public void fileDelete(@RequestBody List<RequestFileIdDto> fileIdList){
        fileService.fileDelete(fileIdList);
    }
}
