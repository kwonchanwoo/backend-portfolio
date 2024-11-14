package com.example.module.repository.file;

import com.example.module.entity.File;
import com.example.module.repository.FileCoreRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface FileRepository extends FileCoreRepository, FileCustomRepository {
    List<File> findByIdIn(Collection<Long> ids);
}
