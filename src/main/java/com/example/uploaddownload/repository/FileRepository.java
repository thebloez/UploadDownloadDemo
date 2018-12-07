package com.example.uploaddownload.repository;

import com.example.uploaddownload.model.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<DBFile, String> {
}
