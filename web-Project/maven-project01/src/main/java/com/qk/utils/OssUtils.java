package com.qk.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class OssUtils {

    @Value("${file.upload-path:uploads}")
    private String uploadPath;

    @Value("${file.base-url:http://localhost:8080/uploads}")
    private String baseUrl;

    private File uploadDir;

    @PostConstruct
    public void init() {
        Path path = Paths.get(uploadPath).toAbsolutePath();
        uploadDir = path.toFile();
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            log.info("上传目录创建：{}，结果：{}", uploadDir.getAbsolutePath(), created);
        } else {
            log.info("上传目录已存在：{}", uploadDir.getAbsolutePath());
        }
        if (!uploadDir.canWrite()) {
            log.warn("上传目录不可写：{}", uploadDir.getAbsolutePath());
        }
    }

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("仅支持上传图片文件（jpg、png、gif等）");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("文件大小不能超过 5MB");
        }

        String originalFilename = file.getOriginalFilename();
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;

        File dir = new File(uploadDir, "avatar/" + dateDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            log.info("创建子目录：{}，结果：{}", dir.getAbsolutePath(), created);
        }

        File targetFile = new File(dir, fileName);
        try {
            file.transferTo(targetFile);
            log.info("文件保存成功：{}，大小：{}", targetFile.getAbsolutePath(), file.getSize());
        } catch (IOException e) {
            log.error("文件保存失败：{}", e.getMessage(), e);
            throw new RuntimeException("文件保存失败：" + e.getMessage());
        }

        String url = baseUrl + "/avatar/" + dateDir + "/" + fileName;
        log.info("文件上传成功，URL：{}", url);
        return url;
    }
}
