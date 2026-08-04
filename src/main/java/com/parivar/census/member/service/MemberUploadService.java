package com.parivar.census.member.service;
import com.parivar.census.member.dto.response.MemberUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MemberUploadService {

    MemberUploadResponse uploadMembers(MultipartFile file);

}