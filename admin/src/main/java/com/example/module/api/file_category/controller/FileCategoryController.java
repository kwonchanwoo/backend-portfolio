package com.example.module.api.file_category.controller;

import com.example.module.api.file_category.dto.request.RequestFileCategoryRoleDto;
import com.example.module.api.file_category.dto.response.ResponseFileCategoryDto;
import com.example.module.api.file_category.dto.response.ResponseFileCategoryMemberDto;
import com.example.module.api.file_category.service.FileCategoryService;
import com.example.module.entity.FileCategory;
import com.example.module.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequestMapping("fileCategories")
@RestController
@RequiredArgsConstructor
public class FileCategoryController {
    private final FileCategoryService fileCategoryService;

    /**
     * 파일 카테고리 목록
     *
     *
     */
    @GetMapping
    public List<ResponseFileCategoryDto> getFileCategoryList(){
        return fileCategoryService.getFileCategoryList();
    }

    /**
     * 파일 카테고리 추가
     *
     * @param fileCategoryName
     */
    @PostMapping("/fileCategory/{fileCategoryName}")
    @ResponseStatus(HttpStatus.OK)
    public void postFileCategory(@PathVariable String fileCategoryName) {
        fileCategoryService.postFileCategory(fileCategoryName);
    }

    /**
         * 파일 카테고리 접근 회원 목록
         * @param fileCategory
         * @param filters
         * @param pageable
         * @return
         */
        @GetMapping("/{fileCategory}/members")
        public Page<ResponseFileCategoryMemberDto> getFileCategoryMemberList(
                @PathVariable(name = "fileCategory") FileCategory fileCategory,
                @RequestParam(required = false) Map<String,Object> filters, Pageable pageable){
            return fileCategoryService.getFileCategoryMemberList(fileCategory,filters,pageable);
    }

    /**
     * 파일 카테고리 권한 제어
     *
     * @param requestFileCategoryRoleDto
     */
    @PostMapping("/fileCategory/role")
    @ResponseStatus(HttpStatus.OK)
    public void postFileCategoryRole(@RequestBody RequestFileCategoryRoleDto requestFileCategoryRoleDto){
        fileCategoryService.postFileCategoryRole(requestFileCategoryRoleDto);
    }

    /**
     * 파일 카테고리 삭제
     *
     * @param fileCategoryId
     */
    @DeleteMapping("fileCategory/{fileCategoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFileCategory(@PathVariable Long fileCategoryId) {
        fileCategoryService.deleteFileCategory(fileCategoryId);
    }
}
