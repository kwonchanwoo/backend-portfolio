package com.example.module.repository.file;

import com.example.module.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long>, FileCustomRepository {
    List<File> findByIdIn(Collection<Long> ids);


}
