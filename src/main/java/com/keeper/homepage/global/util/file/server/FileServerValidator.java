package com.keeper.homepage.global.util.file.server;

import com.keeper.homepage.global.error.BusinessException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.tika.Tika;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.keeper.homepage.global.error.ErrorCode.FILE_INVALID_TYPE;

class FileServerValidator {

    private static final Map<String, Set<String>> ALLOWED_FILE_TYPES;
    private static final Tika TIKA = new Tika();
    
    static {
        ALLOWED_FILE_TYPES = new HashMap<>();
        
        ALLOWED_FILE_TYPES.put("image/jpeg", Set.of(".jpg", ".jpeg"));
        ALLOWED_FILE_TYPES.put("image/png", Set.of(".png"));
        ALLOWED_FILE_TYPES.put("image/gif", Set.of(".gif"));
        ALLOWED_FILE_TYPES.put("image/svg+xml", Set.of(".svg"));
        ALLOWED_FILE_TYPES.put("image/bmp", Set.of(".bmp"));
        ALLOWED_FILE_TYPES.put("image/x-icon", Set.of(".ico"));
        ALLOWED_FILE_TYPES.put("video/mp4", Set.of(".mp4"));
        ALLOWED_FILE_TYPES.put("audio/mpeg", Set.of(".mp3"));
        ALLOWED_FILE_TYPES.put("audio/wav", Set.of(".wav"));
        ALLOWED_FILE_TYPES.put("text/plain", Set.of(".txt"));
        ALLOWED_FILE_TYPES.put("application/pdf", Set.of(".pdf"));
        ALLOWED_FILE_TYPES.put("application/msword", Set.of(".doc"));
        ALLOWED_FILE_TYPES.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", Set.of(".docx"));
        ALLOWED_FILE_TYPES.put("application/vnd.ms-excel", Set.of(".xls"));
        ALLOWED_FILE_TYPES.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Set.of(".xlsx"));
        ALLOWED_FILE_TYPES.put("application/vnd.ms-powerpoint", Set.of(".ppt"));
        ALLOWED_FILE_TYPES.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", Set.of(".pptx"));
        ALLOWED_FILE_TYPES.put("application/zip", Set.of(".zip"));
        ALLOWED_FILE_TYPES.put("application/x-7z-compressed", Set.of(".7z"));
        ALLOWED_FILE_TYPES.put("application/vnd.hancom.hwpx", Set.of(".hwp", ".hwpx"));
    }

    private FileServerValidator() {
    }

    public static void validate(MultipartFile file) {
        String mimeType = detectMimeType(file);
        String extension = extractExtension(file.getOriginalFilename());
        
        if (mimeType == null || !ALLOWED_FILE_TYPES.containsKey(mimeType)) {
            throw new BusinessException(mimeType, "mimeType", FILE_INVALID_TYPE);
        }
        
        Set<String> allowedExtensions = ALLOWED_FILE_TYPES.get(mimeType);
        if (extension == null || !allowedExtensions.contains(extension)) {
            throw new BusinessException(extension, "extension", FILE_INVALID_TYPE);
        }
    }

    private static String extractExtension(String filename) {
        String extension = StringUtils.getFilenameExtension(filename);
        if (extension == null) {
            return null;
        }
        return "." + extension.toLowerCase();
    }

    private static String detectMimeType(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return TIKA.detect(inputStream, file.getOriginalFilename());
        } catch (IOException e) {
            throw new BusinessException("unknown", "mimeType", FILE_INVALID_TYPE);
        }
    }
}
